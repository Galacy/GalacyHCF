package galacy.galacyhcf.managers;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import galacy.galacyhcf.GalacyHCF;

public class BorderFace {
    public Vector3 start, end;

    public boolean removed = true;

    public BorderFace(Vector3 start, Vector3 end) {
        this.start = start;
        this.end = end;
    }

    public void build(Player player) {
        if (removed) {
            player.getServer().getScheduler().scheduleTask(GalacyHCF.instance, () -> {
                int x1 = Math.min((int) start.x, (int) end.x);
                int z1 = Math.min((int) start.z, (int) end.z);
                int x2 = Math.max((int) start.x, (int) end.x);
                int z2 = Math.max((int) start.z, (int) end.z);

                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        for (int y = (int) start.y; y < ((int) start.y + 3); y++) {
                            UpdateBlockPacket packet = new UpdateBlockPacket();
                            packet.x = x;
                            packet.y = y;
                            packet.z = z;
                            packet.blockRuntimeId = 31;
                            player.dataPacket(packet);
                        }
                    }
                }
            }, true);
            removed = false;
        }
    }

    public void remove(Player player) {
        if (!removed) {
            player.getServer().getScheduler().scheduleTask(GalacyHCF.instance, () -> {
                int x1 = Math.min((int) start.x, (int) end.x);
                int z1 = Math.min((int) start.z, (int) end.z);
                int x2 = Math.max((int) start.x, (int) end.x);
                int z2 = Math.max((int) start.z, (int) end.z);

                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        for (int y = (int) start.y; y < ((int) start.y + 3); y++) {
                            UpdateBlockPacket packet = new UpdateBlockPacket();
                            packet.x = x;
                            packet.y = y;
                            packet.z = z;
                            packet.blockRuntimeId = 0;
                            player.dataPacket(packet);
                        }
                    }
                }

            }, true);
            removed = true;
        }
    }

    public boolean equals(BorderFace face) {
        return face.start.x == start.x && face.start.z == start.z && face.end.x == end.x && face.end.z == end.z;
    }
}
