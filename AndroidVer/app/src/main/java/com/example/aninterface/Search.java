package com.example.aninterface;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Search extends Fragment implements AdapterView.OnItemSelectedListener {

    private String[] ZIPCODES = new String[]{};

    //string https://api.myjson.com/bins/rrvno

    private RequestQueue myRequestQ;
    private String Domain;
    private AutoCompleteTextView editTextZipcode;
    private Map<String, String> map = new HashMap<String, String>();

    public Search() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Domain = MainActivity.getInstance().getDomain();
        myRequestQ = Volley.newRequestQueue(getActivity());
        //String message = getArguments().getString("message");
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editTextZipcode = (AutoCompleteTextView) view.findViewById(R.id.editTextZipcode);

        //spinner
        Spinner spin = view.findViewById(R.id.spinner_cat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        CheckBox isNearby = (CheckBox) view.findViewById(R.id.check_nearby);
        isNearby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getView().findViewById(R.id.editTextDistance).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.labal_from).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.radioGroup).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.inputLayoutZipcode).setVisibility(View.VISIBLE);

                } else {
                    getView().findViewById(R.id.editTextDistance).setVisibility(View.GONE);
                    getView().findViewById(R.id.labal_from).setVisibility(View.GONE);
                    getView().findViewById(R.id.radioGroup).setVisibility(View.GONE);
                    getView().findViewById(R.id.inputLayoutZipcode).setVisibility(View.GONE);
                }

            }
        });

        RadioButton customRadio = (RadioButton) view.findViewById(R.id.customRadio);
        RadioButton currentRadio = (RadioButton) view.findViewById(R.id.currentRadio);
        customRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText editTextZipcode = (EditText) getView().findViewById(R.id.editTextZipcode);
                if (isChecked) {
                    editTextZipcode.setEnabled(true);
                } else {

                    editTextZipcode.setText("");
                    editTextZipcode.setEnabled(false);

                }

            }
        });

        currentRadio.setChecked(true);
        view.findViewById(R.id.editTextZipcode).setEnabled(false);
        view.findViewById(R.id.editTextDistance).setVisibility(View.GONE);
        view.findViewById(R.id.labal_from).setVisibility(View.GONE);
        view.findViewById(R.id.radioGroup).setVisibility(View.GONE);
        view.findViewById(R.id.inputLayoutZipcode).setVisibility(View.GONE);

        editTextZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    loadAutoCompleteJson(s.toString());
                    //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                }
            }
        });

        map.put("All", "-1"); // no cate
        map.put("Art", "550");
        map.put("Baby", "2984");
        map.put("Books", "267");
        map.put("Clothing, Shoes & Accessories", "11450");
        map.put("Computers/Tablets & Networking", "58058");
        map.put("Health & Beauty", "26395");
        map.put("Music", "11233");
        map.put("Video Games & Consoles", "1249");

        return view;
    }

    public void loadAutoCompleteJson(final String startsWith) {
        final String url = Domain + "api/android/ip-json/?startsWith=" + startsWith;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String[] zips = new String[5];
                            for (int i = 0; i < response.length(); i++) {
                                zips[i] = response.getString(i);
                            }
                            final ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, zips);
                            editTextZipcode.setAdapter(adap);

                        } catch (JSONException e) {
                            e.printStackTrace();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String key = parent.getItemAtPosition(position).toString();
        MainActivity.getInstance().saveString(getActivity(), map.get(key), "cat_key");
        //Toast.makeText(getContext(), map.get(key), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
