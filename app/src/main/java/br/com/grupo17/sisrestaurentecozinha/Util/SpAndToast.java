package br.com.grupo17.sisrestaurentecozinha.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SpAndToast {
    private static String PREF = "br.com.grupo17.sisrestaurentecozinha.PREF";

    public static void saveSP(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return (sp.getString(key, ""));
    }

    public static void resetSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}