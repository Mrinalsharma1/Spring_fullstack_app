package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class InvalidPincodeException extends RuntimeException {
    public InvalidPincodeException() {
        super(RBundle.getKey(PredefinedConstants.IP));
    }
    public InvalidPincodeException(String message) {
        super(message);
    }
}
