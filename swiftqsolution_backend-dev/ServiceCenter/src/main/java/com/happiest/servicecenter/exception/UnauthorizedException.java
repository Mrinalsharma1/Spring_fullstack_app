package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){
        super(RBundle.getKey(PredefinedConstants.UAE));
    }
    public UnauthorizedException(String message) {
        super(message);
    }


}
