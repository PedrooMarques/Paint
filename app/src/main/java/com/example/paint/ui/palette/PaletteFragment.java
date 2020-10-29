package com.example.paint.ui.palette;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paint.R;

import java.util.Objects;

import top.defaults.colorpicker.ColorPickerView;

public class PaletteFragment extends DialogFragment {

    private PaletteViewModel mViewModel;

    private Dialog d;

    private float tempBrushSize;
    private int tempBrushColor;

    private String brushSizeString;
    private final String BRUSH_SIZE_TEXT_VIEW_HEADER = "Brush size: ";

    public static PaletteFragment newInstance() {
        return new PaletteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.palette_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(PaletteViewModel.class);

        // find Views by Id
        TextView brushSizeTextView = view.findViewById(R.id.sliderValueTextView);
        SeekBar brushSizeSlider = view.findViewById(R.id.brushSizeSlider);
        ColorPickerView colorPickerView = view.findViewById(R.id.paletteColorPicker);
        Button resetButton = view.findViewById(R.id.resetButton);
        Button applyButton = view.findViewById(R.id.applyButton);
        Button dismissButton = view.findViewById(R.id.dismissButton);

        d = getDialog();

        brushSizeString = BRUSH_SIZE_TEXT_VIEW_HEADER + brushSizeSlider.getProgress();
        brushSizeTextView.setText(brushSizeString);

        //TODO set progress to last progress

        brushSizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempBrushSize = progress;
                brushSizeString = BRUSH_SIZE_TEXT_VIEW_HEADER + progress;
                brushSizeTextView.setText(brushSizeString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (mViewModel.getBrushColor().getValue() != null)
            colorPickerView.setInitialColor(mViewModel.getBrushColor().getValue());
        else colorPickerView.setInitialColor(Color.BLACK);

        colorPickerView.subscribe((color, fromUser, shouldPropagate) -> {
            tempBrushColor = color;
        });

        resetButton.setOnClickListener(v -> {
            brushSizeSlider.setProgress(Objects.requireNonNull(mViewModel.getBrushSize().getValue()).intValue());
            colorPickerView.setInitialColor(mViewModel.getBrushColor().getValue());
        });

        applyButton.setOnClickListener(v -> {
            mViewModel.setBrushSize(tempBrushSize);
            mViewModel.setBrushColor(tempBrushColor);
            Toast.makeText(getContext(), "Changes applied", Toast.LENGTH_SHORT).show();
        });

        dismissButton.setOnClickListener(v -> {
            d.dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // This will set the dialog size to match parent
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(d.getWindow()).setLayout(width, height);
        }
    }
}