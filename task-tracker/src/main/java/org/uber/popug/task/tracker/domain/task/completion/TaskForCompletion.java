package org.uber.popug.task.tracker.domain.task.completion;

public record TaskForCompletion(
        long taskId,
        long assigneeId
) {
}
