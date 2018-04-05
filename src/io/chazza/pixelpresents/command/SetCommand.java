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
public class SetCommand extends BaseCommand {

    private final PixelPresents core;
    public SetCommand(PixelPresents core){
        this.core = core;
        core.getCmdManager().registerCommand(this, true);
    }

    @Subcommand("set")
    public void onCommand(Player p){
        if(p.hasPermission("pixelpresents.admin")) {
            p.sendMessage(core.getMsgManager().getMessage("create"));
            core.getPlayersEditing().put(p.getUniqueId(), System.currentTimeMillis());
        } else {
            p.sendMessage(core.getMsgManager().getMessage("permission"));
        }
    }
}
