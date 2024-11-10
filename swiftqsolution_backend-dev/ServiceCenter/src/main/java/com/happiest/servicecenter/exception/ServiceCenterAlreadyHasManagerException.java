package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ServiceCenterAlreadyHasManagerException extends RuntimeException {
    public ServiceCenterAlreadyHasManagerException(){
        super(RBundle.getKey(PredefinedConstants.SCAHM));
    }
    public ServiceCenterAlreadyHasManagerException(String message) {
        super(message);
    }
}
