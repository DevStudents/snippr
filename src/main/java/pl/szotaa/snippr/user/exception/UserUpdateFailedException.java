package pl.szotaa.snippr.user.exception;

import pl.szotaa.snippr.common.AbstractConstraintViolationException;
import pl.szotaa.snippr.common.FieldError;

import java.util.Set;

public class UserUpdateFailedException extends AbstractConstraintViolationException {

    public UserUpdateFailedException(long id, Set<FieldError> fieldErrors) {
        super("Failed to update user with id: " + id, fieldErrors);
    }
}
