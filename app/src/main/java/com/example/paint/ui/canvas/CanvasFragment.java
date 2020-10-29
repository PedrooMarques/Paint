package com.example.paint.ui.canvas;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paint.Canvas;
import com.example.paint.GestureListener;
import com.example.paint.R;
import com.example.paint.ui.palette.PaletteViewModel;

public class CanvasFragment extends Fragment {

    private CanvasViewModel mCanvasViewModel;
    private PaletteViewModel mPaletteSharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mCanvasViewModel = new ViewModelProvider(this).get(CanvasViewModel.class);
        mPaletteSharedViewModel = new ViewModelProvider(requireActivity()).get(PaletteViewModel.class);

        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GestureListener mGestureListener = new GestureListener();
        GestureDetector mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);

        // create new Canvas custom view
        Canvas paintCanvas = new Canvas(getContext(), null, mGestureDetector);
        mGestureListener.setCanvas(paintCanvas);

        // define Canvas as layout view
        ConstraintLayout layout = view.findViewById(R.id.fragmentCanvasConstraintLayout);
        layout.addView(paintCanvas);

        mPaletteSharedViewModel.setBrushColor(paintCanvas.getBrushColor());

        mPaletteSharedViewModel.getBrushSize().observe(getViewLifecycleOwner(), paintCanvas::setBrushSize);
        mPaletteSharedViewModel.getBrushColor().observe(getViewLifecycleOwner(), paintCanvas::setBrushColor);
    }
}