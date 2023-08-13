package ca.modmonster.spells.item.spell;

public enum ClickType {
    CLICK("Click"),
    LEFT_CLICK("Left Click"),
    RIGHT_CLICK("Right Click");

    public final String name;

    ClickType(String name) {
        this.name = name;
    }
}
