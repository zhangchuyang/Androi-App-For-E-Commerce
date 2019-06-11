package com.example.productsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentWish extends Fragment {

    private View view;
    public ArrayList<String> wish_ImageName;
    public ArrayList<String> wish_Image;
    public ArrayList<String> wish_PostalCode ;
    public ArrayList<String> wish_Condition;
    public ArrayList<String> wish_Price;
    public ArrayList<String> wish_Wish ;
    public ArrayList<String> wish_Shipping ;
    public ArrayList<String> wish_Id ;

    public static TextView money;
    public static TextView itemNumber;
    public static LinearLayout nowishLinear;
    public static RecyclerView recyclerView_Wish;

    private static DecimalFormat df2 = new DecimalFormat("#.##");



    Wishlist wishlist = new Wishlist();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wish, container, false);
        initWish();

        return view;
    }

    private void showMessage(String txt) {
        Toast toast = Toast.makeText(this.getContext(), txt, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
    }

    public void initWish(){

        Log.i("INITWISH", "Fragementwish");



        wish_Condition = wishlist.getWish_Condition();
        Log.i("Condition_wish",wish_Condition.toString());
        wish_Id = wishlist.getWish_Id();
        Log.i("Condition_wish",wish_Id.toString());
        wish_Image = wishlist.getWish_Image();
        wish_ImageName = wishlist.getWish_ImageName();
        wish_PostalCode = wishlist.getWish_PostalCode();
        wish_Price = wishlist.getWish_Price();
        wish_Shipping = wishlist.getWish_Shipping();
        wish_Wish = wishlist.getWish_Wish();

        Log.i("WISH SIZE", wishlist.getSizeWish());
        nowishLinear = view.findViewById(R.id.nowish_linear);
        recyclerView_Wish = view.findViewById(R.id.recyclerView_wish);

        if (wishlist.getIntSizeWish() == 0){
            nowishLinear.setVisibility(View.VISIBLE);
            recyclerView_Wish.setVisibility(View.GONE);
        } else {
            nowishLinear.setVisibility(View.GONE);
            recyclerView_Wish.setVisibility(View.VISIBLE);
        }

        money = view.findViewById(R.id.money);
        itemNumber = view.findViewById(R.id.itemNumber);

        double total = 0.0;
        ArrayList<String> wishArray = wishlist.getWish_Price();
        if (wishArray.size() == 0){
            nowishLinear.setVisibility(View.VISIBLE);
            recyclerView_Wish.setVisibility(View.GONE);
        }else {
            nowishLinear.setVisibility(View.GONE);
            recyclerView_Wish.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < wishArray.size(); i++){
            total += Double.parseDouble(wishArray.get(i));
        }
        String totalString = "$" + df2.format(total);
        money.setText(totalString);
        String itemNumberString = "(" + wishlist.getWish_Id().size() + " items )";
        itemNumber.setText(itemNumberString);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getContext(), wish_Id, wish_ImageName, wish_Image, wish_PostalCode, wish_Condition, wish_Price, wish_Wish, wish_Shipping );
        recyclerView_Wish.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView_Wish.setLayoutManager(mLayoutManager);

    }

}
