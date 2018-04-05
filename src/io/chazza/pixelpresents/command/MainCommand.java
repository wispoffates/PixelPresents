package io.chazza.pixelpresents.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.util.ColorUtil;
import org.bukkit.command.CommandSender;

/**
 * Created by Chazmondo
 */

public class MainCommand extends BaseCommand {

    private final PixelPresents core;
    public MainCommand(PixelPresents core){
        this.core = core;
        core.getCmdManager().registerCommand(this, true);
    }

    @CommandAlias("%command")
    public void onCommand(CommandSender cs) {
        if(cs.hasPermission("pixelpresents.admin")) {
            for (String msg : core.getConfig().getStringList("help")) {
                cs.sendMessage(ColorUtil.translate(msg).replace("%version%", core.getDescription().getVersion()));
            }
        } else {
            cs.sendMessage(core.getMsgManager().getMessage("permission"));
        }
    }
}
