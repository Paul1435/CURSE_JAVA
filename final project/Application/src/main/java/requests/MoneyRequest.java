package requests;

import java.math.BigDecimal;

public class MoneyRequest extends Request {
    private BigDecimal decimal;
    private String description;

    public MoneyRequest(BigDecimal decimal, String description) {
        this.decimal = decimal;
        this.description = description;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public String getDescription() {
        return description;
    }
}
