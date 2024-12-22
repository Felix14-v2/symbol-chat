package net.replaceitem.symbolchat.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "CanBeFinal"})
@Config(name = "symbol-chat")
public class ClothConfig implements ConfigData {
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int hud_color = 0x80000000;

    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int button_color = 0xA0000000;

    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int button_active_color = 0xA0202020;

    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int favorite_color = 0xFFFFFF00;
    
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int button_text_color = 0xFFA0A0A0;
    
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public int button_text_hover_color = 0xFFFFFFFF;
    
    public boolean hide_font_button = false;

    @ConfigEntry.Gui.Tooltip
    public boolean hide_settings_button = false;

    @ConfigEntry.Gui.Tooltip
    public boolean hide_table_button = false;

    @ConfigEntry.BoundedDiscrete(min = 100, max = 500)
    public int symbol_panel_height = 200;
    
    @ConfigEntry.BoundedDiscrete(min = 0, max = 50)
    public int max_symbol_suggestions = 5;

    @ConfigEntry.Gui.Tooltip(count = 3)
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ConfigProvider.SymbolTooltipMode symbol_tooltip_mode = ConfigProvider.SymbolTooltipMode.DELAYED;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ConfigProvider.HudSide hud_position = ConfigProvider.HudSide.RIGHT;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ConfigProvider.HudCorner symbol_button_position = ConfigProvider.HudCorner.BOTTOM_RIGHT;
    
    public boolean keep_panel_open = false;

    @ConfigEntry.Gui.Tooltip
    public String chat_suggestion_regex = "^(/(msg|tell|w|say|me|teammsg|tm) |[^/]).*";

    @ConfigEntry.Gui.Tooltip
    public String custom_symbols = "";

    public List<String> custom_kaomojis = new ArrayList<>();
}
