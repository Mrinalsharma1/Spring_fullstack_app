package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class NoManagerAssignedException extends RuntimeException {
    public NoManagerAssignedException(){
        super(RBundle.getKey(PredefinedConstants.NO_MANAGER_ASSIGNED));
    }
    public NoManagerAssignedException(String message) {
        super(message);
    }
}
