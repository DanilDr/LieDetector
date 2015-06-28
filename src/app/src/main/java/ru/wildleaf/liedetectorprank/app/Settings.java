package ru.wildleaf.liedetectorprank.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ru.wildleaf.liedetectorprank.app.R;

/**
 * Created by Данил on 18.07.2014.
 */

public class Settings extends FullScreenActivity {

    private ACLineText notificationStatus;
    private NotificationControl notificationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        // выбор языка
        RelativeLayout choiseLanguageButton = (RelativeLayout) findViewById(R.id.choiseLanguageButton);
        choiseLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguage(null);
                Intent choiseLanguage = new Intent(Settings.this, ChoiseLanguage.class);
                Settings.this.startActivity(choiseLanguage);
            }
        });
        // уведомления
        notificationStatus = (ACLineText) findViewById(R.id.notificationStatus);
        setNotificationStatus(settings.getBoolean("notific", true));

        LinearLayout notificationButton = (LinearLayout) findViewById(R.id.notificationButton);
        notificationControl = new NotificationControl(this);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settings.getBoolean("notific", true) == true) {
                    saveNotificationStatus(false);
                    setNotificationStatus(false);
                    notificationControl.switchOff();
                } else {
                    saveNotificationStatus(true);
                    setNotificationStatus(true);
                    notificationControl.switchOn();
                }
            }
        });
        // ссылка на лицензию
        ImageView licenseLogo = (ImageView) findViewById(R.id.licenseLogo);
        licenseLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.license_url)));
                startActivity(intent);
            }
        });
        return;
    }

    private void setNotificationStatus(Boolean status) {
        if (status == true) {
            notificationStatus.setText(R.string.on);
        } else {
            notificationStatus.setText(R.string.off);
        }
    }
}