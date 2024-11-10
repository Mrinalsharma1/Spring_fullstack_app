package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException(){
        super(RBundle.getKey(PredefinedConstants.MANAGER_NOT_FOUND));
    }
    public ManagerNotFoundException(String message) {
        super(message);
    }
}
