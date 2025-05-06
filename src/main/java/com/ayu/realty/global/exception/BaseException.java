package com.ayu.realty.global.exception;

import com.ayu.realty.global.response.ErrorType.ErrorType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorType errorCode;

    public BaseException(ErrorType errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
