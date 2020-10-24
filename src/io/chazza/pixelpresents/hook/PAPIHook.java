package io.chazza.pixelpresents.hook;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.manager.UserManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;

/**
 * Created by Chazmondo
 */
public class PAPIHook extends PlaceholderExpansion {

    private final PixelPresents core;
    public PAPIHook(PixelPresents core) {
        this.core = core;
    }

    @Override
    public String onRequest(OfflinePlayer p, String identifier) {
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

    @Override
    public String getAuthor() {
        return "wispofffates";
    }

    @Override
    public String getIdentifier() {
        return "pixelpresents";
    }

    @Override
    public String getVersion() {
        return "1.0.1";
    }
}
