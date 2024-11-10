package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class ProductAlreadyAddedException extends RuntimeException {
    public ProductAlreadyAddedException(){
        super(RBundle.getKey(PredefinedConstants.PAA));
    }
    public ProductAlreadyAddedException(String message) {
        super(message);
    }
}
