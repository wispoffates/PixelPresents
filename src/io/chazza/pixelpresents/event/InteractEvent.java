package io.chazza.pixelpresents.event;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.api.Present;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Created by Chazmondo
 */
public class InteractEvent implements Listener {

    private final PixelPresents core;
    public InteractEvent(PixelPresents core){
        this.core = core;
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if (!Bukkit.getVersion().contains("1.8")) {
            if(e.getHand() == EquipmentSlot.OFF_HAND){
                return;
            }
        }

        Present present = core.getPresentManager().get(b.getLocation());
        if(present != null){
            e.setCancelled(true);
            core.getPresentManager().dealActions(p, present);
        }
    }
}
