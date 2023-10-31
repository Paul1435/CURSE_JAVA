package org.crock.order;

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

public class Order {
    public enum OrderStatus {
        CREATED,
        COLLECTED,
        EXPIRED,
        CLOSED
    }

    private OrderStatus status;
    private String numberOfOrder; // Номер заказа
    private final LocalDateTime dateOfReceipt; // Дата поступлления
    private LocalDateTime orderBuildDate; // день сбора заказа
    private LocalDateTime DateOfIssue; // Дата выдачи
    private final Product[] orderComposition;
    private final String nameClient;
    private final String numberPhone;

    public OrderStatus getStatus() {
        return status;
    }

    public boolean readyToIssue() throws IllegalStateException {
        if (orderBuildDate == null) {
            return false;
        } else if (!LocalDateTime.now().isAfter(DateOfIssue)) {
            return true;
        } else {
            throw new IllegalStateException("Срок хранения заказа истек");
        }
    }

    private void setNumberOfOrder() {
        numberOfOrder = dateOfReceipt.format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) +
                numberPhone.substring(numberPhone.length() - 4);
    }

    public boolean hasTheDeadlineExpired() {
        boolean hasTime = !LocalDateTime.now().isAfter(DateOfIssue);
        if (!hasTime) {
            status = OrderStatus.EXPIRED;
        }
        return !hasTime;
    }

    public void getOrder() throws IllegalStateException {
        if (!hasTheDeadlineExpired()) {
            status = OrderStatus.CLOSED;
        } else {
            throw new IllegalStateException("Срок хранения заказа истек");
        }

    }

    public void setOrder() throws IllegalStateException {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Заказ уже был принят");
        }
        this.orderBuildDate = LocalDateTime.now();
        setNumberOfOrder();
        DateOfIssue = orderBuildDate.plusWeeks(2);
        status = OrderStatus.COLLECTED;
    }

    public String getMessageClient() throws IllegalAccessException {
        if (numberOfOrder == null) {
            throw new IllegalAccessException("Заказ еще не собран");
        }
        String messageTemplate = """
                Уважаемый %s!
                                
                Рады сообщить, что Ваш заказ %s готов к выдаче.
                                
                Состав заказа:
                %s
                Сумма к оплате: %s
                                 
                Срок хранения заказа %s.
                                
                С наилучшими пожеланиями, магазин “Кошки и картошки.”
                """;
        String dataToIssueFormatted = DateOfIssue.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        String descriptionsOfProducts = Arrays.stream(orderComposition).map(Product::getDescription)
                .collect(Collectors.joining("\n"));

        BigDecimal sumPrice = BigDecimal.valueOf(Arrays.stream(orderComposition).mapToDouble(Product::getPrice).sum());

        NumberFormat formatNum = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

        return String.format(messageTemplate, nameClient, numberOfOrder,
                descriptionsOfProducts, formatNum.format(sumPrice), dataToIssueFormatted);
    }

    public Order(String name, String phone, Product... orderComposition) throws IllegalArgumentException {
        if (orderComposition.length > 75) {
            throw new IllegalArgumentException("Превышен лимит количества заказываемых товаров");
        }
        Pattern pattern = Pattern.compile("^((\\+7|7|8)+([0-9]){10})$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Неверный номер телефона");
        }
        dateOfReceipt = LocalDateTime.now();
        this.orderComposition = orderComposition;
        nameClient = name;
        numberPhone = phone;
        status = OrderStatus.CREATED;
    }
}