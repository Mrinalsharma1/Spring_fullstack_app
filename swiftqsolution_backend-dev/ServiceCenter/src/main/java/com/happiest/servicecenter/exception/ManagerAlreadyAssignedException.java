package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ManagerAlreadyAssignedException extends RuntimeException {
    public ManagerAlreadyAssignedException(){
        super(RBundle.getKey(PredefinedConstants.MAA));
    }
    public ManagerAlreadyAssignedException(String message) {
        super(message);
    }
}
