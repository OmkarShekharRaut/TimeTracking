package com.timetracking.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class MutableTimeProvider implements TimeProvider {
    private LocalDateTime current;

    public MutableTimeProvider(LocalDateTime initialTime) {
        this.current = Objects.requireNonNull(initialTime, "initialTime");
    }

    @Override
    public LocalDateTime now() {
        return current;
    }

    public void set(LocalDateTime newTime) {
        this.current = Objects.requireNonNull(newTime, "newTime");
    }

    public void advance(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        this.current = this.current.plus(duration);
    }
}

