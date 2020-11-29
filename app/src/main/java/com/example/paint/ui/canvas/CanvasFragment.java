package com.example.paint.ui.canvas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CanvasFragment extends Fragment {

    private SensorManager sensorManager;
    private Sensor mLAccelerometer;
    private Sensor mLightSensor;

    private Canvas paintCanvas;

    private static final String FIREBASE_TAG = "firebase_debug";

    private static CanvasViewModel mCanvasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_canvas, container, false);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void saveCanvas() {

        Map<String, Object> paintMap = new HashMap<>();
        Map<String, Object> pathsMap = new HashMap<>();

        if (!mCanvasViewModel.getCustomPaths().getValue().isEmpty()) {

            // foreach pair in the view models list of pairs
            for (Pair<com.example.paint.Path, com.example.paint.Paint> item : Objects.requireNonNull(mCanvasViewModel.getCustomPaths().getValue())) {

                paintMap.put(item.second.toString(), item.second);

                pathsMap.put(item.first.toString(), item.first);
            }

            // add timestamap to each map to be able get the most recent
            Timestamp timestamp = new Timestamp(new Date());
            pathsMap.put("timestamp", timestamp);
            paintMap.put("timestamp", timestamp);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("paths")
                    .add(pathsMap)
                    .addOnSuccessListener(documentReference -> Log.d(FIREBASE_TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(FIREBASE_TAG, "Error adding document", e));
            db.collection("paint")
                    .add(paintMap)
                    .addOnSuccessListener(documentReference -> Log.d(FIREBASE_TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(FIREBASE_TAG, "Error adding document", e));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void loadCanvas() {

        ArrayList<Paint> paintList = new ArrayList<>();
        ArrayList<Path> pathsList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // get paths
        db.collection("paths")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // get the newest doc (it will be the first because its ordered)
                        DocumentSnapshot recentDoc = Objects.requireNonNull(task.getResult()).getDocuments().get(0);

                        for (Map.Entry<String, Object> e : Objects.requireNonNull(recentDoc.getData()).entrySet()) {
                            // exclude the last entry (timestamp entry)
                            if (!e.getKey().equals("timestamp")) {

                                Map<String, Object> tempMap = new HashMap<>((Map<String, Object>) e.getValue());

                                Path path = new Path();
                                com.example.paint.Path customPath = new com.example.paint.Path();

                                for (Map.Entry<String, Object> values : tempMap.entrySet()) {

                                    double d;
                                    float f;

                                    switch (values.getKey()) {
                                        case "path":
                                            Map<String, Object> map = new HashMap((Map<String, Object>) values.getValue());
                                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                                if (entry.getKey().equals("fillType"))
                                                    path.setFillType(Path.FillType.valueOf(String.valueOf(entry.getValue())));
                                                if (entry.getKey().equals("inverseFillType")) {
                                                    if ((boolean) entry.getValue())
                                                        path.toggleInverseFillType();
                                                }
                                            }
                                            break;
                                        case "initX":
                                            d = (double) values.getValue();
                                            f = (float) d;
                                            customPath.setInitX(f);
                                            break;
                                        case "initY":
                                            d = (double) values.getValue();
                                            f = (float) d;
                                            customPath.setInitY(f);
                                            break;
                                        case "finalX":
                                            d = (double) values.getValue();
                                            f = (float) d;
                                            customPath.setFinalX(f);
                                            break;
                                        case "finalY":
                                            d = (double) values.getValue();
                                            f = (float) d;
                                            customPath.setFinalY(f);
                                            break;
                                    }
                                }

                                path.moveTo(customPath.getInitX(), customPath.getInitY());
                                path.lineTo(customPath.getFinalX(), customPath.getFinalY());
                                pathsList.add(path);
                            }
                        }

                        // get paint
                        db.collection("paint")
                                .orderBy("timestamp", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // get the newest doc
                                        DocumentSnapshot recentDoc1 = Objects.requireNonNull(task1.getResult()).getDocuments().get(0);

                                        for (Map.Entry<String, Object> e : Objects.requireNonNull(recentDoc1.getData()).entrySet()) {

                                            if (!e.getKey().equals("timestamp")) {

                                                Map<String, Object> tempMap = new HashMap<>((Map<String, Object>) e.getValue());
                                                Paint paint = new Paint();

                                                for (Map.Entry values : tempMap.entrySet()) {
                                                    if (values.getKey().equals("paintStrokeWidth")) {
                                                        double d = (double) values.getValue();
                                                        float f = (float) d;
                                                        paint.setStrokeWidth(f);
                                                    }
                                                    if (values.getKey().equals("paintStrokeJoin"))
                                                        paint.setStrokeJoin(Paint.Join.valueOf(String.valueOf(values.getValue())));
                                                    if (values.getKey().equals("paintStyle"))
                                                        paint.setStyle(Paint.Style.valueOf(String.valueOf(values.getValue())));
                                                    if (values.getKey().equals("paintColor")) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                            //paint.setColor((long) values.getValue());
                                                            paint.setColor(Color.BLACK);
                                                        }
                                                    }
                                                    if (values.getKey().equals("paintAntiAlias"))
                                                        paint.setAntiAlias((Boolean) values.getValue());
                                                }

                                                paintList.add(paint);
                                            }
                                        }

                                        ArrayList<Pair<Path, Paint>> tempPaths = new ArrayList<>();
                                        for (int i = 0; i < pathsList.size(); i++) {
                                            tempPaths.add(new Pair<>(pathsList.get(i), paintList.get(i)));
                                        }

                                        mCanvasViewModel.setPaths(tempPaths);

                                    } else {
                                        Log.w(FIREBASE_TAG, "Error getting documents.", task1.getException());
                                    }
                                });

                    } else {
                        Log.w(FIREBASE_TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // view models
        mCanvasViewModel = new ViewModelProvider(requireActivity()).get(CanvasViewModel.class);
        PaletteViewModel mPaletteSharedViewModel = new ViewModelProvider(requireActivity()).get(PaletteViewModel.class);

        // sensors + manager
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        mLAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
            paintCanvas = new Canvas(getContext(), null, mGestureDetector, mCanvasViewModel);
            paintCanvas.setBackgroundColor(selectedColor);

            if (mPaletteSharedViewModel.getBrushSize().getValue() != null)
                paintCanvas.setBrushSize(mPaletteSharedViewModel.getBrushSize().getValue());
            if (mPaletteSharedViewModel.getBrushColor().getValue() != null)
                paintCanvas.setBrushColor(mPaletteSharedViewModel.getBrushColor().getValue());

            if (mCanvasViewModel.getPaths().getValue() != null)
                paintCanvas.setPaths(mCanvasViewModel.getPaths().getValue());

            if (mCanvasViewModel.getCustomPaths().getValue() != null)
                paintCanvas.setCustomPaths(mCanvasViewModel.getCustomPaths().getValue());

            mGestureListener.setCanvas(paintCanvas);

            // define Canvas as layout view
            ConstraintLayout layout = view.findViewById(R.id.fragmentCanvasConstraintLayout);
            layout.addView(paintCanvas);

            mPaletteSharedViewModel.setBrushColor(paintCanvas.getBrushColor());
            mPaletteSharedViewModel.setBrushSize(paintCanvas.getBrushSize());

            mPaletteSharedViewModel.getBrushSize().observe(getViewLifecycleOwner(), paintCanvas::setBrushSize);
            mPaletteSharedViewModel.getBrushColor().observe(getViewLifecycleOwner(), paintCanvas::setBrushColor);

            mCanvasViewModel.setPaths(paintCanvas.getPaths());
            mCanvasViewModel.setCustomPaths(paintCanvas.getCustomPaths());

            mCanvasViewModel.getCanvasColor().observe(getViewLifecycleOwner(), paintCanvas::setCanvasColor);
            mCanvasViewModel.getPaths().observe(getViewLifecycleOwner(), paintCanvas::setPaths);
            mCanvasViewModel.getCustomPaths().observe(getViewLifecycleOwner(), paintCanvas::setCustomPaths);
        }
    }
}