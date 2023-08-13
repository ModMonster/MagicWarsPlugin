package ca.modmonster.spells.util;

import net.kyori.adventure.text.TextComponent;

import java.util.List;

public abstract class AnimationHelper {
    final List<TextComponent> lines = getLines();

    protected abstract List<TextComponent> getLines();
    public TextComponent getLine (Integer index) {
        return lines.get(index);
    }
    public Integer getLength() {
        return lines.size();
    }
}