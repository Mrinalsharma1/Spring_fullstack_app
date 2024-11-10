package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ServiceCenterWithManager extends RuntimeException {
    public ServiceCenterWithManager(){
        super(RBundle.getKey(PredefinedConstants.SCWM));
    }
    public ServiceCenterWithManager(String message) {
        super(message);
    }
}
