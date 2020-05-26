package galacy.galacyhcf.tasks;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.providers.SQLStatements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DtrRegenerationTask extends PluginTask<GalacyHCF> {
    public HashMap<Integer, Long> freeze = new HashMap<>();

    public DtrRegenerationTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Faction faction : factions()) {
            if (freeze.containsKey(faction.id)) {
                if (freeze.get(faction.id) > System.currentTimeMillis()) continue;
                freeze.remove(faction.id);
            }
            faction.updateDtr(Math.min(faction.maxDtr, faction.dtr + 0.05));
        }
    }

    public Faction[] factions() {
        ArrayList<Faction> factions = new ArrayList<>();
        try {
            ResultSet results = GalacyHCF.mysql.query(SQLStatements.dtrFactions);
            while (results.next()) {
                factions.add(new Faction(GalacyHCF.mysql, results));
            }
            results.close();
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues getting faction members: " + e);
        }

        return (Faction[]) factions.toArray();
    }

    public void freeze(int factionId) {
        freeze.remove(factionId);
        freeze.put(factionId, System.currentTimeMillis() + (1000 * 30 * 60));
    }
}
