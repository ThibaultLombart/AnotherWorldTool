package com.thibault_lombart.anotherWorldTool.storage;

import com.thibault_lombart.anotherWorldTool.enums.ToolsEnum;
import com.thibault_lombart.anotherWorldTool.tools.*;
import com.thibault_lombart.anotherWorldTool.utils.PlayerUtils;

import java.util.UUID;

public class PlayerInformations {

    private UUID uuid;
    private String displayName;

    private Tool currentTool;

    private AxeTool axeTool;
    private HoeTool hoeTool;
    private PickaxeTool pickaxeTool;
    private ShearsTool shearsTool;
    private ShovelTool shovelTool;

    public PlayerInformations(UUID uuid) {
        this.uuid = uuid;
        this.displayName = PlayerUtils.getPlayerNameFromUUID(uuid);

        this.axeTool = new AxeTool(uuid);
        this.hoeTool = new HoeTool(uuid);
        this.pickaxeTool = new PickaxeTool(uuid);
        this.shearsTool = new ShearsTool(uuid);
        this.shovelTool = new ShovelTool(uuid);

        this.currentTool = hoeTool;
    }

    public PlayerInformations(UUID uuid, AxeTool axeTool, HoeTool hoeTool, PickaxeTool pickaxeTool, ShearsTool shearsTool, ShovelTool shovelTool) {
        this.uuid = uuid;
        this.displayName = PlayerUtils.getPlayerNameFromUUID(uuid);
        this.axeTool = axeTool;
        this.hoeTool = hoeTool;
        this.pickaxeTool = pickaxeTool;
        this.shearsTool = shearsTool;
        this.shovelTool = shovelTool;

        this.currentTool = hoeTool;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AxeTool getAxeTool() {
        return axeTool;
    }

    public HoeTool getHoeTool() {
        return hoeTool;
    }

    public PickaxeTool getPickaxeTool() {
        return pickaxeTool;
    }

    public ShearsTool getShearsTool() {
        return shearsTool;
    }

    public ShovelTool getShovelTool() {
        return shovelTool;
    }

    public Tool getCurrentTool() {
        return currentTool;
    }

    public void changeCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

    public Tool getToolByEnum(ToolsEnum t) {
        if (t == null) return null;
        return switch (t) {
            case AXE -> axeTool;
            case HOE -> hoeTool;
            case PICKAXE -> pickaxeTool;
            case SHEARS -> shearsTool;
            case SHOVEL -> shovelTool;
        };
    }

    public boolean addXp(Tool tool, int xp) {
        return tool.addXP(xp);
    }


}
