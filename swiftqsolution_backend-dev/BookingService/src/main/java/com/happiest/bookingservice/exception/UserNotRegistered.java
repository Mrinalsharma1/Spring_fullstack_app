package com.happiest.bookingservice.exception;


import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class UserNotRegistered extends Exception {


    public UserNotRegistered(){
        super(RBundle.getKey(PredefinedConstants.USER_NOT_REGISTERED));
    }
    public UserNotRegistered(String message) {
        super(message);
    }
}