package ru.practicum.shareit.exception;

import javax.validation.ConstraintDeclarationException;

public class ConflictEmailException extends ConstraintDeclarationException {
    public ConflictEmailException(String message) {
        super(message);
    }
}
