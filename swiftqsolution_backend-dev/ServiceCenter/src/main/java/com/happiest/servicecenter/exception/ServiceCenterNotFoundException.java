package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ServiceCenterNotFoundException extends RuntimeException {
    public ServiceCenterNotFoundException(){
        super(RBundle.getKey(PredefinedConstants.SCNF));
    }
    public ServiceCenterNotFoundException(String message) {
        super(message);
    }
}
