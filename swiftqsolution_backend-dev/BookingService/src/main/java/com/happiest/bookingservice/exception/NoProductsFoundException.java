package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class NoProductsFoundException extends RuntimeException {
    public NoProductsFoundException() {
        super(RBundle.getKey(PredefinedConstants.NPF));
    }
    public NoProductsFoundException(String message) {
        super(message);
    }
}
