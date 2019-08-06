package cn.bluesadi.bluefriends.gui;

import cn.bluesadi.bluefriends.gui.component.BluesComponent;
import org.bukkit.configuration.ConfigurationSection;

@FunctionalInterface
public interface ComponentLoadingFunction {

    BluesComponent load(BluesGui gui, ConfigurationSection section);

}
