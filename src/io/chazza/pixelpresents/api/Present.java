package io.chazza.pixelpresents.api;

import io.chazza.pixelpresents.PixelPresents;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

/**
 * Created by Chazmondo
 */
public class Present {

    private final UUID uuid;
    private List<String> rewards;
    private final Location location;
    private final PixelPresents core;

    public Present(UUID presentUuid, Location presentLocation){
        this.uuid = presentUuid;
        this.location = presentLocation;
        this.core = (PixelPresents) JavaPlugin.getProvidingPlugin(PixelPresents.class);
    }

    public Present withRewards(List<String> presentRewards){
        this.rewards = presentRewards;
        return this;
    }

    public void build(){
        core.getPresents().add(this);
    }

    public void delete(){
        core.getPresents().remove(this);
        core.getConfig().set("present."+uuid, null);
        core.saveConfig();
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public Location getLocation() {
        return location;
    }
}
