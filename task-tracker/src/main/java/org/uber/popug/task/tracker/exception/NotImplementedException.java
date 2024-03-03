package org.uber.popug.task.tracker.exception;

import java.io.Serial;

public class NotImplementedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotImplementedException(Class<?> clazz, String methodName) {
        super("%s.%s is not implemented.".formatted(clazz.getCanonicalName(), methodName));
    }
}
