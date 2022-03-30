package geez.jdevs.keradion.dbManagers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static SharedPreferences.Editor getEditor(Context context) {
        sharedPreferences = context.getSharedPreferences("localDB",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return editor;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("localDB",Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
