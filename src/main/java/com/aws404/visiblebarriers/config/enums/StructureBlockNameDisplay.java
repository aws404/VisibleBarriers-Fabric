package com.aws404.visiblebarriers.config.enums;

public enum StructureBlockNameDisplay {
    NONE(false, false, false),
    TARGETED_BLOCK(false, true, false),
    BLOCK_CONSTANT(false, false, true),
    ACTION_BAR(true, false, false),
    ACTION_BAR_AND_TARGET(true, true, false),
    ACTION_BAR_AND_CONSTANT(true, false, true);

    public final boolean actionbar;
    public final boolean onTarget;
    public final boolean constant;

    StructureBlockNameDisplay(boolean actionbar, boolean onTarget, boolean constant) {
        this.actionbar = actionbar;
        this.onTarget = onTarget;
        this.constant = constant;
    }
}
