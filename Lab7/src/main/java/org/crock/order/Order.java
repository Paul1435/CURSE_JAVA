package org.crock.order;

import org.crock.exceptions.IncorrectOrderStateException;
import org.crock.product.localproduct.Product;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Order implements SendMessageClient, StatusTracking {
    public enum OrderStatus {
        CREATED,
        COLLECTED,
        EXPIRED,
        CLOSED
    }

    private OrderStatus status;
    private String numberOfOrder; // Номер заказа
    private final LocalDateTime createdDate; // Дата поступлления
    private LocalDateTime collectedDate; // день сбора заказа
    private LocalDateTime closedDate; // Дата выдачи
    private final Product[] orderComposition;
    private final String nameClient;
    private final String numberPhone;

    @Override
    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public boolean readyToIssue() throws IncorrectOrderStateException {
        if (collectedDate == null) {
            return false;
        } else if (!LocalDateTime.now().isAfter(closedDate)) {
            return true;
        } else {
            throw new IncorrectOrderStateException("Срок хранения заказа истек");
        }
    }

    private void setNumberOfOrder() {
        numberOfOrder = createdDate.format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) +
                numberPhone.substring(numberPhone.length() - 4);
    }

    @Override
    public boolean hasTheDeadlineExpired() {
        boolean hasTime = !LocalDateTime.now().isAfter(closedDate);
        if (!hasTime) {
            status = OrderStatus.EXPIRED;
        }
        return !hasTime;
    }

    @Override
    public void closeOrder() throws IncorrectOrderStateException {
        if (!hasTheDeadlineExpired()) {
            status = OrderStatus.CLOSED;
        } else {
            throw new IncorrectOrderStateException("Срок хранения заказа истек");
        }

    }

    @Override
    public void collectOrder() throws IncorrectOrderStateException {
        if (status != OrderStatus.CREATED) {
            throw new IncorrectOrderStateException("Заказ уже был принят");
        }
        this.collectedDate = LocalDateTime.now();
        closedDate = collectedDate.plusWeeks(2);
        status = OrderStatus.COLLECTED;
    }

    @Override
    public String getMessageClient() throws IncorrectOrderStateException {
        if (status != OrderStatus.COLLECTED) {
            throw new IncorrectOrderStateException("Заказ находится в состоянии " + status);
        }
        String descriptionsOfProducts = Arrays.stream(orderComposition).map(Product::getDescription)
                .collect(Collectors.joining("\n"));
        BigDecimal sumPrice = BigDecimal.valueOf(Arrays.stream(orderComposition).mapToDouble(Product::getPrice).sum());
        String dataToIssueFormatted = closedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        NumberFormat formatNum = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
        StringBuilder message = new StringBuilder()
                .append("Уважаемый " + nameClient + "!\n\n")
                .append("Рады сообщить, что Ваш заказ " + numberOfOrder + " готов к выдаче.\n\n")
                .append("Состав заказа:\n" + descriptionsOfProducts + '\n')
                .append("Сумма к оплате: " + formatNum.format(sumPrice) + "\n\n")
                .append("Срок хранения заказа " + dataToIssueFormatted + "\n\n")
                .append("С наилучшими пожеланиями, магазин “Кошки и картошки“.");
        return message.toString();
    }

    public Order(String name, String phone, Product[] products) throws IncorrectOrderStateException {
        if (products.length > 75) {
            throw new IncorrectOrderStateException("Превышен лимит количества заказываемых товаров");
        }
        Pattern pattern = Pattern.compile("^((\\+7|7|8)+([0-9]){10})$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Неверный номер телефона");
        }
        createdDate = LocalDateTime.now();
        this.orderComposition = products;
        nameClient = name;
        numberPhone = phone;
        status = OrderStatus.CREATED;
        setNumberOfOrder();
    }
}