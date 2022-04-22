package utils;

import java.util.regex.Pattern;

public class Utils {

    public static final Pattern DMSLATPATTERN =
            Pattern.compile("(-?)([0-9]{1,3})°([0-5]?[0-9])'([0-5]?[0-9])\\.([0-9]{0,4})\\\"([NS])");
    public static final Pattern DMSLNGPATTERN =
            Pattern.compile("(-?)([0-9]{1,3})°([0-5]?[0-9])'([0-5]?[0-9])\\.([0-9]{0,4})\\\"([EW])");
}
