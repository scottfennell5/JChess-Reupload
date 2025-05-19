package com.chess.uci;

import java.util.Objects;

public final class CheckOption extends SealedOption {
    public final String name;
    public final boolean defaultValue;

    public CheckOption(String name, boolean defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckOption that)) return false;
        return defaultValue == that.defaultValue && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, defaultValue);
    }

    @Override
    public String toString() {
        return "option name %s type check default %b".formatted(name, defaultValue);
    }
}
