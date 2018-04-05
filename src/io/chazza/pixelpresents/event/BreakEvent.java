package io.chazza.pixelpresents.event;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.api.Present;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Chazmondo
 */
public class BreakEvent implements Listener {

    private final PixelPresents core;
    public BreakEvent(PixelPresents core){
        this.core = core;
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();

        Present present = core.getPresentManager().get(b.getLocation());

        if(present != null){
            e.setCancelled(true);
            core.getPresentManager().dealActions(p, present);
            return;
        }

        if(p.hasPermission("pixelpresents.admin") && core.getPlayersEditing().containsKey(p.getUniqueId())){
            e.setCancelled(true);
            core.getPlayersEditing().remove(p.getUniqueId());
            core.getPresentManager().create(b.getLocation());
            p.sendMessage(core.getMsgManager().getMessage("setup"));
        }
    }
}
