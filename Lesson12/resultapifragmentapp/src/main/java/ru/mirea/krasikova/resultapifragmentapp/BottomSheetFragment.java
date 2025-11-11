package ru.mirea.krasikova.resultapifragmentapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String REQUEST_KEY = "data_request_key";
    public static final String BUNDLE_KEY = "data_bundle_key";
    private TextView textViewResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewResult = view.findViewById(R.id.textViewResult);

        getParentFragmentManager().setFragmentResultListener(REQUEST_KEY, this, (requestKey, result) -> {
            String data = result.getString(BUNDLE_KEY);
            textViewResult.setText(data);
        });
    }
}