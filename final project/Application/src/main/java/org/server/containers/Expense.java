package org.server.containers;

import java.math.BigDecimal;

public record Expense(Integer candidateId, BigDecimal amount, String description, Integer managerId) {
}
