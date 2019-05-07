package com.example.aninterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import static android.util.Log.d;

public class Result extends AppCompatActivity {

    private RecyclerView myRecycler;
    private resultItemAdapter myRecyclerAdapter;
    private ArrayList<resultItems> myResultItems = new ArrayList<>();
    private ArrayList<resultItems> myWishItems = new ArrayList<>();
    private RequestQueue myRequestQ;

    private NestedScrollView sv;
    private TextView progressBar_text;
    private ProgressBar progressBar_cyclic;
    private ConstraintLayout resultConstraintLayout;
    private TextView resultCount;
    private TextView resultKeywordTextView;

    private String resultKeyword;
    private boolean resultCondition;
    private String resultBuyerPostalCode;
    private String resultMaxDistance;
    private boolean resultConditionNew;
    private boolean resultConditionUsed;
    private boolean resultConditionUnsp;
    private String resultCategoryId;
    private boolean resultLocalpickup;
    private boolean resultFreeshipping;

    private TextView noResultsText;
    private String Domain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Domain = MainActivity.getInstance().getDomain();
        myWishItems = MainActivity.getInstance().getMyWishItems();

        setContentView(R.layout.activity_result);

        sv = findViewById(R.id.resultScrollView);
        progressBar_text = findViewById(R.id.progressBar_text);
        progressBar_cyclic = findViewById(R.id.progressBar_cyclic);
        resultConstraintLayout = findViewById(R.id.resultConstraintLayout);
        resultCount = findViewById(R.id.resultCount);
        resultKeywordTextView = findViewById(R.id.resultKeyword);
        noResultsText = findViewById(R.id.noResultsText);

        progressBar_text.setVisibility(View.VISIBLE);
        progressBar_cyclic.setVisibility(View.VISIBLE);
        sv.setVisibility(View.GONE);
        resultConstraintLayout.setVisibility(View.GONE);
        noResultsText.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        resultKeyword = intent.getStringExtra("keyword");
        resultBuyerPostalCode = intent.getStringExtra("buyerPostalCode");
        resultMaxDistance = intent.getStringExtra("MaxDistance");
        resultCondition = intent.getBooleanExtra("isCondition", false);
        resultConditionNew = intent.getBooleanExtra("conditionNew", false);
        resultConditionUsed = intent.getBooleanExtra("conditionUsed", false);
        resultConditionUnsp = intent.getBooleanExtra("conditionUnsp", false);
        resultCategoryId = intent.getStringExtra("categoryId");
        resultLocalpickup = intent.getBooleanExtra("isLocalpickup", false);
        resultFreeshipping = intent.getBooleanExtra("isFreeshipping", false);

        resultKeywordTextView.setText(resultKeyword);

        myRecycler = findViewById(R.id.resultRecyclerView);
        myRecycler.setHasFixedSize(true);
        myRecycler.setLayoutManager(new GridLayoutManager(Result.this, 2)); //getApplicationContext()

