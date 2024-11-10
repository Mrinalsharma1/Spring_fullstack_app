package com.happiest.userservice.exception;

import com.happiest.userservice.constant.PredefinedConstants;
import com.happiest.userservice.utility.RBundle;

public class NoUsersRegistered extends Exception {
    public NoUsersRegistered(){
        super(RBundle.getKey(PredefinedConstants.NO_USER_REGISTERED));
    }
    public NoUsersRegistered(String message) {
        super(message);
    }
}

