package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class NoTimingsFoundException extends RuntimeException {
  public NoTimingsFoundException() {
    super(RBundle.getKey(PredefinedConstants.NTF));
  }
    public NoTimingsFoundException(String message) {
        super(message);
    }
}
