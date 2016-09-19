package in.nyuyu.android.commons;

/**
 * Created by Vinay on 19/09/16.
 */
public class Scopes {
    public static String onboarding(String userId) {
        return String.format("onboarding_%s", userId);
    }
}
