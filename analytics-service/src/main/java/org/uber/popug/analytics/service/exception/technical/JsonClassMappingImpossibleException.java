package org.uber.popug.analytics.service.exception.technical;

import com.fasterxml.jackson.core.TreeNode;
import lombok.Getter;

@Getter
public class JsonClassMappingImpossibleException extends RuntimeException {

    private static final String JSON_MAPPING_TO_TYPE_IMPOSSIBLE_MESSAGE_TEMPLATE
            = "Could not deserialize JSON: %s to class %s.";

    private final Class<?> failedClass;
    private final String jsonString;

    public JsonClassMappingImpossibleException(Class<?> failedClass, TreeNode jsonTreeRoot, Exception cause) {
        super(JSON_MAPPING_TO_TYPE_IMPOSSIBLE_MESSAGE_TEMPLATE.formatted(
                        jsonTreeRoot.toString(),
                        failedClass.getTypeName()
                ),
                cause
        );

        this.failedClass = failedClass;
        this.jsonString = jsonTreeRoot.toString();
    }

}
