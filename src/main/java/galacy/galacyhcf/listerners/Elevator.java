package galacy.galacyhcf.listerners;

import cn.nukkit.block.BlockSignPost;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.utils.Utils;

public class Elevator implements Listener {

    @EventHandler
    public void on(SignChangeEvent event) {
        if (event.getLine(0).toLowerCase().equals("[elevator]")) {
            switch (event.getLine(1).toLowerCase()) {
                case "up":
                    event.setLine(0, TextFormat.GREEN + "[Elevator]");
                    event.setLine(1, "UP");
                    event.setLine(2, "");
                    event.setLine(3, "");

                    break;

                case "down":
                    event.setLine(0, TextFormat.GREEN + "[Elevator]");
                    event.setLine(1, "DOWN");
                    event.setLine(2, "");
                    event.setLine(3, "");

                    break;
            }
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getBlock() instanceof BlockSignPost || event.getBlock() instanceof BlockWallSign) {
            BlockEntity sign = event.getBlock().getLevel().getBlockEntity(event.getTouchVector());
            if (sign instanceof BlockEntitySign) {
                if (((BlockEntitySign) sign).getText()[0] != null && ((BlockEntitySign) sign).getText()[0].equals(TextFormat.GREEN + "[Elevator]")) {
                    if (((BlockEntitySign) sign).getText()[1] != null) {
                        switch (((BlockEntitySign) sign).getText()[1]) {
                            case "UP":
                                int y = findHighestBlockFrom(event.getTouchVector(), sign.getLevel());
                                event.getPlayer().teleport(new Vector3(sign.x, y, sign.z));

                                break;

                            case "DOWN":
                                int y2 = findLowestBlockFrom(event.getTouchVector(), sign.getLevel());
                                if (y2 <= 0) {
                                    event.getPlayer().sendMessage(Utils.prefix + TextFormat.YELLOW + "Couldn't teleport you, there's no space down there.");
                                } else event.getPlayer().teleport(new Vector3(sign.x, y2, sign.z));

                                break;
                        }
                    }
                }
            }
        }
    }

    private int findHighestBlockFrom(Vector3 pos, Level level) {
        for (int y = (int) pos.y; y < 255; y++) {
            if (level.getBlockIdAt((int) pos.x, y, (int) pos.z) == 0) return y;
        }

        return 256;
    }

    private int findLowestBlockFrom(Vector3 pos, Level level) {
        for (int y = (int) pos.y; y > 0; y--) {
            if (level.getBlockIdAt((int) pos.x, y, (int) pos.z) == 0) return (y - 1);
        }

        return 0;
    }
}

/*
19:20:17 [FATAL] Could not pass event "cn.nukkit.event.player.PlayerInteractEvent" to "GalacyHCF v0.2": null on galacy.galacyhcf.listerners.Elevator
        19:20:17 [ERROR] Throwing
        cn.nukkit.utils.EventException: null
        at cn.nukkit.plugin.MethodEventExecutor.execute(MethodEventExecutor.java:34) ~[nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:56) ~[nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.plugin.PluginManager.callEvent(PluginManager.java:546) ~[nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.level.Level.useItemOn(Level.java:2052) ~[nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.level.Level.useItemOn(Level.java:2020) ~[nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Player.handleDataPacket(Player.java:3086) ~[nukkit-1.0-SNAPSHOT.jar:?]
        at java.util.ArrayList.forEach(ArrayList.java:1540) [?:?]
        at cn.nukkit.network.Network.processPackets(Network.java:265) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.network.Network.processBatch(Network.java:248) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Player.handleDataPacket(Player.java:2103) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.network.RakNetInterface.handleEncapsulated(RakNetInterface.java:156) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.raknet.server.ServerHandler.handlePacket(ServerHandler.java:132) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.network.RakNetInterface.process(RakNetInterface.java:65) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.network.Network.processInterfaces(Network.java:149) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Server.tick(Server.java:1137) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Server.tickProcessor(Server.java:916) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Server.start(Server.java:893) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Server.<init>(Server.java:578) [nukkit-1.0-SNAPSHOT.jar:?]
        at cn.nukkit.Nukkit.main(Nukkit.java:112) [nukkit-1.0-SNAPSHOT.jar:?]
        Caused by: java.lang.NullPointerException
        at galacy.galacyhcf.listerners.Elevator.on(Elevator.java:46) ~[?:?]
        at jdk.internal.reflect.GeneratedMethodAccessor16.invoke(Unknown Source) ~[?:?]
        at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:?]
        at java.lang.reflect.Method.invoke(Method.java:566) ~[?:?]
        at cn.nukkit.plugin.MethodEventExecutor.execute(MethodEventExecutor.java:29) ~[nukkit-1.0-SNAPSHOT.jar:?]
*/