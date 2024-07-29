package org.sohan.BookManagementSystem.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, "No_Code" ,HttpStatus.NOT_IMPLEMENTED),

    Account_DISABLED(302,"User account is DISABLED",HttpStatus.FORBIDDEN),
    INCORRECT_CURRENT_PASSWORD(401,"Incorrect password",HttpStatus.UNAUTHORIZED),
    NEW_PASSWORD_DOES_NOT_MATCH(402,"Password does not match",HttpStatus.BAD_REQUEST),
    BAD_CREDIENTIALS(403,"UserName and Password Not Matched",HttpStatus.FORBIDDEN),
    Account_LOCKED(302,"User account is LOCKED",HttpStatus.FORBIDDEN),
    ;

    private final int code;

    private final String description;

    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
