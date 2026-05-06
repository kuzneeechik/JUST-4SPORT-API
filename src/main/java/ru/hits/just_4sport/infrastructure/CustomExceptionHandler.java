package ru.hits.just_4sport.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hits.just_4sport.infrastructure.exception.BadRequestException;
import ru.hits.just_4sport.infrastructure.exception.NotFoundException;
import ru.hits.just_4sport.model.api.ExceptionResponseModel;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseModel> catchNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ExceptionResponseModel(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseModel> catchBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(new ExceptionResponseModel(HttpStatus.BAD_REQUEST.value(), exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
