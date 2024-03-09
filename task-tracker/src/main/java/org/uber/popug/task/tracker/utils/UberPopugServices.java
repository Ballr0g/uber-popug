package org.uber.popug.task.tracker.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UberPopugServices {

    TASK_TRACKER("uber-popug.task-tracker");

    private final String name;

}
