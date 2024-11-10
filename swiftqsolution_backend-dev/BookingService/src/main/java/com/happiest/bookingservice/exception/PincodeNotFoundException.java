package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class PincodeNotFoundException extends RuntimeException {
    public PincodeNotFoundException() {
        super(RBundle.getKey(PredefinedConstants.PNF));
    }
    public PincodeNotFoundException(String message) {
        super(message);
    }
}
