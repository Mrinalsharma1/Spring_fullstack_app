package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class NoServiceCenterWithoutManagerException extends RuntimeException {
    public NoServiceCenterWithoutManagerException(){
        super(RBundle.getKey(PredefinedConstants.NSCWM));
    }
    public NoServiceCenterWithoutManagerException(String message) {
        super(message);
    }
}
