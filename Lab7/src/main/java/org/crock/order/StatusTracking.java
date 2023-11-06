package org.crock.order;

import org.crock.exceptions.IncorrectOrderStateException;

public interface StatusTracking {
    Order.OrderStatus getStatus();

    public boolean readyToIssue() throws IncorrectOrderStateException;

    public boolean hasTheDeadlineExpired();

    public void closeOrder() throws IncorrectOrderStateException;

    public void collectOrder() throws IncorrectOrderStateException;
}
