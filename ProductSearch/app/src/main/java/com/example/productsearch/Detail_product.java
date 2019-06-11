package com.example.productsearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Detail_product extends Fragment {

    private View view;


    private Context mContext;

    private List<String> picture = new ArrayList<>();
    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_product, container, false);

        ViewGroup parent = (ViewGroup) view.getParent();

        if(parent != null){
            parent.removeView(view);
        }
        if(picture != null){
            try {
                //showMessage("staticMyPlace = " + MyPlace.toString());
//                dealWithInfo(picture);
            }catch (Exception e){
                showMessage(e.getLocalizedMessage() + "!");
            }
        }

        return view;

    }



    private void showMessage(String txt) {
        Toast toast = Toast.makeText(this.getContext(), txt, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 10);
        toast.show();
    }
}
