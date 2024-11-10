package com.happiest.servicecenter.exception;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.utility.RBundle;

public class RoleCannotBeChangedException extends RuntimeException {
  public RoleCannotBeChangedException(){
    super(RBundle.getKey(PredefinedConstants.RCBC));
  }
    public RoleCannotBeChangedException(String message) {
        super(message);
    }
}
