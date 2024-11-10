package com.happiest.bookingservice.exception;

import com.happiest.bookingservice.constant.PredefinedConstants;
import com.happiest.bookingservice.utility.RBundle;

public class SlotNotFoundException extends RuntimeException {
    public SlotNotFoundException() {
        super(RBundle.getKey(PredefinedConstants.SLOT_NOT_FOUND));
    }
    public SlotNotFoundException(String message) {
        super(message);
    }
}
