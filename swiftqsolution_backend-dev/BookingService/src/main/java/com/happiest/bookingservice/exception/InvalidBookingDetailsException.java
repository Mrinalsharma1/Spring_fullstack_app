package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class InvalidBookingDetailsException extends RuntimeException {
    public InvalidBookingDetailsException(){
        super(RBundle.getKey(PredefinedConstants.IBD));
    }
    public InvalidBookingDetailsException(String message) {
        super(message);
    }
}
