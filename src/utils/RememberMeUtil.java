package utils ;

import java.util.prefs.Preferences;

public class RememberMeUtil {

    private static final Preferences prefs = Preferences.userNodeForPackage(RememberMeUtil.class);
    
    private static final String KEY_TOKEN = "rememberToken";

    public static void saveToken(String token) {
        prefs.put(KEY_TOKEN, token);
    }

    public static String getToken() {
        return prefs.get(KEY_TOKEN, null);
    }

    public static void clearToken() {
        prefs.remove(KEY_TOKEN);
    }
}
