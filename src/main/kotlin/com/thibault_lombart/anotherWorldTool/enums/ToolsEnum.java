package com.thibault_lombart.anotherWorldTool.enums;

public enum ToolsEnum {

    AXE,
    SHOVEL,
    PICKAXE,
    SHEARS,
    HOE;

    public static ToolsEnum fromId(String id) {
        if (id == null) return null;
        for (ToolsEnum t : values()) if (t.name().equalsIgnoreCase(id)) return t;
        return null;
    }
}
