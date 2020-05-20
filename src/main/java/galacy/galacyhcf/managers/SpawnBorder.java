package galacy.galacyhcf.managers;

import cn.nukkit.math.Vector3;
import galacy.galacyhcf.GalacyHCF;

public class SpawnBorder {
    public int minX, minZ, maxX, maxZ, y;

    public SpawnBorder(int minX, int minZ, int maxX, int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
        this.y = GalacyHCF.instance.getServer().getDefaultLevel().getSafeSpawn().getFloorY();
    }

    public boolean insideSpawn(int x, int z) {
        return minX <= x && maxX >= x && minZ <= z && maxZ >= z;
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
