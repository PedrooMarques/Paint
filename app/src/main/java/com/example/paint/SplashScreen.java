package com.example.paint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!Settings.System.canWrite(this)) {
            // If has permission then show an alert dialog with message.
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("You have system write settings permission now.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ALLOW", (dialog, which) -> {
                // If do not have write settings permission then open the Can modify system settings panel.
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                startActivity(intent);
            });
            alertDialog.show();
        } else {
            Toast.makeText(this, "Permission to write on system settings granted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}