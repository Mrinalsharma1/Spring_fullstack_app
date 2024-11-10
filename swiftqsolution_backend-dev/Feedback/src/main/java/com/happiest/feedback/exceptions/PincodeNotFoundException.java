package com.happiest.feedback.exceptions;


import com.happiest.feedback.constant.PredefinedConstants;
import com.happiest.feedback.utility.RBundle;

public class PincodeNotFoundException extends RuntimeException {
    public PincodeNotFoundException() {
        super(RBundle.getKey(PredefinedConstants.PNF));
    }
    public PincodeNotFoundException(String message) {
        super(message);
    }
}
