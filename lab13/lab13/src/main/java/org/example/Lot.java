package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Lot {
    volatile private BigDecimal betPrice;
    volatile private String lastUserName;
    private final String lotName;
    private final LocalDateTime time;

    public Lot(String lotName, LocalDateTime time, BigDecimal firstPrice) {
        Objects.requireNonNull(time, "не установлено время");
        if (time.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Неверный время формат");
        }
        this.time = time;
        Objects.requireNonNull(lotName, "Пустое имя Лота");
        this.lotName = lotName;
        Objects.requireNonNull(firstPrice, "не установлена цена");
        if (firstPrice.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        betPrice = firstPrice;
    }

    public void makeBet(BigDecimal curPrice, String user) {
        synchronized (this) {
            if (LocalDateTime.now().isBefore(time) && curPrice.compareTo(betPrice) > 0) {
                betPrice = curPrice;
                lastUserName = user;
            }
        }
    }

    public BigDecimal getBet() {
        synchronized (this) {
            return betPrice;
        }
    }

    public String getWinner() {
        if (LocalDateTime.now().isBefore(time)) {
            return "Аукцион еще не закончился";
        }
        Objects.requireNonNull(lastUserName, "Никто  не предложил достойную ставку");
        return lastUserName;
    }
}
