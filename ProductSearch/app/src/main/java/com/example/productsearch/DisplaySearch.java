package com.example.productsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplaySearch extends AppCompatActivity {
    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;
    private String responseContent;
    private static String thisurl = "";
    private static int temp_size;
    private static String keywordString;
    private static String thisKeyword;

    private JSONObject obj;

    private ArrayList<String> mImageName = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mPostalCode = new ArrayList<>();
    private ArrayList<String> mCondition = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mWish  = new ArrayList<>();
    private ArrayList<String> mShipping = new ArrayList<>();
    private ArrayList<String> mId = new ArrayList<>();


    private JSONArray temp;
    private JSONArray title;

    private LinearLayout progressBar;
    private LinearLayout mainContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaysearch);
        mInflater = LayoutInflater.from(this);
        progressBar = findViewById(R.id.progressbar_search);
        mainContent = findViewById(R.id.mainContent);
        progressBar.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);

        initData();

        initView();

    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        keywordString = intent.getStringExtra(MainActivity.KEYWORD);

        if (url == null){
            url = thisurl;
        }

        thisurl = url;

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        SearchResult(response);
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());

                }
            });
            queue.add(stringRequest);
        }



    private void SearchResult(String responseContent) throws JSONException {
        progressBar.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);

        LinearLayout noresult = findViewById(R.id.display_noresult);
        noresult.setVisibility(View.GONE);


        if (keywordString == null){
            keywordString = thisKeyword;
        }
        thisKeyword = keywordString;

        Log.i("DisplaySearch", responseContent + "...." + keywordString);

        TextView showName = (TextView) findViewById(R.id.showingName);
        TextView showSize = findViewById(R.id.showingSize);

        try {
            obj = new JSONObject(responseContent);
            temp = obj.getJSONArray("findItemsAdvancedResponse");
            JSONObject temp1 = temp.getJSONObject(0);
            try{
                JSONArray searchResultArray = temp1.getJSONArray("searchResult");

                temp1 = searchResultArray.getJSONObject(0);
                JSONArray searchResultItemArray = temp1.getJSONArray("item");
                Log.i("info", searchResultItemArray.toString());


                if (searchResultItemArray.length() > 50){
                    temp_size = 50;
                } else{
                    temp_size = searchResultItemArray.length();
                }

                showName.setText(keywordString);
                showSize.setText(String.valueOf(temp_size));


                for (int i = 0; i < temp_size; i++){
                    JSONObject details = searchResultItemArray.getJSONObject(i);
                    JSONArray itemId = details.getJSONArray("itemId");
                    mId.add(itemId.get(0).toString());

                    title = details.getJSONArray("title");
                    mImageName.add(title.get(0).toString());

                    try{
                        JSONArray photos = details.getJSONArray("galleryURL");
                        mImages.add(photos.get(0).toString());
                    } catch (JSONException e){
                        mImages.add("");
                    }

                    try{
                        JSONArray zipcode = details.getJSONArray("postalCode");
                        mPostalCode.add(zipcode.get(0).toString());
                    } catch (JSONException e){
                        mPostalCode.add("N/A");
                    }

                    try{
                        JSONArray shippingcost = details.getJSONArray("shippingInfo");
                        JSONObject shippingcostObj = shippingcost.getJSONObject(0);
                        shippingcost = shippingcostObj.getJSONArray("shippingServiceCost");
                        shippingcostObj = shippingcost.getJSONObject(0);
                        mShipping.add(shippingcostObj.get("__value__").toString());
                    } catch (JSONException e){
                        mShipping.add("N/A");
                    }

                    try{
                        JSONArray condition = details.getJSONArray("condition");
                        JSONObject conditionObj = condition.getJSONObject(0);
                        condition = conditionObj.getJSONArray("conditionDisplayName");
                        mCondition.add(condition.get(0).toString());
                    } catch (JSONException e) {
                        mCondition.add("N/A");
                    }

                    mWish.add("false");

                    JSONArray price = details.getJSONArray("sellingStatus");
                    JSONObject priceObj = price.getJSONObject(0);
                    price = priceObj.getJSONArray("currentPrice");
                    priceObj = price.getJSONObject(0);
                    mPrice.add(priceObj.get("__value__").toString());

                }
            } catch (JSONException e){
                e.printStackTrace();
                noresult.setVisibility(View.VISIBLE);
                LinearLayout showing = findViewById(R.id.display_result);
                showing.setVisibility(View.GONE);
                RecyclerView table = findViewById(R.id.recyclerView);
                table.setVisibility(View.GONE);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }


    private void initView() {

        Log.i("initView", "Here");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mId, mImageName, mImages, mPostalCode, mCondition, mPrice, mWish, mShipping);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);


    }


}