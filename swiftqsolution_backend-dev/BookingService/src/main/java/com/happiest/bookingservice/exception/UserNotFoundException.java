package com.happiest.bookingservice.exception;


import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super(RBundle.getKey(PredefinedConstants.NUF));
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
