package ru.wildleaf.liedetectorprank.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

import java.util.Locale;

/**
 * Created by Данил on 20.06.2014.
 */

public class FullScreenActivity extends Activity {

    private String systemLanguage = "";
    protected String savedlang;
    protected Boolean soundStatus;
    private String lang;
    private static final String PREFS_NAME = "TrueOrLiePrefs";
    private SharedPreferences.Editor editor;
    public SharedPreferences settings;
    private MediaPlayer mp = null;

    public FullScreenActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // настройка языкового отображения

        settings = getSharedPreferences(PREFS_NAME, 0);

        editor = settings.edit();

        savedlang = settings.getString("lang", null);

        if (savedlang == null) {
            lang = Locale.getDefault().getLanguage();
        } else {
            lang = savedlang;
        }

        setLanguage(lang);

        // настройка звука

        soundStatus = settings.getBoolean("sound", true);
    }

    protected void saveNotificationStatus(Boolean notificationStatus) {
        editor.putBoolean("notific", notificationStatus);
        editor.commit();
    }

    protected void setLanguage(String langStr) {
        Locale locale = new Locale(langStr);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
    }

    protected void saveLanguage(String langStr) {
        editor.putString("lang", langStr);
        editor.commit();
    }

    protected void soundOnOff(Boolean soundChange) {
        soundStatus = soundChange;
        editor.putBoolean("sound", soundChange);
        editor.commit();
    }

    protected void soundOff() {
        soundOnOff(false);
    }

    protected void soundOn() {
        soundOnOff(true);
    }

    protected Boolean getSoundStatus() {
        return soundStatus;
    }

    protected void playSound(int soundRawId) {
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        if (getSoundStatus() == true) {
            mp = MediaPlayer.create(this, soundRawId);
            mp.start();
        }
    }

    protected void stopSound() {
        if (mp != null) {
            mp.stop();
        }
    }

}
