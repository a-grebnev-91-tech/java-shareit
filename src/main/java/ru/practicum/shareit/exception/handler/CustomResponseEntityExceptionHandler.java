package ru.practicum.shareit.exception.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String PATH = "path";

    @ExceptionHandler(value = ConflictEmailException.class)
    protected ResponseEntity<Object> handleConflictEmail(ConflictEmailException ex, WebRequest request) {
        log.warn("Email conflict: {}", ex.getMessage());
        return getResponseEntity(ex, request, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(value = ForbiddenOperationException.class)
    protected ResponseEntity<Object> handleForbiddenOperation(ForbiddenOperationException ex, WebRequest request) {
        log.warn("Forbidden operation error: {}", ex.getMessage());
        return getResponseEntity(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotAvailableException.class)
    protected ResponseEntity<Object> handleItemNotAvailable(NotAvailableException ex, WebRequest request) {
        log.warn("Not available error: {}", ex.getMessage());
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeader(MissingRequestHeaderException ex, WebRequest request) {
        log.warn("Missing request header error: {}", ex.getMessage());
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
        log.warn("Not found error: {}", ex.getMessage());
        return getResponseEntity(ex, request, HttpStatus.NOT_FOUND);
    }

     @ExceptionHandler(value = BookingStateIsNotSupportedException.class)
     protected ResponseEntity<Object> handleBookingStateIsNotSupported(
             BookingStateIsNotSupportedException ex, WebRequest request
     ) {
        log.warn("Booking state not supported error: {}", ex.getMessage());
        //TODO поправить после завершения проекта (тесты постмана требуют такой ответ)
        Map<String, Object> body = new HashMap<>(1);
        body.put("error", ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        log.warn("Not Valid. Massege: {}", ex.getMessage());
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorString)
                .collect(Collectors.toList());
        Map<String, Object> body = getGeneralErrorBody(status, request, errors.toString());
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = PatchException.class)
    protected ResponseEntity<Object> handlePatchException(PatchException ex, WebRequest request) {
        log.warn("Patch error: {}", ex.getMessage());
        return getResponseEntity(ex, request, HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> getGeneralErrorBody(HttpStatus status, WebRequest request, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, status.value());
        body.put(ERROR, message);
        body.put(PATH, getRequestURI(request));
        return body;
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + " : " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

    private ResponseEntity<Object> getResponseEntity(Exception ex, WebRequest request, HttpStatus status) {
        Map<String, Object> body = getGeneralErrorBody(status, request, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest requestHttp = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", requestHttp.getMethod(), requestHttp.getRequestURI());
        } else {
            return "";
        }
    }
}
