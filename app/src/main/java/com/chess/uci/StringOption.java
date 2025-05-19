package com.chess.uci;

import java.util.Objects;

@SuppressWarnings("unused")
public final class StringOption extends SealedOption {
    public final String name;
    public final String defaultValue;
    private String currentValue;

    public StringOption(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        currentValue = defaultValue;
    }

    public String getValue() {
        return currentValue;
    }

    public void setValue(String value) {
        currentValue = value;
    }

    public void resetValue() {
        currentValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringOption that)) return false;
        return name.equals(that.name) && defaultValue.equals(that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, defaultValue);
    }

    @Override
    public String toString() {
        return "option name %s type string default %s".formatted(name, defaultValue).stripTrailing();
    }
}
