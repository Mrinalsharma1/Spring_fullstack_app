package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException(){
        super(RBundle.getKey(PredefinedConstants.NUF));
    }
    public NoUserFoundException(String message) {
        super(message);
    }
}
