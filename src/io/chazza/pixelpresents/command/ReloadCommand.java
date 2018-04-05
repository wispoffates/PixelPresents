package io.chazza.pixelpresents.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.pixelpresents.PixelPresents;
import org.bukkit.command.CommandSender;

/**
 * Created by Chazmondo
 */
@CommandAlias("%command")
public class ReloadCommand extends BaseCommand {

    private final PixelPresents core;
    public ReloadCommand(PixelPresents core){
        this.core = core;
        core.getCmdManager().registerCommand(this, true);
    }

    @Subcommand("reload")
    public void onCommand(CommandSender cs){
        if(cs.hasPermission("pixelpresents.admin")) {
            core.reloadConfig();


            cs.sendMessage("");
            core.setupPresents();
            cs.sendMessage(core.getMsgManager().getMessage("reloaded"));
        } else {
            cs.sendMessage(core.getMsgManager().getMessage("permission"));
        }
    }
}
