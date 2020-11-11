package com.example.paint.ui.canvas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paint.Canvas;
import com.example.paint.GestureListener;
import com.example.paint.R;
import com.example.paint.ui.palette.PaletteViewModel;

public class CanvasFragment extends Fragment {

    private SensorManager sensorManager;
    private Sensor mLAccelerometer;
    private Sensor mLightSensor;

    private Canvas paintCanvas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // view models
        CanvasViewModel mCanvasViewModel = new ViewModelProvider(this).get(CanvasViewModel.class);
        PaletteViewModel mPaletteSharedViewModel = new ViewModelProvider(requireActivity()).get(PaletteViewModel.class);

        // sensors + manager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        mLAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // touch events
        GestureListener mGestureListener = new GestureListener();
        GestureDetector mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);

        // shared preferences
        SharedPreferences sharedPref = requireContext().getSharedPreferences(
                requireContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int defaultValue = 0;
        int selectedColor = sharedPref.getInt(
                requireContext().getString(R.string.background_color_preference), defaultValue);

        mCanvasViewModel.setCanvasColor(selectedColor);

        requestWriteSettingsPermission();

        if (Settings.System.canWrite(getContext())) {
            // create new Canvas custom view
            paintCanvas = new Canvas(getContext(), null, mGestureDetector);
            paintCanvas.setBackgroundColor(selectedColor);

            if (mPaletteSharedViewModel.getBrushSize().getValue() != null)
                paintCanvas.setBrushSize(mPaletteSharedViewModel.getBrushSize().getValue());
            if (mPaletteSharedViewModel.getBrushColor().getValue() != null)
                paintCanvas.setBrushColor(mPaletteSharedViewModel.getBrushColor().getValue());

            mGestureListener.setCanvas(paintCanvas);

            // define Canvas as layout view
            ConstraintLayout layout = view.findViewById(R.id.fragmentCanvasConstraintLayout);
            layout.addView(paintCanvas);

            mPaletteSharedViewModel.setBrushColor(paintCanvas.getBrushColor());
            mPaletteSharedViewModel.setBrushSize(paintCanvas.getBrushSize());

            mPaletteSharedViewModel.getBrushSize().observe(getViewLifecycleOwner(), paintCanvas::setBrushSize);
            mPaletteSharedViewModel.getBrushColor().observe(getViewLifecycleOwner(), paintCanvas::setBrushColor);

            mCanvasViewModel.getCanvasColor().observe(getViewLifecycleOwner(), paintCanvas::setCanvasColor);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestWriteSettingsPermission() {
        if (!Settings.System.canWrite(getContext())) {
            // If has permission then show an alert dialog with message.
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("PERMISSION REQUIRED");
            alertDialog.setMessage("This app requires permission to write on the system settings" +
                    " to change the brightness of the screen according to the daylight");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ALLOW", (dialog, which) ->
                    startGrantPermissionActivity());
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (dialog, which) ->
                    requireActivity().finish());
            alertDialog.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startGrantPermissionActivity() {
        // If do not have write settings permission then open the Can modify system settings panel.
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(paintCanvas, mLAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(paintCanvas, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(paintCanvas);
    }
}