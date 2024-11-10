package com.happiest.userservice.exception;

import com.happiest.userservice.constant.PredefinedConstants;
import com.happiest.userservice.utility.RBundle;

public class UserAlreadyRegistered extends Exception {

    public UserAlreadyRegistered(){
        super(RBundle.getKey(PredefinedConstants.USER_ALREADY_EXISTS));
    }
    public UserAlreadyRegistered(String message) {
        super(message);
    }
}

