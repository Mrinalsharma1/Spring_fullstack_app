package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class SlotAlreadyAddedException extends RuntimeException {
    public SlotAlreadyAddedException() {
        super(RBundle.getKey(PredefinedConstants.SLOT_EXISTS));
    }
    public SlotAlreadyAddedException(String message) {
        super(message);
    }
}
