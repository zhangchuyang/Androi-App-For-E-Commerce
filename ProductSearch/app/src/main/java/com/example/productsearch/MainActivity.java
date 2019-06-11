package com.example.productsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private TabLayout tabLayout;
    private ViewPager tab_viewpager;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String KEYWORD = "keyword";

    //    private String MY_SERVER_URL = "http://www.chuyang-571.us-east-2.elasticbeanstalk.com/user?";
//    private String MY_SERVER_URL = "http://192.168.70.248:8081/user?";
//    private String MY_SERVER_URL = "http://10.26.2.13:3000/user?";

    private String MY_SERVER_URL = "http://www.ProductSearch2.us-east-2.elasticbeanstalk.com/user?";

    private String finalcontent;


    private ArrayList<String> tabList = new ArrayList<String>();

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private FragmentSearch fragmentSearch;
    private RequestQueue zipcode;
    public static final String ERROR_MESSAGE = "Please fix all fields with errors";
    public String zip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvent();

        zipcode = Volley.newRequestQueue(this);

        String url = "http://ip-api.com/json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    zip = response.getString("zip");
                    showMessage(zip);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        zipcode.add(request);
    }

    private void initViews(){
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tab_viewpager = (ViewPager) findViewById(R.id.tab_viewpager);

        tabList = new ArrayList<String>();
//        iconList = new ArrayList<>();
        tabList.add("SEARCH");
        tabList.add("WISH LIST");

        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(1)));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        FragmentSearch fragmentSearch = new FragmentSearch();
        FragmentWish fragmentWish = new FragmentWish();
        mFragments.add(fragmentSearch);
        mFragments.add(fragmentWish);

        List<Integer> iconList = new ArrayList<>();
        tab_viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), tabList, mFragments, iconList));
        tabLayout.setupWithViewPager(tab_viewpager);

