package com.chess.uci;

import java.util.Objects;

public final class SpinOption extends SealedOption {
    public final String name;
    public final int minimum;
    public final int maximum;
    public final int defaultValue;

    public SpinOption(String name, int minimum, int maximum, int defaultValue) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpinOption that)) return false;
        return minimum == that.minimum && maximum == that.maximum && defaultValue == that.defaultValue && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, minimum, maximum, defaultValue);
    }

    @Override
    public String toString() {
        return "option name %s type spin default %d min %d max %d".formatted(name, defaultValue, minimum, maximum);
    }
}
