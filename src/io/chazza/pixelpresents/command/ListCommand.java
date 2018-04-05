package io.chazza.pixelpresents.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.api.Present;
import io.chazza.pixelpresents.util.ColorUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Chazmondo
 */
@CommandAlias("%command")
public class ListCommand extends BaseCommand {

    private final PixelPresents core;
    public ListCommand(PixelPresents core){
        this.core = core;
        core.getCmdManager().registerCommand(this, true);
    }

    @Subcommand("list")
    public void onCommand(Player p){
        if(p.hasPermission("pixelpresents.admin")) {
            p.sendMessage("");
            p.sendMessage("§a§lListing Presents§r §7("+core.getPresents().size()+"§7)");
            p.sendMessage("");
            int id = 1;
            String format;
            Location loc;
            for(Present present : core.getPresents()){
                loc = present.getLocation();

                format = " &8• &fPresent #" + id + " &8- &cX: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ();

                TextComponent message = new TextComponent(
                    TextComponent.fromLegacyText(ColorUtil.translate(format)));

                message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/present tp "+id));
                message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to teleport!").create() ) );
                p.spigot().sendMessage( message );
                id++;
            }
            p.sendMessage("");
        } else {
            p.sendMessage(core.getMsgManager().getMessage("permission"));
        }
    }
}
