package com.example.paint.ui.palette;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paint.R;

import java.util.Objects;

import top.defaults.colorpicker.ColorPickerView;

public class PaletteFragment extends DialogFragment {

    private PaletteViewModel mViewModel;

    private ColorPickerView colorPickerView;

    private final String BRUSH_SIZE_TEXT_VIEW_HEADER = "Brush size: ";
    private SeekBar brushSizeSlider;
    private String brushSizeString;

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
        brushSizeSlider = view.findViewById(R.id.brushSizeSlider);
        colorPickerView = view.findViewById(R.id.paletteColorPicker);

        brushSizeString = BRUSH_SIZE_TEXT_VIEW_HEADER + brushSizeSlider.getProgress();
        brushSizeTextView.setText(brushSizeString);

        //TODO set progress to last progress

        brushSizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewModel.setBrushSize(progress);
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

        colorPickerView.setInitialColor(mViewModel.getBrushColor().getValue());
        colorPickerView.subscribe((color, fromUser, shouldPropagate) -> {
            mViewModel.setBrushColor(color);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // This will set the dialog size to match parent
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(d.getWindow()).setLayout(width, height);
        }
    }
}