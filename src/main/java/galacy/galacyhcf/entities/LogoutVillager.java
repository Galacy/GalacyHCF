package galacy.galacyhcf.entities;

import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class LogoutVillager extends EntityVillager {
    public PlayerInventory inventory;
    public int factionId;

    public LogoutVillager(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public String getName() {
        return "LogoutVillager";
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.age > 1200) {
            close();
            return true;
        }

        return super.onUpdate(currentTick);
    }
}
