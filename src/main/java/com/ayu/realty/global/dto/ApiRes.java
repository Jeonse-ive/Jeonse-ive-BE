package com.ayu.realty.global.dto;


import com.ayu.realty.global.response.ErrorType.ErrorType;
import com.ayu.realty.global.response.SuccessType.DataSuccessCode;
import com.ayu.realty.global.response.SuccessType.UserSuccessCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
@Schema(description = "공통 API 응답")
public class ApiRes<T> {

    @Schema(description = "응답 코드", example = "M001" /* 또는 E001 등 */)
    private final String code;

    @Schema(description = "응답 메시지", example = "회원 정보 조회 성공")
    private final String message;

    @Schema(description = "응답 데이터", nullable = true)
    private final T data;

    // 유저 성공 응답
    public static <T> ApiRes<T> success(UserSuccessCode successCode, T data) {
        return new ApiRes<>(successCode.getCode(), successCode.getMessage(), data);
    }

    public static ApiRes<Void> success(UserSuccessCode successCode) {
        return new ApiRes<>(successCode.getCode(), successCode.getMessage(), null);
    }

    // 데이터 성공 응답
    public static <T> ApiRes<T> success(DataSuccessCode successCode, T data) {
        return new ApiRes<>(successCode.getCode(), successCode.getMessage(), data);
    }
    public static ApiRes<Void> success(DataSuccessCode successCode) {
        return new ApiRes<>(successCode.getCode(), successCode.getMessage(), null);
    }

    // 실패 응답
    public static <T> ApiRes<T> fail(ErrorType errorType) {
        return new ApiRes<>(errorType.getCode(), errorType.getMessage(), null);
    }



    // 생성자
    private ApiRes(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}