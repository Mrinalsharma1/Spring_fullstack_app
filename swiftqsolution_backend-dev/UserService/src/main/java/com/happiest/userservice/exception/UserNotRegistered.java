package com.happiest.userservice.exception;

import com.happiest.userservice.constant.PredefinedConstants;
import com.happiest.userservice.utility.RBundle;

public class UserNotRegistered extends Exception {


    public UserNotRegistered(){
        super(RBundle.getKey(PredefinedConstants.USER_NOT_REGISTERED));
    }
    public UserNotRegistered(String message) {
        super(message);
    }
}