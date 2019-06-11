package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Details extends AppCompatActivity {

    //    private String MY_SERVER_URL = "http://www.chuyang-571.us-east-2.elasticbeanstalk.com/user?";
//    private String MY_SERVER_URL = "http://192.168.70.248:3000/user?";
//    private String MY_SERVER_URL = "http://10.26.2.13:3000/user?";

    private String MY_SERVER_URL = "http://www.ProductSearch2.us-east-2.elasticbeanstalk.com/user?";

    private JSONObject temp;
    private String receiveIDString;
    private String receiveTitleString;
    private String receiveWishString;
    private String shipping;
    private String receivePriceString;
    private String receivePostalCodeString;
    private String receiveConditionString;
    private String receiveImageString;



    private TabLayout tabLayout;
    private ViewPager tab_viewpager;

    private List<Integer> iconList;
    private ArrayList<String> tabList;
    private List<Fragment> mFragments;

    private Detail_product detail_product;
    private Detail_photo detail_photos;
    private Detail_shipping detail_shipping;
    private Detail_similar detail_similar;

    private String finalcontent;
    private Object  obj;
    private Context mContext;

    private List<String> picture = new ArrayList<>();
    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;


    private ArrayList<String> spcificValue = new ArrayList<>();

    private String title;
    private String currentPrice;
    private String storeURL;

    private ArrayList<String> google_img = new ArrayList<>();


    private int pointer1 = 0;
    private int pointer2 = 0;

    private boolean firstListener1 = true;
    private boolean firstListener2 = true;

    TextView noresult;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_bar);

        final Wishlist wishlist = new Wishlist();

        Intent intent = getIntent();
        receiveIDString = intent.getStringExtra(RecyclerViewAdapter.ITEM);
        receiveTitleString = intent.getStringExtra(RecyclerViewAdapter.TITLE);
        receiveWishString = intent.getStringExtra(RecyclerViewAdapter.WISH);
        receivePriceString = intent.getStringExtra(RecyclerViewAdapter.PRICE);
        receivePostalCodeString = intent.getStringExtra(RecyclerViewAdapter.POSTALCODE);
        receiveConditionString = intent.getStringExtra(RecyclerViewAdapter.CONDITION);
        receiveImageString = intent.getStringExtra(RecyclerViewAdapter.IMAGE);
        shipping = intent.getStringExtra(RecyclerViewAdapter.SHIPPING);
        Log.i("Details", receiveTitleString);

        detail_product = new Detail_product();
        detail_photos = new Detail_photo();
        detail_shipping = new Detail_shipping();
        detail_similar = new Detail_similar();



        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("CHECK", String.valueOf(wishlist.checkExistByIdString(receiveIDString)));
                if (wishlist.checkExistByIdString(receiveIDString)){
                    fab.setImageResource(R.drawable.white_cart_plus);
                    wishlist.deleteWish(receiveIDString);
                } else {
                    fab.setImageResource(R.drawable.white_cart_remove);
                    wishlist.addWish(receiveTitleString, receiveImageString, receivePostalCodeString, receiveConditionString, receivePriceString, "true", shipping, receiveIDString);
                }
            }
        });
        if (receiveWishString.equals("false")){
            fab.setImageResource(R.drawable.white_cart_plus);
        } else {
            fab.setImageResource(R.drawable.white_cart_remove);
        }

        initDetails(receiveIDString, shipping);
        initViews(receiveTitleString);

    }

    private void initDetails(String receiveIDString, final String shipping){


        StringBuilder awsURL = new StringBuilder();
        awsURL.append(MY_SERVER_URL);

        try{
            awsURL.append("itemID=");
            awsURL.append(URLEncoder.encode(receiveIDString, "utf-8"));
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        Log.i("DETAIL", awsURL.toString());

        String url = awsURL.toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finalcontent = response;
                try{
                    SearchResult(response, shipping);
                } catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                showMessage("Error in Response");
            }
        });
        queue.add(stringRequest);
    }


    private  void SearchResult(String response, String shipping_txt) {

        Log.i("SEARCHRESULT", "come to search result");

        try{
            obj = new JSONObject(response);
            temp = ((JSONObject) obj).getJSONObject("Item");
            Log.i("TEMP", temp.toString());
            JSONArray p = temp.getJSONArray("PictureURL");

//----------------------------- PRODUCT TAB-----------------
            //Subtitle
            LinearLayout subtitle_layout = findViewById(R.id.subtitle_row);
            subtitle_layout.setVisibility(View.VISIBLE);
            try{
                String subtitle = temp.get("Subtitle").toString();
                TextView subtitle_view = findViewById(R.id.subtitle);
                subtitle_view.setText(subtitle);
            } catch (JSONException e){
                subtitle_layout.setVisibility(View.GONE);
            }

            //PRICE
            JSONObject price = temp.getJSONObject("CurrentPrice");
            currentPrice = "$" + String.valueOf(price.get("Value"));
            Log.i("PRICE", currentPrice);
            TextView price_txt = (TextView) findViewById(R.id.product_price);
            price_txt.setText(currentPrice);
            TextView price_big = (TextView) findViewById(R.id.product_price_big);
            price_big.setText(currentPrice);

// title
            title = temp.get("Title").toString();
            TextView product_title = (TextView) findViewById(R.id.product_title);
            product_title.setText(title);

            //Shipping Info
            String shipping_info;
            if (shipping_txt.equals("Free Shipping") || shipping_txt.equals("N/A")){
                shipping_info = "With " + shipping_txt;
            } else if (shipping_txt.equals("0.0") ){
                shipping_info = "With Free Shipping";
            } else{
                shipping_info = "With $" + shipping_txt;
            }
            Log.i("SHIPPING FROM PREVIOUS", shipping_info);

            TextView product_shipping = (TextView) findViewById(R.id.product_shippingInfo);
            product_shipping.setText(shipping_info);

//Specifications
            String brandName = "";

            try{
                JSONObject itemSpecifics = temp.getJSONObject("ItemSpecifics");
                JSONArray valueList = itemSpecifics.getJSONArray("NameValueList");

                for (int i = 0; i < valueList.length(); i ++){
                    JSONObject element = valueList.getJSONObject(i);
                    JSONArray list = element.getJSONArray("Value");
                    if (element.get("Name").equals("Brand")){
                        brandName = list.get(0).toString();
                    }
                    Log.i("SPECIFICS", list.get(0).toString());
                    spcificValue.add(list.get(0).toString());
                }

                ListView listView = (ListView) findViewById(R.id.product_list);
                ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.list_element, spcificValue);
                listView.setAdapter(listAdapter);
                listView.setDivider(null);

                TextView brand = (TextView) findViewById(R.id.product_brand);
                brand.setText(brandName);

            } catch (JSONException e){
                LinearLayout specifi_layout = findViewById(R.id.specification);
                specifi_layout.setVisibility(View.GONE);
            }



            for (int i = 0; i < p.length(); i++){
                String p_i = p.get(i).toString();
                Log.i("picture", p_i);
                picture.add(p_i);
            }

            mGallery = (LinearLayout) findViewById(R.id.gallery);
            mInflater = LayoutInflater.from(this);
            for (int i = 0; i < picture.size(); i++){
                View view2 = mInflater.inflate(R.layout.activity_gallery, mGallery, false);
                ImageView image = view2.findViewById(R.id.gallery_img);
                Glide.with(getApplicationContext()).asBitmap().load(picture.get(i)).override(1000, 1000).fitCenter().into(image);
                mGallery.addView(view2);
            }

            //-------------------------SHIPPING--------------------------------

            //======================Sold By=========================
            try{
                JSONObject storefront = temp.getJSONObject("Storefront");
                TextView store_name = findViewById(R.id.shipping_storeName);
                TextView feedback_score = findViewById(R.id.shipping_feedbackscore);
                CircularScoreView popularity_score = (CircularScoreView) findViewById(R.id.shipping_popularity);
                ImageView star = findViewById(R.id.shipping_star);


                try{
                    storeURL = storefront.get("StoreURL").toString();
                    String storeName = storefront.get("StoreName").toString();
                    store_name.setText(storeName);
                } catch (JSONException e){
                    store_name.setVisibility(View.GONE);
                }

                try{
                    JSONObject seller = temp.getJSONObject("Seller");
                    String feedbackScore = seller.get("FeedbackScore").toString();
                    feedback_score.setText(feedbackScore);

                    try{
                        Integer popularity = Math.round(Float.parseFloat(seller.get("PositiveFeedbackPercent").toString()));
                        popularity_score.setScore(popularity);
                    }catch ( JSONException e){
                        popularity_score.setVisibility(View.GONE);
                    }

                    try{
                        String ratingStar = seller.get("FeedbackRatingStar").toString();
                        if (ratingStar.equals("Blue")){
                            star.setImageResource(R.drawable.blue_so);
                        } else if (ratingStar.equals("Green")){
                            star.setImageResource(R.drawable.green_so);
                        }else if (ratingStar.equals("GreenShooting")){
                            star.setImageResource(R.drawable.green_s);
                        }else if (ratingStar.equals("Purple")){
                            star.setImageResource(R.drawable.purple_so);
                        }else if (ratingStar.equals("PurpleShooting")){
                            star.setImageResource(R.drawable.purple_s);
                        }else if (ratingStar.equals("Red")){
                            star.setImageResource(R.drawable.red_so);
                        }else if (ratingStar.equals("RedShooting")){
                            star.setImageResource(R.drawable.red_s);
                        }else if (ratingStar.equals("SilverShooting")){
                            star.setImageResource(R.drawable.silver_s);
                        }else if (ratingStar.equals("Turquoise")){
                            star.setImageResource(R.drawable.turquoise_so);
                        }else if (ratingStar.equals("TurquoiseShooting")){
                            star.setImageResource(R.drawable.turquoise_s);
                        }else if (ratingStar.equals("Yellow")){
                            star.setImageResource(R.drawable.yellow_so);
                        }else if (ratingStar.equals("YellowShooting")){
                            star.setImageResource(R.drawable.yellow_s);
                        }
                    }catch (JSONException e){
                        star.setVisibility(View.GONE);
                    }
                } catch (JSONException e){
                    feedback_score.setVisibility(View.GONE);
                }

            } catch (JSONException e){
                LinearLayout sort = findViewById(R.id.soldBy);
                sort.setVisibility(View.GONE);
            }
            //==========================Shipping Info===============================
            TextView shippingCost = findViewById(R.id.shipping_cost);
            TextView shippingCost_txt = findViewById(R.id.shipping_cost_txt);
            if (shipping_txt.equals("Free Shipping") || shipping_txt.equals("0.0")) {
                shippingCost.setText("Free Shipping");
            } else if (shipping_txt.equals("N/A")){
                shippingCost.setVisibility(View.GONE);
                shippingCost_txt.setVisibility(View.GONE);
            } else {
                String cost = "$" + shipping_txt;
                shippingCost.setText(cost);
            }

            TextView gloabl = findViewById(R.id.shipping_global);
            TableRow gloablRow = findViewById(R.id.globalRow);
            try{
                String gloablString = temp.get("GlobalShipping").toString();
                gloabl.setText(gloablString);
            } catch (JSONException e){
                gloablRow.setVisibility(View.GONE);
            }

            TableRow handlingRow = findViewById(R.id.handlingRow);
            TextView handling = findViewById(R.id.shipping_handling);
            try{
                String handlingString = temp.get("HandlingTime").toString();
                if (handlingString.equals("1") || handlingString.equals("0")){
                    handlingString = handlingString + "day";
                } else {
                    handlingString = handlingString + "days";
                }
                handling.setText(handlingString);
            }catch (JSONException e){
                handlingRow.setVisibility(View.GONE);
            }

            TableRow conditionRow = findViewById(R.id.conditionRow);
            TextView condition = findViewById(R.id.shipping_condition);
            try{
                String conditionString = temp.get("ConditionDisplayName").toString();
                condition.setText(conditionString);
            } catch (JSONException e){
                conditionRow.setVisibility(View.GONE);
            }

            //==================Return Policy ========================================
            JSONObject returnPolicy = temp.getJSONObject("ReturnPolicy");
            TableRow policyRow = findViewById(R.id.policyRow);
            TableRow return_WithinRow = findViewById(R.id.return_withinRow);
            TableRow refundRow = findViewById(R.id.refundRow);
            TableRow shippingByRow = findViewById(R.id.shippingByRow);

            TextView return_policy = findViewById(R.id.return_policy);
            TextView return_Within = findViewById(R.id.return_within);
            TextView refund = findViewById(R.id.return_refund);
            TextView shippingBy = findViewById(R.id.return_shippingby);

            try{
                String returnAccepted = returnPolicy.get("ReturnsAccepted").toString();
                return_policy.setText(returnAccepted);
            }catch (JSONException e){
                policyRow.setVisibility(View.GONE);
            }

            try{
                String refundString = returnPolicy.get("Refund").toString();
                refund.setText(refundString);

            } catch (JSONException e){
                refundRow.setVisibility(View.GONE);
            }

            try{
                String returnWithin  = returnPolicy.get("ReturnsWithin").toString();
                return_Within.setText(returnWithin);
            } catch (JSONException e){
                return_WithinRow.setVisibility(View.GONE);
            }

            try{
                String shippedBy = returnPolicy.get("ShippingCostPaidBy").toString();
                shippingBy.setText(shippedBy);
            }catch (JSONException e){
                shippingByRow.setVisibility(View.GONE);
            }

            //================================== GOOGLE TAB ===================

            initGoogleImage();

            //================================== Seller TAB ===================

            initSimilarProduct();

    } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void initGoogleImage(){

        StringBuilder googleURL = new StringBuilder();
        googleURL.append(MY_SERVER_URL);

        try{
            googleURL.append("title=");
            googleURL.append(URLEncoder.encode(title, "utf-8"));
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        Log.i("GOOGLE TAB", googleURL.toString());

        String url = googleURL.toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    googleSearchResult(response);
                } catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressdialog.dismiss();
                Log.e("error", error.toString());
                showMessage("Error in Response");
            }
        });
        queue.add(stringRequest);

    }

    private void googleSearchResult(String response){

        try{
            obj = new JSONObject(response);
            JSONArray googleSearch_item = ((JSONObject) obj).getJSONArray("items");

            for (int i = 0; i < googleSearch_item.length(); i++){
                JSONObject item = googleSearch_item.getJSONObject(i);
                String link = item.get("link").toString();
                google_img.add(link);
            }


            LinearLayout detail = findViewById(R.id.detail_gallery);
            for(int i = 0; i < google_img.size(); i++){
                ImageView image = new ImageView(this);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(this).asBitmap().load(google_img.get(i)).override(1000, 1000).into(image);
                detail.addView(image);

            }


        } catch (JSONException e){
            e.printStackTrace();
        }


    }

    private void initSimilarProduct(){
        StringBuilder googleURL = new StringBuilder();
        googleURL.append(MY_SERVER_URL);

        Spinner similar_name = findViewById(R.id.similar_sort);
        similar_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(firstListener1) firstListener1 = false;
                else {
                    pointer1 = position;
                    Log.i("POSITION1", String.valueOf(pointer1));
                    sortReviews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner similar_ascending = findViewById(R.id.similar_ascending);
        similar_ascending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(firstListener2) firstListener2 = false;
                else {
                    pointer2 = position;
                    Log.i("POSITION2", String.valueOf(pointer2));
                    sortReviews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try{
            googleURL.append("itemId=");
            googleURL.append(URLEncoder.encode(receiveIDString, "utf-8"));
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        Log.i("SIMILAR TAB", googleURL.toString());

        String url = googleURL.toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finalcontent = response;
                try{
                    similarSearchResult(response);
                } catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                showMessage("Error in Response");
            }
        });
        queue.add(stringRequest);



    }

    private void similarSearchResult(String response){
        Log.i("SIMILAR", response);
        noresult = findViewById(R.id.similar_noresult);
        noresult.setVisibility(View.GONE);


        ArrayList<String> mImages = new ArrayList<>();
        ArrayList<String> mtitle = new ArrayList<>();
        ArrayList<String> mPrice = new ArrayList<>();
        ArrayList<String> mShipping = new ArrayList<>();
        ArrayList<String> mId = new ArrayList<>();
        ArrayList<String> mDays = new ArrayList<>();
        ArrayList<String> mURL = new ArrayList<>();

        Log.i("spinner", "here to spinner");


        try{
            obj = new JSONObject(finalcontent);
            Log.i("FINALCONTENT", finalcontent);
            JSONObject getSimilarResponse = ((JSONObject) obj).getJSONObject("getSimilarItemsResponse");
            JSONObject itemRecommendations = getSimilarResponse.getJSONObject("itemRecommendations");
            JSONArray itemArray = itemRecommendations.getJSONArray("item");

            for (int i = 0; i < itemArray.length(); i++){
                JSONObject item = itemArray.getJSONObject(i);
                String images = item.get("imageURL").toString();
                mImages.add(images);
                String itemId = item.get("itemId").toString();
                mId.add(itemId);
                String title = item.get("title").toString();
                mtitle.add(title);
                JSONObject shippingCost = item.getJSONObject("shippingCost");
                String shippingValue = shippingCost.get("__value__").toString();
                mShipping.add(shippingValue);
                JSONObject price = item.getJSONObject("buyItNowPrice");
                String priceValue = price.get("__value__").toString();
                mPrice.add(priceValue);
                String daysLeft = item.get("timeLeft").toString();
                daysLeft = daysLeft.substring(1, daysLeft.indexOf("D"));
                Log.i("DAYSLEFT", daysLeft);
                mDays.add(daysLeft);
                String url = item.get("viewItemURL").toString();
                mURL.add(url);
            }
            initSimilarView( mImages, mtitle, mPrice, mShipping, mId, mDays, mURL);

        } catch (JSONException e){
            noresult.setVisibility(View.VISIBLE);
        }

    }

    private void sortReviews() {

        ArrayList<String> mImages = new ArrayList<>();
        ArrayList<String> mtitle = new ArrayList<>();
        ArrayList<String> mPrice = new ArrayList<>();
        ArrayList<String> mShipping = new ArrayList<>();
        ArrayList<String> mId = new ArrayList<>();
        ArrayList<String> mDays = new ArrayList<>();
        ArrayList<String> mURL = new ArrayList<>();
        JSONArray itemArray;
        ArrayList<JSONObject> items = new ArrayList<JSONObject>();

        try{
            obj = new JSONObject(finalcontent);
            Log.i("FINALCONTENT", finalcontent);
            JSONObject getSimilarResponse = ((JSONObject) obj).getJSONObject("getSimilarItemsResponse");
            JSONObject itemRecommendations = getSimilarResponse.getJSONObject("itemRecommendations");
            itemArray = itemRecommendations.getJSONArray("item");
            for (int i = 0; i < itemArray.length(); i++){
                items.add(itemArray.getJSONObject(i));
            }
        } catch (JSONException e){
            noresult.setVisibility(View.VISIBLE);
        }

        Log.i("SORTED_REVIEW", "HERE IS THE SORTED REVIEW");
        if (pointer1 == 0){
            Log.i("sorted", "Default");
        } else if (pointer1 == 1){
            Collections.sort(items, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    String s1 = "";
                    String s2 = "";
                    try{
                        s1 = o1.get("title").toString();
                        s2 = o2.get("title").toString();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return s1.compareTo(s2);
                }
            });
            if (pointer2 == 1) {
                Collections.reverse(items);
            }
        }else if (pointer1 == 2){
            Collections.sort(items, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    Double priceValue1 = 0.0;
                    Double priceValue2 = 0.0;
                    try{
                        JSONObject price1 = o1.getJSONObject("buyItNowPrice");
                        priceValue1 = Double.parseDouble(price1.get("__value__").toString());
                        JSONObject price2 = o2.getJSONObject("buyItNowPrice");
                        priceValue2 = Double.parseDouble(price2.get("__value__").toString());

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return priceValue1.compareTo(priceValue2);
                }
            });

            if (pointer2 == 1){
                Collections.reverse(items);
            }
        }else if (pointer1 == 3){
            Collections.sort(items, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    Integer daysLeft1 = 0;
                    Integer daysLeft2 = 0;
                    try{
                        String daysLeft1String = o1.get("timeLeft").toString();
                        daysLeft1 = Integer.parseInt(daysLeft1String.substring(1, daysLeft1String.indexOf("D")));
                        String daysLeft2String = o2.get("timeLeft").toString();
                        daysLeft2 = Integer.parseInt(daysLeft2String.substring(1, daysLeft2String.indexOf("D")));

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return daysLeft1.compareTo(daysLeft2);
                }
            });
            if (pointer2 == 1){
                Collections.reverse(items);
            }
        }

        try{
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.get(i);
                String images = item.get("imageURL").toString();
                mImages.add(images);
                String itemId = item.get("itemId").toString();
                mId.add(itemId);
                String title = item.get("title").toString();
                Log.i("TITLE", title);
                mtitle.add(title);
                JSONObject shippingCost = item.getJSONObject("shippingCost");
                String shippingValue = shippingCost.get("__value__").toString();
                mShipping.add(shippingValue);
                JSONObject price = item.getJSONObject("buyItNowPrice");
                String priceValue = price.get("__value__").toString();
                mPrice.add(priceValue);
                String daysLeft = item.get("timeLeft").toString();
                daysLeft = daysLeft.substring(1, daysLeft.indexOf("D"));
                Log.i("DAYSLEFT", daysLeft);
                mDays.add(daysLeft);
                String url = item.get("viewItemURL").toString();
                mURL.add(url);

            }

            initSimilarView( mImages, mtitle, mPrice, mShipping, mId, mDays, mURL);

        } catch (JSONException e){
            e.printStackTrace();
        }


    }

    private void initSimilarView(ArrayList mImages, ArrayList mtitle, ArrayList mPrice,ArrayList mShipping, ArrayList mId,ArrayList mDays, ArrayList mURL) {
        Log.i("INITSIMILARVIEW", "come to here");
        RecyclerView recyclerView = findViewById(R.id.similar_recyclerView);
        similar_RecyclerViewAdapter adapter = new similar_RecyclerViewAdapter(this, mImages, mtitle, mPrice, mShipping, mId, mDays, mURL);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
    }


    public void gotoStore(View view) {
        Uri uri = Uri.parse(storeURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void facebook(View view) {

        try {
            String naturalSearch = temp.get("ViewItemURLForNaturalSearch").toString();
            String fburl ="https://www.facebook.com/sharer/sharer.php?u=" + naturalSearch + "&quote=";

            try{
                String query = URLEncoder.encode("Buy " + title + " at " + currentPrice + " from Ebay!", "utf-8");
                fburl = fburl + query + "&hashtag=%23CSCI571Spring2019Ebay";
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            Uri uri = Uri.parse(fburl);
            Log.i("NATURAL SEARCH", uri.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private View getMainTitleTabView(String receiveTitleString, String receiveIDString) {

        View view1 = LayoutInflater.from(this).inflate(R.layout.title_bar, null);

        TextView title = (TextView) view1.findViewById(R.id.title_txt);
        ImageView img = (ImageView) view1.findViewById(R.id.img_bar);
        title.setText(receiveTitleString);
        img.setImageResource(R.drawable.facebook);

        return view1;

    }


    private void initViews(String receiveTitleString) {

        getSupportActionBar().setTitle("");
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);

        getSupportActionBar().setCustomView(getMainTitleTabView(receiveTitleString, receiveIDString));
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        tabLayout     = (TabLayout) findViewById(R.id.tablayout_detail);
        tab_viewpager = (ViewPager) findViewById(R.id.tab_viewpager_detail);

        mFragments = new ArrayList<Fragment>();
        iconList   = new ArrayList<>();
        tabList    = new ArrayList<>();

        tabList.add("PRODUCT");
        tabList.add("SHIPPING");
        tabList.add("PHOTOS");
        tabList.add("SIMILAR");



        mFragments.add(detail_product);
        mFragments.add(detail_shipping);
        mFragments.add(detail_photos);
        mFragments.add(detail_similar);

        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(3)));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        iconList.add(R.drawable.product_tab);
        iconList.add(R.drawable.shipping_tab);
        iconList.add(R.drawable.photo_tab);
        iconList.add(R.drawable.similar_tab);

        tab_viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), tabList, mFragments, iconList));
        tabLayout.setupWithViewPager(tab_viewpager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_viewpager.setCurrentItem(tab.getPosition());
                showMessage("OnTab" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_viewpager.setOffscreenPageLimit(4);

        tabLayout.getTabAt(0).setCustomView(getTabView(0));
        tabLayout.getTabAt(1).setCustomView(getTabView(1));
        tabLayout.getTabAt(2).setCustomView(getTabView(2));
        tabLayout.getTabAt(3).setCustomView(getTabView(3));


    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(tabList.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(iconList.get(position));
        return view;
    }

    private void showMessage(String txt){
        Toast toast = Toast.makeText(this, txt, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
    }
}
