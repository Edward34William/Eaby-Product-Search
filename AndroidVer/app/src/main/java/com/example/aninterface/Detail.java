package com.example.aninterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import static android.util.Log.d;

public class Detail extends AppCompatActivity {
    private final int[] ICONS = new int[]{
            R.drawable.product_info,
            R.drawable.shipping,
            R.drawable.google,
            R.drawable.equal
    };
    private final int[] UICONS = new int[]{
            R.drawable.product_info_2,
            R.drawable.shipping_2,
            R.drawable.google_2,
            R.drawable.equal_2
    };

    private String url;
    private boolean isWish;

    private String itemId;

    private fragAdapterDetail fragAdapter;
    private ActionBar actionBar;
    private ArrayList<resultItems> myWishItems = new ArrayList<>();
    private resultItems item;
    private RequestQueue myRequestQ;
    private String Domain;

    private String viewItemURLForNaturalSearch = "http://www.ebay.com";
    private double price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        Domain = MainActivity.getInstance().getDomain();
        myWishItems = MainActivity.getInstance().getMyWishItems();
        item = MainActivity.getInstance().readItem(Detail.this);
        itemId = item.getItemId();

        myRequestQ = Volley.newRequestQueue(Detail.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViewById(R.id.detailViewpager).setVisibility(View.GONE);
        findViewById(R.id.progressBar_cyclic).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar_textView).setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(item.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detailTabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.detailViewpager);
        tabLayout.setupWithViewPager(mViewPager);


