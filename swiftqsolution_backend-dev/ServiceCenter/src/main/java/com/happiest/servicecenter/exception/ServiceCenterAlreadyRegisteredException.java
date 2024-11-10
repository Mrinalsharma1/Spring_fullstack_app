package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ServiceCenterAlreadyRegisteredException extends RuntimeException {
    public ServiceCenterAlreadyRegisteredException(){
        super(RBundle.getKey(PredefinedConstants.SERVICECENTER_ALREADY_REGISTERED));
    }
    public ServiceCenterAlreadyRegisteredException(String message) {
        super(message);
    }
}
