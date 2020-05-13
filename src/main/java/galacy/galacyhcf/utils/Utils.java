package galacy.galacyhcf.utils;

import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Utils {
    public static final String prefix = GalacyHCF.dotenv.get("PREFIX") + TextFormat.RESET;
    public static final Pattern factionRgx = Pattern.compile("^[a-zA-Z0-9]+$");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
