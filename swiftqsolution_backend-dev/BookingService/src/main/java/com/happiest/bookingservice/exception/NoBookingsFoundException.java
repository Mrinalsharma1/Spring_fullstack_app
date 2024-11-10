package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class NoBookingsFoundException extends RuntimeException {
  public NoBookingsFoundException() {
    super(RBundle.getKey(PredefinedConstants.NBF));
  }
    public NoBookingsFoundException(String message) {
        super(message);
    }
}
