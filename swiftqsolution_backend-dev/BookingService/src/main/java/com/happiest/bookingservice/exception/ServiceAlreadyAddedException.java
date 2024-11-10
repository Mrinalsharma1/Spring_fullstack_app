package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class ServiceAlreadyAddedException extends RuntimeException {
    public ServiceAlreadyAddedException() {
        super(RBundle.getKey(PredefinedConstants.SAA));
    }
    public ServiceAlreadyAddedException(String message) {
        super(message);
    }


}
