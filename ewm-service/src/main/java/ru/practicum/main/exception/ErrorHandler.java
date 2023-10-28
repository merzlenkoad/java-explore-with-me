package ru.practicum.main.exception;

import io.micrometer.core.lang.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiError> handle(Exception ex) throws IOException {
        ApiError apiError = ApiError.builder()
                .errors(Collections.singletonList(error(ex)))
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(ex.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    public @NotNull
    ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField());
        }
        ApiError apiError = ApiError.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .reason("The required object was not found.")
                .message(ex.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        ApiError apiError = ApiError.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .reason("The required object was not found.")
                .message(ex.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    private String error(Exception e) throws IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace();
        String error = sw.toString();
        sw.close();
        pw.close();
        return error;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)   // для всех ситуаций, если искомый объект не найден
    public ApiError handle(final NotFoundException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(ValidationCategoryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)   // Категория существует
    public ApiError handle(final ValidationCategoryException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(DuplicateNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  //если есть дубликат Name.
    public ApiError handleThrowable(final DuplicateNameException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("CONFLICT CONFLICT The name is exists, dublicate!!!!!!!!!!!!!!!!!!!!!!")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  //если есть дубликат Email.
    public ApiError handleThrowable(final DuplicateEmailException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("CONFLICT CONFLICT The email is exists, dublicate!!!!!!!!!!!!!!!!!!!!!!")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(EventDateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowable(final EventDateException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("Incorrectly  time")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(StateArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowable(final StateArgumentException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("CONFLICT CONFLICT The required object was not found.!!!!!!!!!!!!!!!!!!!!!!")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(OverflowLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowable(final OverflowLimitException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("The participant limit has been reached")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(StatusPerticipationRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowable(final StatusPerticipationRequestException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("The status Request NOT PENDING")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(DuplicateParticipationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleThrowable(final DuplicateParticipationException e) throws IOException {
        return ApiError.builder()
                .errors(Collections.singletonList(error(e)))
                .status(HttpStatus.CONFLICT)
                .reason("Request has been")
                .message(e.getLocalizedMessage())
                .timestamp((LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}