package galacy.galacyhcf.utils;

import cn.nukkit.utils.TextFormat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Utils {
    public static final String prefix = TextFormat.GOLD + "" + TextFormat.BOLD + "GalacyHCF> " + TextFormat.RESET; //GalacyHCF.dotenv.get("PREFIX") + TextFormat.RESET;
    public static final Pattern factionRgx = Pattern.compile("^[a-zA-Z0-9]+$");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat timerHour = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat timerMinutes = new SimpleDateFormat("mm:ss");

    public static String readFileAsString(String fileName) throws Exception {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