        //resultItems item = MainActivity.getInstance().readItem(Detail.this);
        fragAdapter = new fragAdapterDetail(getSupportFragmentManager(), item);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(fragAdapter);


        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(UICONS[1]);
        tabLayout.getTabAt(2).setIcon(UICONS[2]);
        tabLayout.getTabAt(3).setIcon(UICONS[3]);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(ICONS[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(UICONS[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        removePrefs();
        loadDetailJson(MainActivity.getInstance().readItem(Detail.this));

    }

    public void loadSimilarJson(final resultItems item) {
        String url = Domain + "api/android/similar/?itemId=" + item.getItemId();
        VolleyLog.DEBUG = true;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<simItem> simItemArrayList = new ArrayList<>();
                        for(int i = 0; i<response.length(); i++){
                            String image = null;
                            try {
                                image = response.getJSONObject(i).getString("Image");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String title = null;
                            try {
                                title = response.getJSONObject(i).getString("Title");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            double price = 0;
                            try {
                                price = response.getJSONObject(i).getDouble("Price");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            double shipping = 0;
                            try {
                                shipping = response.getJSONObject(i).getDouble("Shipping");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int daysleft = 0;
                            try {
                                daysleft = response.getJSONObject(i).getInt("DaysLeft");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String _viewItemURL = null;
                            try {
                                _viewItemURL = response.getJSONObject(i).getString("_viewItemURL");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            simItemArrayList.add(new simItem(image, title,_viewItemURL, daysleft, shipping, price));
                        }

                        MainActivity.getInstance().saveSim(Detail.this, simItemArrayList);
                        //d("simItemArrayList", simItemArrayList.toString());
                        afterLoad(item);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Detail.this, "No network", Toast.LENGTH_SHORT).show();
            }
        });

        myRequestQ.add(req);


    }

    public void loadGoogleJson(final resultItems item) {
        String url = null;
        try {
            if(item.getTitle().length() > 35){
                url = Domain + "api/android/google-img/?productTitle=" + URLEncoder.encode(item.getTitle().substring(0,30), "UTF-8")+ "&v=" + 1;
            }else{
                url = Domain + "api/android/google-img/?productTitle=" + URLEncoder.encode(item.getTitle(), "UTF-8")+ "&v=" + 1;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //final String url = "https://api.myjson.com/bins/n1rfg";

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //d("googleUrl", url);
                        try {

                            ArrayList<String> googleUrlArray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                String googleUrl = response.get(i).toString();
                                googleUrlArray.add(googleUrl);
                                d("googleUrl", googleUrl);
                            }

                            MainActivity.getInstance().saveStringArrayList(Detail.this, googleUrlArray, "Google");

                            loadSimilarJson(item);
                            //afterLoad(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Detail.this, "No network", Toast.LENGTH_SHORT).show();
            }
        });

        myRequestQ.add(req);

    }

    public void loadDetailJson(final resultItems item) {
        final String url = Domain + "api/android/item-detail/?itemId=" + item.getItemId();
        //final String url = "https://api.myjson.com/bins/8pa2c";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            try {
                                viewItemURLForNaturalSearch = response.getString("viewItemURLForNaturalSearch");
                                //MainActivity.getInstance().saveString(Detail.this, response.getString("viewItemURLForNaturalSearch"), "viewItemURLForNaturalSearch");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONObject highlights = response.getJSONObject("Highlights");

                            try {
                                JSONArray Images = highlights.getJSONArray("ProductImages");
                                ArrayList<String> imagesArray = new ArrayList<>();
                                for (int i = 0; i < Images.length(); i++) {
                                    String image = Images.get(i).toString();
                                    imagesArray.add(image);
                                    d("ProductImages", image);
                                }
                                MainActivity.getInstance().saveStringArrayList(Detail.this, imagesArray, "ProductImages");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                MainActivity.getInstance().saveString(Detail.this, highlights.getString("Subtitle"), "Subtitle");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                price = highlights.getDouble("Price");
                                MainActivity.getInstance().saveString(Detail.this, String.format("%.2f", price), "Price");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, highlights.getString("Brand"), "Brand");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray specifications = response.getJSONArray("Specifications");

                                ArrayList<String> specArray = new ArrayList<>();
                                for (int i = 0; i < specifications.length(); i++) {
                                    String spec = specifications.get(i).toString();
                                    specArray.add(spec);

                                }
                                MainActivity.getInstance().saveStringArrayList(Detail.this, specArray, "Specifications");
                                d("specifications", specArray.toString());
                                //item.setGalleyPhotos(specArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //SoldBy
                            JSONObject soldBy = response.getJSONObject("SoldBy");
                            try {
                                MainActivity.getInstance().saveString(Detail.this, soldBy.getString("StoreName"), "StoreName");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, soldBy.getString("StoreURL"), "StoreURL");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, soldBy.getString("FeedbackScore"), "FeedbackScore");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, soldBy.getDouble("Popularity") + "", "Popularity");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, soldBy.getString("FeedbackStar"), "FeedbackStar");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//
                            //Return
                            JSONObject returnPolicy = response.getJSONObject("ReturnPolicy");
                            try {
                                MainActivity.getInstance().saveString(Detail.this, returnPolicy.getString("ShippedBy"), "ShippedBy");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, returnPolicy.getString("RefundMode"), "RefundMode");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, returnPolicy.getString("ReturnsWithin"), "ReturnsWithin");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, returnPolicy.getString("Policy"), "Policy");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Return
                            try {
                                MainActivity.getInstance().saveString(Detail.this, response.getJSONObject("ShippingInfo").getString("ConditionDescription"), "ConditionDescription");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, response.getJSONObject("ShippingInfo").getString("GlobalShipping"), "GlobalShipping");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                MainActivity.getInstance().saveString(Detail.this, response.getJSONObject("ShippingInfo").getString("HandlingTime"), "HandlingTime");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //afterLoad(item);
                            loadGoogleJson(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Detail.this, "No network", Toast.LENGTH_SHORT).show();
            }
        });

        myRequestQ.add(req);

    }

    public void removePrefs(){
//        MainActivity.getInstance().saveString(Detail.this, "", "viewItemURLForNaturalSearch");

        MainActivity.getInstance().saveString(Detail.this, "", "Subtitle");
        MainActivity.getInstance().saveString(Detail.this, "", "Price");
        MainActivity.getInstance().saveString(Detail.this, "", "Brand");

        MainActivity.getInstance().saveString(Detail.this, "", "ConditionDescription");
        MainActivity.getInstance().saveString(Detail.this, "", "GlobalShipping");
        MainActivity.getInstance().saveString(Detail.this, "", "HandlingTime");
        MainActivity.getInstance().saveString(Detail.this, "", "StoreName");
        MainActivity.getInstance().saveString(Detail.this, "", "StoreURL");
        MainActivity.getInstance().saveString(Detail.this, "", "FeedbackScore");
        MainActivity.getInstance().saveString(Detail.this, "", "Popularity");
        MainActivity.getInstance().saveString(Detail.this, "", "FeedbackStar");

        MainActivity.getInstance().saveString(Detail.this, "", "ShippedBy");
        MainActivity.getInstance().saveString(Detail.this, "", "RefundMode");
        MainActivity.getInstance().saveString(Detail.this, "", "ReturnsWithin");
        MainActivity.getInstance().saveString(Detail.this, "", "Policy");

        MainActivity.getInstance().saveStringArrayList(Detail.this, new ArrayList<String>(), "ProductImages");
        MainActivity.getInstance().saveStringArrayList(Detail.this, new ArrayList<String>(), "Specifications");
        MainActivity.getInstance().saveStringArrayList(Detail.this, new ArrayList<String>(), "Google");

        MainActivity.getInstance().saveSim(Detail.this, new ArrayList<simItem>());

    }

    public void afterLoad(final resultItems item) {

        //Intent intent = getIntent();
        final String itemId = item.getItemId();
        String title = item.getTitle();
        isWish = item.isWish();

        final FloatingActionButton fab = findViewById(R.id.fab);
        if (!isWish) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cart_plus_fab));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.cart_remove_fab));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isWish) { //add
                    fab.setImageDrawable(ContextCompat.getDrawable(Detail.this, R.drawable.cart_remove_fab));
                    isWish = !isWish;
                    item.setWish(true);
                    myWishItems.add(item);

                    MainActivity.getInstance().setMyWishItems(myWishItems, Detail.this);
                    Toast.makeText(Detail.this, "Add " + item.getTitle(), Toast.LENGTH_SHORT).show();
                } else { //remove
                    fab.setImageDrawable(ContextCompat.getDrawable(Detail.this, R.drawable.cart_plus_fab));
                    isWish = !isWish;

                    Iterator<resultItems> itr = myWishItems.iterator();
                    while (itr.hasNext()) {
                        resultItems rs = itr.next();
                        if (rs.getItemId().equals(itemId)) {
                            //Toast.makeText(Detail.this, "Remove " + rs.getTitle(), Toast.LENGTH_SHORT).show();
                            itr.remove();
                        }
                    }

                    MainActivity.getInstance().setMyWishItems(myWishItems, Detail.this);
                    Toast.makeText(Detail.this, "Remove " + item.getTitle(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        findViewById(R.id.detailViewpager).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar_cyclic).setVisibility(View.GONE);
        findViewById(R.id.progressBar_textView).setVisibility(View.GONE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    public void fbOnclick(View view){
        try {

            String quote = "Buy " + item.getTitle() + " for $" + price + " from Ebay!" ;
            String fbHref = "https://www.facebook.com/dialog/share?app_id=377233376198486&display=popup"
                    + "&quote="+URLEncoder.encode(quote, "UTF-8") + "&hashtag=%23CSCI571Spring2019Ebay"
                    + "&href="+ URLEncoder.encode(viewItemURLForNaturalSearch, "UTF-8")
                    +"&redirect_uri=" + URLEncoder.encode("http://localhost", "UTF-8");

            d("fb link", fbHref);
            Uri uri = Uri.parse(fbHref);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}
