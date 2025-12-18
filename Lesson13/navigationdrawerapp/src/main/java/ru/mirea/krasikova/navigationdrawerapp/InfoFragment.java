package ru.mirea.krasikova.navigationdrawerapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.krasikova.navigationdrawerapp.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment {
    private FragmentInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);

        binding.textViewInfo.setText(
                "Это приложение использует Bottom Navigation — панель навигации, " +
                        "расположенную в нижней части экрана, которая обеспечивает быстрый доступ " +
                        "к основным разделам приложения."
        );

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}