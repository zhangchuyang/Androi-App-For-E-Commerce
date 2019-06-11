package com.example.productsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Detail_similar extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_similar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner similar_item = (Spinner) view.findViewById(R.id.similar_sort);
        Spinner similar_ascending = (Spinner) view.findViewById(R.id.similar_ascending);
        ArrayAdapter<CharSequence> categoryAdapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.similar_item, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> categoryAdapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.similar_ascending, android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        similar_item.setAdapter(categoryAdapter1);
        similar_ascending.setAdapter(categoryAdapter2);

    }
}
