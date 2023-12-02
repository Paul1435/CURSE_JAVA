package org.server.containers;

import java.math.BigDecimal;

public record Income(Integer candidateId, BigDecimal price, String description) {
}
