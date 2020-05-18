package galacy.galacyhcf.managers;

public class WorldBorder {
    public int minX, minZ, maxX, maxZ;

    public WorldBorder(int minX, int minZ, int maxX, int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public boolean outside(int x, int z) {
        return x > maxX || x < minX || z > maxZ || z < minZ;
    }
}
