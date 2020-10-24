package io.chazza.pixelpresents.manager;

import io.chazza.pixelpresents.PixelPresents;
import io.chazza.pixelpresents.api.ActionType;
import io.chazza.pixelpresents.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chazmondo
 */
public class ActionManager {

    private final Map<String, ActionType> actionTypes;
    private final PixelPresents core;

    private String nmsVer;
    private boolean useOldMethods = false;

    public ActionManager(){
        this.actionTypes = new HashMap<>();
        this.core = (PixelPresents) JavaPlugin.getProvidingPlugin(PixelPresents.class);

        // Action Bar
        nmsVer = Bukkit.getServer().getClass().getPackage().getName();
        nmsVer = nmsVer.substring(nmsVer.lastIndexOf(".") + 1);

        if (nmsVer.equalsIgnoreCase("v1_8_R1") || nmsVer.startsWith("v1_7_")) {
            useOldMethods = true;
        }
    }

    private ActionType getActionType(String prefix){
        for(Map.Entry<String, ActionType> actions : actionTypes.entrySet()){
            if(actions.getKey().equalsIgnoreCase(prefix))
                return actions.getValue();
        }
        return null;
    }

    public void addAction(String prefix, ActionType action){
        actionTypes.put(prefix, action);
    }

    public void removeAction(String prefix){
        actionTypes.remove(prefix);
    }

    public void execute(String reward, Player p){
        ActionType actionType = getActionType(reward.contains(" ") ? reward.split(" ", 2)[0] : reward);
        String rewardStr = reward.contains(" ") ? reward.split(" ", 2)[1] : "";

        UserManager um = new UserManager(p.getUniqueId());

        int total = core.getPresents().size();
        int found = um.getConfig().getStringList("found").size();
        int left = total - found;

        rewardStr = rewardStr.replace("%player%", p.getName())
            .replace("%found%", ""+found)
        .replace("%total%", ""+total)
        .replace("%left%", ""+left);

        rewardStr = ColorUtil.translate(rewardStr);

        if(actionType == null) return;

        switch(actionType){
            case CONSOLE_COMMAND:
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), rewardStr);
                break;

            case PLAYER_COMMAND:
                p.performCommand(rewardStr);
                break;

            case MESSAGE:
                p.sendMessage(rewardStr);
                break;

            case BROADCAST:
                Bukkit.broadcastMessage(rewardStr);
                break;

            case TITLE:
                String title = rewardStr.contains(";") ? rewardStr.split(";", 2)[0] : rewardStr;
                String subTitle = rewardStr.contains(";") ? rewardStr.split(";", 2)[1] : "";
                p.sendTitle(title, subTitle);
                break;

            case ACTION_BAR:
                String actionBar = rewardStr.contains(";") ? rewardStr.split(";", 2)[0] : rewardStr;
                Integer duration = rewardStr.contains(";") ? Integer.valueOf(rewardStr.split(";", 2)[1])*20 : 30;
                sendActionBar(p, actionBar, duration);
                break;

            case SOUND:
                p.playSound(p.getLocation(), Sound.valueOf(rewardStr), 3, 3);
                break;
        }
    }


    // Action Bar
    private void sendActionBar(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        if (nmsVer.startsWith("v1_11_") || nmsVer.startsWith("v1_10_") || nmsVer.startsWith("v1_9_") || nmsVer.startsWith("v1_8_")) {
            sendActionBarPre112(player, message);
        } else {
            sendActionBarPost112(player, message);
        }
    }

    private void sendActionBarPost112(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVer + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsVer + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsVer + ".Packet");
            Class<?> c2 = Class.forName("net.minecraft.server." + nmsVer + ".ChatComponentText");
            Class<?> c3 = Class.forName("net.minecraft.server." + nmsVer + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsVer + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendActionBarPre112(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVer + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsVer + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsVer + ".Packet");
            if (useOldMethods) {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsVer + ".ChatSerializer");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsVer + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsVer + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsVer + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            }
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendActionBar(final Player player, final String message, int duration) {
        sendActionBar(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, "");
                }
            }.runTaskLater(core, duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, message);
                }
            }.runTaskLater(core, (long) duration);
        }
    }

    public void sendActionBarToAllPlayers(String message) {
        sendActionBarToAllPlayers(message, -1);
    }

    private void sendActionBarToAllPlayers(String message, int duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionBar(p, message, duration);
        }
    }
}
