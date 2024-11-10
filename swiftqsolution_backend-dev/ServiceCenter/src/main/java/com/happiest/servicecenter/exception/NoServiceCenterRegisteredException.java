package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class NoServiceCenterRegisteredException extends RuntimeException {
    public NoServiceCenterRegisteredException(){
        super(RBundle.getKey(PredefinedConstants.SERVICECENTER_ALREADY_REGISTERED));
    }
    public NoServiceCenterRegisteredException(String message) {
        super(message);
    }
}
