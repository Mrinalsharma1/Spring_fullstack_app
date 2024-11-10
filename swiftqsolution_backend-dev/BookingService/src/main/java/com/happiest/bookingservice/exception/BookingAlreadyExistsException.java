package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class BookingAlreadyExistsException extends RuntimeException {
    public BookingAlreadyExistsException(){
        super(RBundle.getKey(PredefinedConstants.BAE));
    }
    public BookingAlreadyExistsException(String message) {
        super(message);
    }
}
