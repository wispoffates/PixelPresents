package io.chazza.pixelpresents.manager;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.api.Present;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Chazmondo
 */
public class PresentManager {

    private final PixelPresents core;
    public PresentManager(){
        this.core = (PixelPresents) JavaPlugin.getProvidingPlugin(PixelPresents.class);
    }

    public PixelPresents getCore() {
        return core;
    }

    public void create(Location loc){
        Present present = new Present(UUID.randomUUID(), loc)
            .withRewards(Arrays.asList("[Message] &aPresent Found! &f+$100",
                "[Console] eco give %player% 100",
                "[Title] &aPresent Found!;&7Congratulations!",
                "[Actionbar] &aPresent Found!;5"));
        core.getConfig().set("present."+present.getUuid()+".reward", present.getRewards());

        core.getConfig().set("present."+present.getUuid()+".location.x", loc.getBlockX());
        core.getConfig().set("present."+present.getUuid()+".location.y", loc.getBlockY());
        core.getConfig().set("present."+present.getUuid()+".location.z", loc.getBlockZ());
        core.getConfig().set("present."+present.getUuid()+".location.world", loc.getWorld().getName());
        core.saveConfig();
        present.build();
    }

    public void remove(Location loc){
        Present present = get(loc);
        core.getPresents().remove(present);
        core.getConfig().set("present."+present.getUuid(), null);
        core.saveConfig();
    }

    public Present get(Location loc){
        for(Present present : core.getPresents()){
            if(present.getLocation().equals(loc)){
                return present;
            }
        }
        return null;
    }

    public void dealActions(Player player, Present present){
        UserManager um = new UserManager(player.getUniqueId());

        if(player.hasPermission("pixelpresents.admin") && player.isSneaking()){
            player.sendMessage(core.getMsgManager().getMessage("shift-broken"));
            present.delete();
            return;
        }


        if(um.getConfig().getStringList("found").contains(present.getUuid().toString())){
            player.sendMessage(core.getMsgManager().getMessage("already-found"));
        } else {

            List<String> found = um.getConfig().getStringList("found");
            found.add(present.getUuid().toString());
            um.getConfig().set("found", found);
            um.save();

            present.getRewards().forEach(reward -> core.getActionManager().execute(reward, player));

            if(found.size() == core.getPresents().size()){
                core.getConfig().getStringList("complete-reward").forEach(reward -> core.getActionManager().execute(reward, player));
            }
        }
    }
}
