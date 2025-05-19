package com.chess.uci;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.function.Consumer;

public final class ButtonOption extends SealedOption implements MouseListener {
    public final String name;
    public final Consumer<ButtonOption> onClickListener;

    public ButtonOption(String name) {
        this.name = name;
        // Default onClickListener.
        this.onClickListener = buttonOption -> {};
    }

    @SuppressWarnings("unused")
    public ButtonOption(String name, Consumer<ButtonOption> onClickListener) {
        this.name = name;
        this.onClickListener = onClickListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        onClickListener.accept(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ButtonOption that)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "option name %s type button".formatted(name);
    }
}
