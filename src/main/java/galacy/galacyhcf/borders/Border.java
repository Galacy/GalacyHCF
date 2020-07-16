package galacy.galacyhcf.borders;

public class Border {
    public int minX, minZ, maxX, maxZ;

    public Border(int minX, int minZ, int maxX, int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public boolean inside(int x, int z) {
        return minX <= x && maxX >= x && minZ <= z && maxZ >= z;
    }

    public boolean outside(int x, int z) {
        return x > maxX || x < minX || z > maxZ || z < minZ;
    }
}
