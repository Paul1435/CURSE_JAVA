package org.example;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Lot {
    volatile private BigDecimal betPrice;
    volatile private String lastUserName;
    private final String lotName;
    private Optional<String> winner = Optional.empty();
    private final LocalDateTime endOfTheAuction;

    public Lot(String lotName, LocalDateTime time, BigDecimal firstPrice) {
        Objects.requireNonNull(time, "не установлено время");
        if (time.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Неверный время формат");
        }
        this.endOfTheAuction = time;
        Objects.requireNonNull(lotName, "Пустое имя Лота");
        this.lotName = lotName;
        Objects.requireNonNull(firstPrice, "не установлена цена");
        if (firstPrice.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        betPrice = firstPrice;
    }

    public void makeBet(BigDecimal curPrice, String user) {
        if (curPrice.compareTo(betPrice) > 0) {
            synchronized (this) {
                if (LocalDateTime.now().isBefore(endOfTheAuction) && curPrice.compareTo(betPrice) > 0) {
                    betPrice = curPrice;
                    lastUserName = user;
                }
            }
        }
    }

    public BigDecimal getBet() {
        return betPrice;
    }

    public Optional<String> getWinner() {
        if (LocalDateTime.now().isAfter(endOfTheAuction)) {
            winner = Optional.ofNullable(lastUserName);
        }
        return winner;
    }
}
