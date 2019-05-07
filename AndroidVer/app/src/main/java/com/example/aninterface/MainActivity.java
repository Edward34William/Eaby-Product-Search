package com.example.aninterface;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private fragAdapter fragAdapter;
    private TabLayout tabLayout;

    private Spinner spin;

    private CheckBox check_new;
    private CheckBox check_used;
    private CheckBox check_unsp;

    private CheckBox check_local;
    private CheckBox check_free;

    private TextInputLayout keyWordLayout;
    private TextInputLayout inputLayoutZipcode;
    private AutoCompleteTextView editTextZipcode;
    private EditText editTextDistance;
    private CheckBox check_nearby;

    private RadioButton currentRadio;

    private RequestQueue mRequestQueue;
    private static DataHolder holder;
    private RequestQueue myRequestQ;
    private boolean isGetIp = false;
    private String localZipCode;
    private String cat_key = "-1";


    public void loadIpJson() {
        String url = "http://ip-api.com/json";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            localZipCode = response.getString("zip");
                            isGetIp = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            isGetIp = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        myRequestQ.add(req);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRequestQ = Volley.newRequestQueue(this);
        loadIpJson();
        holder = new DataHolder(this);

        viewPager = findViewById(R.id.main_viewPager);

        fragAdapter = new fragAdapter(getSupportFragmentManager());

        tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragAdapter);

    }



    public static DataHolder getInstance() {return holder;}

    public void onClick(View view) {

        keyWordLayout = (TextInputLayout) findViewById(R.id.inputLayoutKeyword);
        inputLayoutZipcode = (TextInputLayout) findViewById(R.id.inputLayoutZipcode);
        editTextZipcode = (AutoCompleteTextView) findViewById(R.id.editTextZipcode);
        editTextDistance = findViewById(R.id.editTextDistance);


        check_new = findViewById(R.id.check_new);
        check_used = findViewById(R.id.check_used);
        check_unsp = findViewById(R.id.check_unsp);
        check_local = findViewById(R.id.check_local);
        check_free = findViewById(R.id.check_free);
        check_nearby = findViewById(R.id.check_nearby);

        currentRadio = (RadioButton) findViewById(R.id.currentRadio);

        String inputName = keyWordLayout.getEditText().getText().toString().trim();
        if(inputName.isEmpty()){
            keyWordLayout.setErrorEnabled(true);
            keyWordLayout.setError("Please enter mandatory field");
        }else{
            keyWordLayout.setErrorEnabled(false);
            keyWordLayout.setError(null);
            keyWordLayout.getEditText().clearFocus();
        }

        String textZipcode = editTextZipcode.getText().toString().trim();
        if(editTextZipcode.isEnabled() && !Pattern.matches("\\d{5}", textZipcode)){
            inputLayoutZipcode.setErrorEnabled(true);
            inputLayoutZipcode.setError("Please enter mandatory field");
        }else{
            inputLayoutZipcode.setErrorEnabled(false);
            inputLayoutZipcode.setError(null);
            inputLayoutZipcode.getEditText().clearFocus();
        }

        if(inputName.isEmpty() || (editTextZipcode.isEnabled() && !Pattern.matches("\\d{5}", textZipcode) && check_nearby.isChecked()) ){
            Toast.makeText(this,"Please fix all fields with errors", Toast.LENGTH_SHORT).show();
        }else{

            if(isGetIp){

                Intent myIntent = new Intent(getBaseContext(), Result.class);
                myIntent.putExtra("keyword", keyWordLayout.getEditText().getText().toString().trim());

                if(check_nearby.isChecked()){
                    myIntent.putExtra("buyerPostalCode", (!inputLayoutZipcode.getEditText().getText().toString().trim().isEmpty())?(inputLayoutZipcode.getEditText().getText().toString().trim()):localZipCode);
                    myIntent.putExtra("MaxDistance", (!editTextDistance.getText().toString().trim().isEmpty())?editTextDistance.getText().toString().trim():"10");
                }else{
                    myIntent.putExtra("buyerPostalCode", "");
                    myIntent.putExtra("MaxDistance", "");

                }

                myIntent.putExtra("isCondition", (check_new.isChecked() || check_used.isChecked() || check_unsp.isChecked())?true:false);
                myIntent.putExtra("conditionNew", (check_new.isChecked())?true:false);
                myIntent.putExtra("conditionUsed", (check_used.isChecked())?true:false);
                myIntent.putExtra("conditionUnsp", (check_unsp.isChecked())?true:false);
                myIntent.putExtra("isFreeshipping", (check_free.isChecked())?true:false);
                myIntent.putExtra("isLocalpickup", (check_local.isChecked())?true:false);

                cat_key = MainActivity.getInstance().readString(this,"cat_key");
                myIntent.putExtra("categoryId", cat_key);

                //saveSearch
                this.startActivity(myIntent);
            }else{
                Toast.makeText(this,"Not get ip-api Zipcode", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClear(View view) {

        keyWordLayout = (TextInputLayout) findViewById(R.id.inputLayoutKeyword);
        inputLayoutZipcode = (TextInputLayout) findViewById(R.id.inputLayoutZipcode);
        editTextZipcode = (AutoCompleteTextView) findViewById(R.id.editTextZipcode);
        editTextDistance = findViewById(R.id.editTextDistance);
//
//
//        check_new = findViewById(R.id.check_new);
//        check_used = findViewById(R.id.check_used);
//        check_unsp = findViewById(R.id.check_unsp);
//        check_local = findViewById(R.id.check_local);
//        check_free = findViewById(R.id.check_free);
        check_nearby = findViewById(R.id.check_nearby);
//
        spin = findViewById(R.id.spinner_cat);
        currentRadio = (RadioButton) findViewById(R.id.currentRadio);

        keyWordLayout.setErrorEnabled(false);
        keyWordLayout.setError(null);
        keyWordLayout.getEditText().clearFocus();
        keyWordLayout.getEditText().setText("");

        inputLayoutZipcode.setError(null);
        inputLayoutZipcode.getEditText().clearFocus();
        inputLayoutZipcode.getEditText().setText("");

        spin.setSelection(0);

        //check_new.setChecked(false);
        //check_used.setChecked(false);
        //check_unsp.setChecked(false);
        //check_local.setChecked(false);
        //check_free.setChecked(false);
        check_nearby.setChecked(false);

        editTextZipcode.setText("");
        editTextDistance.setText("");

        currentRadio.setChecked(true);

        hideKeyWord(view);
    }

    public void hideKeyWord(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteQuietly(getCacheDir());
    }


    @Override
    protected void onStart() {
        super.onStart();
        FileUtils.deleteQuietly(getCacheDir());
    }



}
