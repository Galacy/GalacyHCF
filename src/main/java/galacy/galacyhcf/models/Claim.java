package galacy.galacyhcf.models;

import java.sql.Date;

public class Claim {
    public static final int factionClaim = 0;
    public static final int spawnClaim = 1;

    public int id;
    public Date createdAt;
    public Date updatedAt;
    public int type;
    public int factionId;
    public String factionName;
    public int x1, x2, z1, z2;

    public Claim(int id, Date createdAt, Date updatedAt, int type, int factionId, String factionName, int x1, int x2, int z1, int z2) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.factionId = factionId;
        this.factionName = factionName;
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
    }
}