        getJsonResponseSearch();
    }

    @Override
    public void onStart() {
        super.onStart();
        replaceWish();
        try {
            myRecyclerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    public void getJsonResponseSearch() {
        String url;
        url = Domain + "api/android/search/?";
        if(!resultKeyword.isEmpty()){
            url+="keyword=" + resultKeyword + "&";
        }

        if(!resultBuyerPostalCode.isEmpty()){
            url+="buyerPostalCode=" + resultBuyerPostalCode + "&";
            url+="MaxDistance=" + resultMaxDistance + "&";
        }

        if(resultCondition){
            url+="Condition=true&";
            if(resultConditionNew){
                url+="New=true&";
            }
            if(resultConditionUsed){
                url+="Used=true&";
            }
            if(resultConditionUnsp){
                url+="Unspecified=true&";
            }
        }

        if(resultLocalpickup){
            url+="LocalPickupOnly=true&";
        }

        if(resultFreeshipping){
            url+="FreeShippingOnly=true&";
        }

        if(!resultCategoryId.isEmpty()){
            if(!resultCategoryId.equals("-1")){
                url+="categoryId="+ resultCategoryId +"&";
            }
        }

        VolleyLog.DEBUG = true;
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();



        myRequestQ = Volley.newRequestQueue(Result.this);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject searchItem = response.getJSONObject(i);

                                //id = jsonObject.getString("id");

                                String image = searchItem.getString("image");
                                String title = searchItem.getString("title");
                                String price = searchItem.getString("price");
                                String shipping = searchItem.getString("shipping");
                                //shipping = jsonObject.getString("shipping");

                                String condition = searchItem.getString("condition");

                                String zipcode = "Zip: " + searchItem.getString("zip");
                                String itemId = searchItem.getString("itemId");
                                boolean isWish = false;

                                //replace isWish
                                Iterator<resultItems> itr = myWishItems.iterator();

                                while (itr.hasNext()) {
                                    resultItems rs = itr.next();
                                    if (rs.getItemId().equals(itemId)) {
                                        isWish = true;
                                        break;
                                    }
                                }

//                                //ShippingInfo
                                String GobalShipping = null;
                                try {
                                    GobalShipping = searchItem.getJSONObject("ShippingInfo").getString("GobalShipping");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String handlingTime = null;
                                try {
                                    handlingTime = searchItem.getJSONObject("ShippingInfo").getString("handlingTime");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //sold by
                                String storeURL = null;
                                try {
                                    storeURL = searchItem.getJSONObject("SoldBy").getString("storeURL");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String storeName = null;
                                try {
                                    storeName = searchItem.getJSONObject("SoldBy").getString("storeName");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                int feedbackScore = -1;
                                try {
                                    feedbackScore = searchItem.getJSONObject("SoldBy").getInt("feedbackScore");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                int positiveFeedbackPercent = 0;
                                try {
                                    positiveFeedbackPercent = searchItem.getJSONObject("SoldBy").getInt("positiveFeedbackPercent");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String feedbackRatingStar = null;
                                try {
                                    feedbackRatingStar = searchItem.getJSONObject("SoldBy").getString("feedbackRatingStar");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                myResultItems.add(new resultItems(image, title, zipcode, shipping, condition, price, isWish, itemId, GobalShipping, handlingTime, storeURL, storeName, feedbackScore, positiveFeedbackPercent, feedbackRatingStar));
                                //myResultItems.add(new resultItems(searchItem));
                            }

                            myRecyclerAdapter = new resultItemAdapter(Result.this, myResultItems);
                            myRecycler.setAdapter(myRecyclerAdapter);

                            myRecyclerAdapter.setOnItemClickListener(new resultItemAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                    load(myResultItems.get(position));

                                }

                                @Override
                                public void onAddCartClick(int position) {

                                    myResultItems.get(position).setWish(true);
                                    myWishItems.add(myResultItems.get(position));
                                    MainActivity.getInstance().setMyWishItems(myWishItems, Result.this);

                                    Toast.makeText(Result.this,"Add " + myResultItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                                    d("current isWish", myResultItems.get(position).isWish()+"");
                                }

                                @Override
                                public void onRemoveCartClick(int position) {

                                    Iterator<resultItems> itr = myWishItems.iterator();
                                    while (itr.hasNext()) {
                                        resultItems rs = itr.next();
                                        if (rs.getItemId().equals(myResultItems.get(position).getItemId())) {
                                            itr.remove();
                                        }
                                    }
                                    myResultItems.get(position).setWish(false);
                                    MainActivity.getInstance().setMyWishItems(myWishItems, Result.this);


                                    Toast.makeText(Result.this,"Remove " + myResultItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                                    d("current isWish", myResultItems.get(position).isWish()+"");

                                }
                            });


                            if(response.length() != 0){
                                resultCount.setText(response.length()+"");

                                progressBar_text.setVisibility(View.GONE);
                                progressBar_cyclic.setVisibility(View.GONE);
                                sv.setVisibility(View.VISIBLE);
                                resultConstraintLayout.setVisibility(View.VISIBLE);

                            }else{
                                progressBar_text.setVisibility(View.GONE);
                                progressBar_cyclic.setVisibility(View.GONE);
                                noResultsText.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Result.this, "No network", Toast.LENGTH_SHORT).show();
            }
        });

        myRequestQ.add(req);
    }

    public void replaceWish(){
        myWishItems = MainActivity.getInstance().getMyWishItems();
        for (int i = 0; i < myResultItems.size(); i++) {
            Iterator<resultItems> itr = myWishItems.iterator();
            myResultItems.get(i).setWish(false);
            while (itr.hasNext()) {
                resultItems rs = itr.next();
                if (rs.getItemId().equals(myResultItems.get(i).getItemId())) {
                    myResultItems.get(i).setWish(true);
                }
            }
        }
    }

    public void load(final resultItems item) {
        MainActivity.getInstance().saveItem(Result.this, item);
        Intent myIntent = new Intent(Result.this, Detail.class);
        //myIntent.putExtra("isWish", current.isWish());
        Result.this.startActivity(myIntent);

    }




}
