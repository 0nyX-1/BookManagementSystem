//package org.sohan.springsecurity02.handler;
//
//import jakarta.mail.MessagingException;
//import org.sohan.springsecurity02.Exception.OperationNotPermittedOperation;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.client.HttpClientErrorException;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(LockedException.class)
//    public ResponseEntity<ExceptionResponse> handleException(LockedException lockedException){
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessErrorCode(
//                                        BusinessErrorCodes.Account_LOCKED.getCode()
//                                )
//                                .businessExceptionDescription(
//                                        BusinessErrorCodes.Account_LOCKED.getDescription()
//                                )
//                                .error(lockedException.getMessage())
//                                .build()
//                );
//    }
//
//    @ExceptionHandler(DisabledException.class)
//    public ResponseEntity<ExceptionResponse> handleException(DisabledException disabledException){
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessErrorCode( BusinessErrorCodes.Account_DISABLED.getCode()
//                                )
//                                .businessExceptionDescription(
//                                        BusinessErrorCodes.Account_DISABLED.getDescription()
//                                )
//                                .error(disabledException.getMessage())
//                                .build()
//                );
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException badCredentialsException){
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessErrorCode( BusinessErrorCodes.BAD_CREDIENTIALS.getCode()
//                                )
//                                .businessExceptionDescription(
//                                        BusinessErrorCodes.BAD_CREDIENTIALS.getDescription()
//                                )
//                                .error(badCredentialsException.getMessage())
//                                .build()
//                );
//    }
//
//    @ExceptionHandler(MessagingException.class)
//    public ResponseEntity<ExceptionResponse> handleException(MessagingException messagingException){
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(
//                        ExceptionResponse.builder()
//                                .error(messagingException.getMessage())
//                                .build()
//                );
//    }
//
//    // Thrown by @Valid annotation
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException methodArgumentNotValidException){
//        Set<String> set = new HashSet<>();
//        methodArgumentNotValidException.getBindingResult()
//                .getAllErrors()
//                .forEach(error->{
//                    String defaultMessage = error.getDefaultMessage();
//                    set.add(defaultMessage);
//                });
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(
//                        ExceptionResponse.builder()
//                                .validationError(set)
//                                .build()
//                );
//    }
//
//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<ExceptionResponse> handleException(Exception exception){
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessExceptionDescription(exception.getLocalizedMessage())
//                                .error(exception.getMessage())
//                                .build()
//                );
//    }
//
//    @ExceptionHandler(OperationNotPermittedOperation.class)
//    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedOperation operationNotPermittedOperation){
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(
//                        ExceptionResponse.builder()
//                                .error(operationNotPermittedOperation.getMessage())
//                                .build()
//                );
//    }
//}
//
