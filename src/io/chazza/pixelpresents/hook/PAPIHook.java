package io.chazza.pixelpresents.hook;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.manager.UserManager;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

/**
 * Created by Chazmondo
 */
public class PAPIHook extends EZPlaceholderHook {

    private final PixelPresents core;
    public PAPIHook(PixelPresents core) {
        super(core, "pixelpresents");
        this.core = core;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        UserManager um = new UserManager(p.getUniqueId());

        if(identifier.equals("found")){
            return "" + um.getConfig().getStringList("found").size();
        }

        if(identifier.equals("total")){
            return "" + core.getPresents().size();
        }

        if(identifier.equals("left")){
            return "" + (core.getPresents().size() - um.getConfig().getStringList("found").size());
        }
        return null;
    }
}
