package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class ManagerNotAssignedException extends RuntimeException {
    public ManagerNotAssignedException(){
        super(RBundle.getKey(PredefinedConstants.MANAGER_NOT_ASSIGNED));
    }
    public ManagerNotAssignedException(String message) {
        super(message);
    }
}
