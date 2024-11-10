package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class NoServicesFoundException extends RuntimeException {
  public NoServicesFoundException(){
    super(RBundle.getKey(PredefinedConstants.NSF));
  }
    public NoServicesFoundException(String message) {
        super(message);
    }
}
