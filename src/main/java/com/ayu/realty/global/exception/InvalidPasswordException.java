package com.ayu.realty.global.exception;

import com.ayu.realty.global.response.ErrorType.ErrorCode;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException(String message) {
        super(ErrorCode.INVALID_PASSWORD);
    }
}
