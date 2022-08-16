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
import ru.practicum.shareit.exception.ConflictEmailException;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
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

    private static final String REASONS = "reasons";

    @ExceptionHandler(value = ConflictEmailException.class)
    protected ResponseEntity<Object> handleConflictEmail(ConflictEmailException ex, WebRequest request) {
        log.warn("Email conflict: {}", ex.getMessage());
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.CONFLICT, request);
        body.put(REASONS, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = ForbiddenOperationException.class)
    protected ResponseEntity<Object> handleForbiddenOperation(ForbiddenOperationException ex, WebRequest request) {
        log.warn("Forbidden operation error: {}", ex.getMessage());
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.FORBIDDEN, request);
        body.put(REASONS, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeader(MissingRequestHeaderException ex, WebRequest request) {
        log.warn("Missing request header error: {}", ex.getMessage());
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.BAD_REQUEST, request);
        body.put(REASONS, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
        log.warn("Not found error: {}", ex.getMessage());
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.NOT_FOUND, request);
        body.put(REASONS, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
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
        Map<String, Object> body = getGeneralErrorBody(status, request);
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::getErrorString)
                .collect(Collectors.toList());
        body.put(REASONS, errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = PatchException.class)
    protected ResponseEntity<Object> handlePatchException(PatchException ex, WebRequest request) {
        log.warn("Patch error: {}", ex.getMessage());
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.NOT_FOUND, request);
        body.put(REASONS, ex.getMessage());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private Map<String, Object> getGeneralErrorBody(HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, OffsetDateTime.now());
        body.put(STATUS, status.value());
        body.put(ERROR, status.getReasonPhrase());
        body.put(PATH, getRequestURI(request));
        return body;
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + " : " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
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
