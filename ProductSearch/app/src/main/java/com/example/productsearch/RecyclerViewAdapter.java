package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageName = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mPostalCode = new ArrayList<>();
    private ArrayList<String> mCondition = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mWish  = new ArrayList<>();
    private ArrayList<String> mShipping = new ArrayList<>();
    private ArrayList<String> mId = new ArrayList<>();


    private Context mContext;
    public static final String TITLE = "Title";
    public static final String IMAGE = "Image";
    public static final String POSTALCODE = "postalcode";
    public static final String CONDITION = "condition";
    public static final String PRICE = "Price";
    public static final String WISH = "WISH";
    public static final String SHIPPING = "SHIPPING";
    public static final String ITEM = "ItemId";

    private static DecimalFormat df2 = new DecimalFormat("#.##");


    Wishlist wishlist = new Wishlist();
    FragmentWish fragmentWish = new FragmentWish();



    public RecyclerViewAdapter(Context mContext, ArrayList<String> mId, ArrayList<String> mImageName, ArrayList<String> mImages, ArrayList<String> mPostalCode, ArrayList<String> mCondition, ArrayList<String> mPrice, ArrayList<String> mWish, ArrayList<String> mShipping) {
        this.mImageName = mImageName;
        this.mId = mId;
        this.mImages = mImages;
        this.mPostalCode = mPostalCode;
        this.mCondition = mCondition;
        this.mPrice = mPrice;
        this.mWish = mWish;
        this.mShipping = mShipping;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        Glide.with(mContext).asBitmap().load(mImages.get(i)).into((viewHolder.image));

        viewHolder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWish.get(i).equals("false")){
                    Log.i("HERE WISH FALSE", "HERE");
                    Glide.with(mContext).asBitmap().load("").placeholder(R.drawable.cart_remove).into(viewHolder.cart);
                    Toast.makeText(mContext, mImageName.get(i) + " was added to wish list", Toast.LENGTH_SHORT).show();
                    mWish.set(i, "true");
                    Log.i("INFO", mImageName.get(i));
                    Log.i("INFO",  mImages.get(i));
                    Log.i("INFO",  mPostalCode.get(i));
                    Log.i("INFO",  mCondition.get(i));
                    Log.i("INFO",  mPrice.get(i));
                    Log.i("INFO",  mWish.get(i));

                    wishlist.addWish(mImageName.get(i), mImages.get(i), mPostalCode.get(i), mCondition.get(i), mPrice.get(i), mWish.get(i), mShipping.get(i), mId.get(i));



                    notifyDataSetChanged();

                    double total = 0.0;
                    ArrayList<String> wishArray = wishlist.getWish_Price();
                    if (wishArray.size() == 0){
                        fragmentWish.nowishLinear.setVisibility(View.VISIBLE);
                        fragmentWish.recyclerView_Wish.setVisibility(View.GONE);
                    }else {
                        fragmentWish.nowishLinear.setVisibility(View.GONE);
                        fragmentWish.recyclerView_Wish.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < wishArray.size(); i++){
                        total += Double.parseDouble(wishArray.get(i));
                    }
                    String totalString = "$" + df2.format(total);
                    fragmentWish.money.setText(totalString);
                    String itemNumberString = "(" + wishlist.getWish_Id().size() + " items )";
                    fragmentWish.itemNumber.setText(itemNumberString);


                } else {
                    Glide.with(mContext).asBitmap().load("").placeholder(R.drawable.car_plus).into(viewHolder.cart);
                    Toast.makeText(mContext, mImageName.get(i) + " was removed to wish list", Toast.LENGTH_SHORT).show();
                    mWish.set(i, "false");
                    wishlist.deleteWish(mId.get(i));

                    notifyDataSetChanged();

                    double total = 0.0;
                    ArrayList<String> wishArray = wishlist.getWish_Price();
                    if (wishArray.size() == 0){
                        fragmentWish.nowishLinear.setVisibility(View.VISIBLE);
                        fragmentWish.recyclerView_Wish.setVisibility(View.GONE);
                    }else {
                        fragmentWish.nowishLinear.setVisibility(View.GONE);
                        fragmentWish.recyclerView_Wish.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < wishArray.size(); i++){
                        total += Double.parseDouble(wishArray.get(i));
                    }

                    String totalString = "$" + df2.format(total);
                    fragmentWish.money.setText(totalString);
                    String itemNumberString = "(" + wishlist.getWish_Id().size() + " items )";
                    fragmentWish.itemNumber.setText(itemNumberString);


                }




            }
        });

        if (mWish.get(i).equals("false")){
            Glide.with(mContext).asBitmap().load("").placeholder(R.drawable.car_plus).into(viewHolder.cart);

        } else {
            Glide.with(mContext).asBitmap().load("").placeholder(R.drawable.cart_remove).into(viewHolder.cart);

        }
        String shippingString;

        viewHolder.image_name.setText(mImageName.get(i));
        viewHolder.image_name.setMaxLines(2);
        String priceString = "$" + mPrice.get(i);
        viewHolder.price.setText(priceString);
        viewHolder.condition.setText(mCondition.get(i));
        if (mShipping.get(i).equals("0.0")){
            shippingString = "Free Shipping";
        }else if (mShipping.get(i).equals("N/A")){
            shippingString = "N/A";
        }else{
            shippingString = "$" + mShipping.get(i);
        }
        viewHolder.shipping.setText(shippingString);

        String postalcodeString = "Zip: " + mPostalCode.get(i);
        viewHolder.postalcode.setText(postalcodeString);

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Details.class);
                intent.putExtra(WISH, mWish.get(i));
                intent.putExtra(ITEM, mId.get(i));
                intent.putExtra(TITLE, mImageName.get(i));
                intent.putExtra(SHIPPING, mShipping.get(i));
                intent.putExtra(PRICE, mPrice.get(i));
                intent.putExtra(IMAGE, mImages.get(i));
                intent.putExtra(CONDITION, mCondition.get(i));
                intent.putExtra(POSTALCODE, mPostalCode.get(i));
                Toast.makeText(mContext, mImageName.get(i), Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageName.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView image_name;
        RelativeLayout relativeLayout;
        TextView price;
        TextView condition;
        TextView shipping;
        ImageView cart;
        TextView postalcode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            image_name = itemView.findViewById(R.id.image_name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            price = itemView.findViewById(R.id.price);
            condition = itemView.findViewById(R.id.condition);
            cart = itemView.findViewById(R.id.cart);
            shipping = itemView.findViewById(R.id.shipping);
            postalcode = itemView.findViewById(R.id.postalcode);

        }
    }

}
