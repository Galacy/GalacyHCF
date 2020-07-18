package galacy.galacyhcf.borders;

import cn.nukkit.math.Vector3;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.managers.BorderFace;

public class SpawnBorder extends Border {
    public int y;

    public SpawnBorder(int minX, int minZ, int maxX, int maxZ) {
        super(minX, minZ, maxX, maxZ);
        this.y = GalacyHCF.instance.getServer().getDefaultLevel().getHighestBlockAt(maxX, maxZ) + 1;
    }

    public boolean insideSpawn(int x, int z) {
        return inside(x, z);
    }

    public BorderFace borderFace(int x, int z) {
        if (x > minX && x < maxX) {
            if (z < minZ) {
                return new BorderFace(new Vector3(minX, y, minZ), new Vector3(maxX, y, minZ));
            }
            if (z > maxZ) {
                return new BorderFace(new Vector3(minX, y, maxZ), new Vector3(maxX, y, maxZ));
            }
        }
        if (z > minZ && z < maxZ) {
            if (x < minX) {
                return new BorderFace(new Vector3(minX, y, minZ), new Vector3(minX, y, maxZ));
            }
            if (x > maxX) {
                return new BorderFace(new Vector3(maxX, y, minZ), new Vector3(maxX, y, maxZ));
            }
        }

        return null;
    }
}
