package com.example.paint;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paint.ui.palette.PaletteFragment;

public class PaletteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PaletteFragment.newInstance())
                    .commitNow();
        }
    }
}