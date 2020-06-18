package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.item.ItemID;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.PluginTask;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.managers.SetsManager;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.models.GPlayer;

import java.util.Objects;

public class BardTask extends PluginTask<GalacyHCF> {
    public BardTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                if (((GPlayer) player).bardCooldown > 0) ((GPlayer) player).bardCooldown--;
                if (((GPlayer) player).rogueCooldown > 0) ((GPlayer) player).rogueCooldown--;
                ((GPlayer) player).applySet(false);
                if (((GPlayer) player).set == SetsManager.Sets.Bard) {
                    if (((GPlayer) player).bardEnergy < 100) ((GPlayer) player).bardEnergy++;
                    if (((GPlayer) player).factionId != 0) {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                        switch (player.getInventory().getItemInHand().getId()) {
                            case ItemID.SUGAR:
                                for (GPlayer member : faction.onlineMembers()) {
                                    if (Objects.equals(member.xuid, ((GPlayer) player).xuid)) continue;
                                    if (player.distance(member) > 10) continue;
                                    member.addEffect(Effect.getEffect(Effect.SPEED).setAmplifier(2).setDuration(100));
                                }
                                break;
                            case ItemID.FEATHER:
                                for (GPlayer member : faction.onlineMembers()) {
                                    if (Objects.equals(member.xuid, ((GPlayer) player).xuid)) continue;
                                    if (player.distance(member) > 10) continue;
                                    member.addEffect(Effect.getEffect(Effect.JUMP).setAmplifier(2).setDuration(100));
                                }
                                break;
                            case ItemID.IRON_INGOT:
                                for (GPlayer member : faction.onlineMembers()) {
                                    if (Objects.equals(member.xuid, ((GPlayer) player).xuid)) continue;
                                    if (player.distance(member) > 10) continue;
                                    member.addEffect(Effect.getEffect(Effect.DAMAGE_RESISTANCE).setAmplifier(0).setDuration(100));
                                }
                                break;
                            case ItemID.GHAST_TEAR:
                                for (GPlayer member : faction.onlineMembers()) {
                                    if (Objects.equals(member.xuid, ((GPlayer) player).xuid)) continue;
                                    if (player.distance(member) > 10) continue;
                                    member.addEffect(Effect.getEffect(Effect.REGENERATION).setAmplifier(0).setDuration(100));
                                }
                                break;
                            case ItemID.BLAZE_POWDER:
                                for (GPlayer member : faction.onlineMembers()) {
                                    if (Objects.equals(member.xuid, ((GPlayer) player).xuid)) continue;
                                    if (player.distance(member) > 10) continue;
                                    member.addEffect(Effect.getEffect(Effect.STRENGTH).setAmplifier(0).setDuration(100));
                                }
                                break;
                        }
                    }
                }
            }
        }
    }
}
