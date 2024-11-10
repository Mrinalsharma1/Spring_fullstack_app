package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class SlotNotAvailableException extends RuntimeException {
  public SlotNotAvailableException(){
    super(RBundle.getKey(PredefinedConstants.SNA));
  }
  public SlotNotAvailableException(String message) {
    super(message);
  }
}
