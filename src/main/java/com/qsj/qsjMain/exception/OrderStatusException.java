package com.qsj.qsjMain.exception;

/**
 * exception for delivery order query error
 */
public class OrderStatusException extends Exception{
    public OrderStatusException(String message) {
        super(message);
    }
}
