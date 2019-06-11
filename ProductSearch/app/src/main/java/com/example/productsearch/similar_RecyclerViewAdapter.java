package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class similar_RecyclerViewAdapter extends RecyclerView.Adapter<similar_RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "SIMILAR_ADAPTER";

    private Context mContext;
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mtitle = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mShipping = new ArrayList<>();
    private ArrayList<String> mId = new ArrayList<>();
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mURL = new ArrayList<>();


    public static final String ITEM = "ItemId";


    public similar_RecyclerViewAdapter( Context mContext, ArrayList<String> mImages, ArrayList<String> title, ArrayList<String> mPrice, ArrayList<String> mShipping, ArrayList<String> mId, ArrayList<String> mDays, ArrayList<String> mURL) {
        this.mImages = mImages;
        this.mtitle = title;
        this.mPrice = mPrice;
        this.mShipping = mShipping;
        this.mId = mId;
        this.mContext = mContext;
        this.mDays = mDays;
        this.mURL = mURL;
    }

    @NonNull
    @Override
    public similar_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similar_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull similar_RecyclerViewAdapter.ViewHolder viewHolder, final int i) {

        String shippingString;

        Glide.with(mContext).asBitmap().load(mImages.get(i)).into(viewHolder.image);
        viewHolder.title.setText(mtitle.get(i));
        String priceString = "$" + mPrice.get(i);
        viewHolder.price.setText(priceString);
        if (mShipping.get(i).equals("0.00")){
            shippingString = "Free Shipping";
        }else if (mShipping.get(i).equals("N/A")){
            shippingString = "N/A";
        }else{
            shippingString = "$" + mShipping.get(i);
        }
        viewHolder.shipping.setText(shippingString);
        String daysString = mDays.get(i) + " Days Left";
        viewHolder.days.setText(daysString);

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(mURL.get(i));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);

//                intent.putExtra(ITEM, mId.get(i));
//                intent.putExtra(TITLE, mImageName.get(i));
//                intent.putExtra(SHIPPING, mShipping.get(i));
//
//                Toast.makeText(mContext, mtitle.get(i), Toast.LENGTH_SHORT).show();
//                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mtitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView price;
        TextView shipping;
        TextView days;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.similar_img);
            title = itemView.findViewById(R.id.similar_title);
            price = itemView.findViewById(R.id.similar_price);
            shipping = itemView.findViewById(R.id.similar_shipping);
            days = itemView.findViewById(R.id.similar_daysLeft);
            relativeLayout = itemView.findViewById(R.id.similar_relative);
        }
    }

}
