package com.chess.uci;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ComboOption extends SealedOption {
    public final String name;
    public final String[] options;
    public final String defaultValue;

    public ComboOption(String name, String[] options, String defaultValue) {
        this.name = name;
        assert Arrays.asList(options).contains(defaultValue);
        this.options = options;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComboOption that)) return false;
        return name.equals(that.name) && Arrays.equals(options, that.options) && defaultValue.equals(that.defaultValue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, defaultValue);
        result = 31 * result + Arrays.hashCode(options);
        return result;
    }

    @Override
    public String toString() {
        Stream<String> vars = Arrays.stream(options).map(op -> "var " + op);
        return "option name %s type combo default %s %s"
            .formatted(name, defaultValue, vars.collect(Collectors.joining(" ")));
    }
}