//        tabLayout.getTabAt(0).setCustomView(getTabView(0));
//        tabLayout.getTabAt(1).setCustomView(getTabView(1));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == 0)showMessage("0!");
        else showMessage("1");
    }

    private void showMessage(String txt){
        Toast toast = Toast.makeText(this, txt, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
    }


    private void initEvent() {
        showMessage("initEvent!");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_viewpager.setCurrentItem(tab.getPosition());
                showMessage("onTabSelected!" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //showMessage("onTabUnselected!");

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //showMessage("onTabReselected!");

            }
        });
    }


    public void mainSearch(View view) {
        boolean avl = true;
        EditText keyword = (EditText) findViewById(R.id.keyword);
        String keywordString = keyword.getText().toString();
        TextView error1 = (TextView) findViewById(R.id.errormessage1);
        TextView error2 = (TextView) findViewById(R.id.errormessage2);
        AutoCompleteTextView zip_view = findViewById(R.id.zipcode);


        if (keywordString.length() == 0) {
            error1.setVisibility(View.VISIBLE);
            showMessage(ERROR_MESSAGE);
            avl = false;
            //return;
        }
        Spinner category = (Spinner) findViewById(R.id.category);
        String categoryString = category.getSelectedItem().toString().toLowerCase();

        CheckBox new_check = (CheckBox) findViewById(R.id.new_check);
        String newCond = "";
        if (new_check.isChecked()) newCond = "true";
        else newCond = "false";
        String usedCond = "";
        CheckBox used_check = (CheckBox) findViewById(R.id.used);
        if (used_check.isChecked()) usedCond = "true";
        CheckBox unspecified_check = (CheckBox) findViewById(R.id.unspecified);
        String unspcifiedCond = "";
        if (unspecified_check.isChecked()) unspcifiedCond = "true";

        CheckBox free = (CheckBox) findViewById(R.id.freeshipping);
        String freeShipping = "";
        if (free.isChecked()) freeShipping = "true";

        CheckBox pickup = (CheckBox) findViewById(R.id.pickup);
        String pickupShipping = "";
        if (pickup.isChecked()) pickupShipping = "true";

        CheckBox nearby = (CheckBox) findViewById(R.id.nearbysearch);


        String zipcode = "";
        String distanceString = "";
        String radioString = "";

        if (nearby.isChecked()) {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            radioString = radioButton.getText().toString();

            EditText distance = (EditText) findViewById(R.id.milesfrom);
            distanceString = distance.getText().toString();
            if (distanceString.length() == 0) {
                distanceString = "10";
            }
            if (radioString.equals("Current Location")) {
                radioString = "option1";
                zipcode = zip;
            } else {
                radioString = "option2";

                zipcode = zip_view.getText().toString();
                if (zipcode.length() == 0) {
                    error2.setVisibility(View.VISIBLE);
                    showMessage(ERROR_MESSAGE);
                    avl = false;
                    return;
                }
            }
        } else {
            radioString = "option3";
        }

        if (!avl) return;

        StringBuilder awsURL = new StringBuilder();
        awsURL.append(MY_SERVER_URL);

        try {
            awsURL.append("placechosen=");
            awsURL.append(URLEncoder.encode(radioString, "utf-8"));
            awsURL.append("&placekeyword=");
            awsURL.append(URLEncoder.encode(keywordString, "utf-8"));
            awsURL.append("&placecategory=");
            awsURL.append(URLEncoder.encode(categoryString, "utf-8"));
            if (radioString.equals("option3")) {

            } else {
                awsURL.append("&placelocation=");
                awsURL.append(URLEncoder.encode(zipcode, "utf-8"));

                awsURL.append("&placeDistance=");
                awsURL.append(URLEncoder.encode(distanceString, "utf-8"));
            }

            if (usedCond.equals("true")) {
                awsURL.append("&used_check=");
                awsURL.append(URLEncoder.encode(usedCond, "utf-8"));
            }
            if (newCond.equals("true")) {
                awsURL.append("&new_check=");
                awsURL.append(URLEncoder.encode(newCond, "utf-8"));
            }
            if (unspcifiedCond.equals("true")) {
                awsURL.append("&unspecified_check=");
                awsURL.append(URLEncoder.encode(unspcifiedCond, "utf-8"));
            }
            if (pickupShipping.equals("true")) {
                awsURL.append("&pickup_check=");
                awsURL.append(URLEncoder.encode(pickupShipping, "utf-8"));
            }
            if (freeShipping.equals("true")) {
                awsURL.append("&free_check=");
                awsURL.append(URLEncoder.encode(freeShipping, "utf-8"));
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Log.i("tag", "awsURL = " + awsURL.toString());
        showMessage("awsURL = " + awsURL.toString());
        String url = awsURL.toString();

        Intent intent = new Intent(this, DisplaySearch.class);
        intent.putExtra(EXTRA_MESSAGE, url);
        intent.putExtra(KEYWORD, keywordString);
        startActivity(intent);
    }


    public void clearSearch(View view){
        TextView error1 = (TextView) findViewById(R.id.errormessage1);
        TextView error2 = (TextView) findViewById(R.id.errormessage2);
        error1.setVisibility(View.GONE);
        error2.setVisibility(View.GONE);
        EditText keyword = (EditText) findViewById(R.id.keyword);
        keyword.setText("");
        Spinner category = (Spinner) findViewById(R.id.category);
        category.setSelection(0, true);
        CheckBox nearby1 = (CheckBox) findViewById(R.id.nearbysearch);
        nearby1.setChecked(false);
        RadioButton r = (RadioButton) findViewById(R.id.currentlocationradio);
        r.setChecked(true);
        CheckBox new1 = (CheckBox) findViewById(R.id.new_check);
        new1.setChecked(false);
        CheckBox used1 = (CheckBox) findViewById(R.id.used);
        used1.setChecked(false);
        CheckBox unspecified1 = (CheckBox) findViewById(R.id.unspecified);
        unspecified1.setChecked(false);
        CheckBox free1 = (CheckBox) findViewById(R.id.freeshipping);
        free1.setChecked(false);
        CheckBox pickup1 = (CheckBox) findViewById(R.id.pickup);
        pickup1.setChecked(false);
        EditText distance1 = (EditText) findViewById(R.id.milesfrom);
        distance1.setText("");
        AutoCompleteTextView textView = findViewById(R.id.zipcode);
        textView.setEnabled(false);

    }

}

