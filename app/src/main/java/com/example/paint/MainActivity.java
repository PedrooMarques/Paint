package com.example.paint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.paint.ui.palette.PaletteFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    private SharedPreferences sharedPref;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // the main activity layout
        drawer = findViewById(R.id.drawer_layout);
        // the side menu layout
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_canvas, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.action_about:
                // User chose the "AboutActivity" action, show the app about UI...
                startActivity(new Intent(this, AboutActivity.class));
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    // This is for when the android back button is pressed instead of the app back action
//    // so it applies the changes on resume
//    @Override
//    protected void onResume() {
//        super.onResume();
//        int selectedColor = sharedPref.getInt(
//                getString(R.string.background_color_preference), defaultValue);
//        drawer.setBackgroundColor(selectedColor);
//    }

    //TODO background resets when orientation changed

    // This onStop will reset the app background to WHITE when the app closes
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.background_color_preference), Color.WHITE);
        editor.apply();
    }

    // This onPause will reset the app background to WHITE when the app pauses
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.background_color_preference), Color.WHITE);
        editor.apply();
    }

    public void openPaletteDialogFragment(View view) {
        // Create the fragment and show it as a dialog.
        DialogFragment paletteDialogFragment = PaletteFragment.newInstance();
        paletteDialogFragment.show(getSupportFragmentManager(), "dialogFragment");
    }
}