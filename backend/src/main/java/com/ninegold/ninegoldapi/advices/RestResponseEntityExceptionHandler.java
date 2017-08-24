package com.ninegold.ninegoldapi.advices;

import com.google.common.base.Throwables;
import com.ninegold.ninegoldapi.controllers.UserController;
import com.ninegold.ninegoldapi.entities.ExceptionInfoEntity;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = { NineGoldException.class })
  protected ResponseEntity<Object> handleConflict(NineGoldException ex,
                                                  ServletWebRequest request,
                                                  HandlerMethod handlerMethod) {
    String errorMessage = null;
    if (handlerMethod.getBean() instanceof UserController &&
      handlerMethod.getMethod().getName().contentEquals("create")) {
      if (Throwables.getCausalChain(ex).stream().anyMatch(
        (throwable -> throwable instanceof ConstraintViolationException))) {
        errorMessage = "Error: The E-mail has been registered";
      }
    }

    ExceptionInfoEntity exceptionInfo = new ExceptionInfoEntity();
    exceptionInfo.setMessage(errorMessage);
    return handleExceptionInternal(ex, exceptionInfo,
      new HttpHeaders(), HttpStatus.CONFLICT, request);
  }
}

