package io.chazza.pixelpresents.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.pixelpresents.PixelPresents;
import org.bukkit.entity.Player;

/**
 * Created by Chazmondo
 */
@CommandAlias("%command")
public class TPCommand extends BaseCommand {

    private final PixelPresents core;
    public TPCommand(PixelPresents core){
        this.core = core;
        core.getCmdManager().registerCommand(this, true);
    }

    @Subcommand("tp")
    public void onCommand(Player p, Integer id){
        if(p.hasPermission("pixelpresents.admin")) {
            p.sendMessage(core.getMsgManager().getMessage("tp").replace("%id%", ""+id));
            p.teleport(core.getPresents().get(id-1).getLocation());
        } else {
            p.sendMessage(core.getMsgManager().getMessage("permission"));
        }
    }
}
