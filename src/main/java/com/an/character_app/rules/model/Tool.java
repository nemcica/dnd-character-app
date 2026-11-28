package com.an.character_app.rules.model;

import lombok.Getter;

@Getter
public enum Tool {
    ALCHEMISTS_SUPPLIES(ToolCategory.ARTISAN_TOOL),
    BREWERS_SUPPLIES(ToolCategory.ARTISAN_TOOL),
    CALLIGRAPHERS_SUPPLIES(ToolCategory.ARTISAN_TOOL),
    CARPENTERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    CARTOGRAPHERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    COBBLERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    COOKS_UTENSILS(ToolCategory.ARTISAN_TOOL),
    GLASSBLOWERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    JEWELERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    LEATHERWORKERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    MASONS_TOOLS(ToolCategory.ARTISAN_TOOL),
    PAINTERS_SUPPLIES(ToolCategory.ARTISAN_TOOL),
    POTTERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    SMITHS_TOOLS(ToolCategory.ARTISAN_TOOL),
    TINKERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    WEAVERS_TOOLS(ToolCategory.ARTISAN_TOOL),
    WOODCARVERS_TOOLS(ToolCategory.ARTISAN_TOOL),

    DISGUISE_KIT(ToolCategory.OTHER_TOOL),
    FORGERY_KIT(ToolCategory.OTHER_TOOL),
    HERBALISM_KIT(ToolCategory.OTHER_TOOL),
    NAVIGATORS_TOOLS(ToolCategory.OTHER_TOOL),
    POISONERS_KIT(ToolCategory.OTHER_TOOL),
    THIEVES_TOOLS(ToolCategory.OTHER_TOOL),

    DICE(ToolCategory.GAMING_SET),
    DRAGONCHESS(ToolCategory.GAMING_SET),
    PLAYING_CARDS(ToolCategory.GAMING_SET),
    THREE_DRAGON_ANTE(ToolCategory.GAMING_SET),

    BAGPIPES(ToolCategory.MUSICAL_INSTRUMENT),
    DRUM(ToolCategory.MUSICAL_INSTRUMENT),
    DULCIMER(ToolCategory.MUSICAL_INSTRUMENT),
    FLUTE(ToolCategory.MUSICAL_INSTRUMENT),
    HORN(ToolCategory.MUSICAL_INSTRUMENT),
    LUTE(ToolCategory.MUSICAL_INSTRUMENT),
    LYRE(ToolCategory.MUSICAL_INSTRUMENT),
    PAN_FLUTE(ToolCategory.MUSICAL_INSTRUMENT),
    SHAWM(ToolCategory.MUSICAL_INSTRUMENT),
    VIOL(ToolCategory.MUSICAL_INSTRUMENT);

    private final ToolCategory toolCategory;

    Tool(ToolCategory toolCategory) {
        this.toolCategory = toolCategory;
    }
}
