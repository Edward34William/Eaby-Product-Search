package com.example.aninterface;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.util.Log.d;
import static com.example.aninterface.simItem.itemDaysComparatorAsc;
import static com.example.aninterface.simItem.itemDaysComparatorDes;
import static com.example.aninterface.simItem.itemNameComparatorAsc;
import static com.example.aninterface.simItem.itemNameComparatorDes;
import static com.example.aninterface.simItem.itemPriceComparatorAsc;
import static com.example.aninterface.simItem.itemPriceComparatorDes;

public class Similar extends Fragment implements AdapterView.OnItemSelectedListener {

    private simItemAdapter myRecyclerAdapter;
    private RecyclerView simRecyclerView;
    private ArrayList<simItem> mySimItems = new ArrayList<>();
    private ArrayList<simItem> backupItems = new ArrayList<>();
    private View view;

    private String cat_key = "Default";
    private String method_key = "Ascending";

    private ArrayAdapter<CharSequence> adapter_cart;
    private ArrayAdapter<CharSequence> adapter_method;

    private Spinner sort_cat;
    private Spinner sort_method;

    public Similar() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_similar, container, false);

        view.findViewById(R.id.simScrollView).setVisibility(View.GONE);
        view.findViewById(R.id.linearLayout).setVisibility(View.GONE);
        view.findViewById(R.id.NoSimTextView).setVisibility(View.GONE);
//        view.findViewById(R.id.progressBar_simliar).setVisibility(View.VISIBLE);

        mySimItems = MainActivity.getInstance().readSim(getActivity());
        backupItems = new ArrayList<>(mySimItems);
        //Toast.makeText(getActivity(), "mySimItems size: " + mySimItems.size(), Toast.LENGTH_SHORT).show();



        simRecyclerView = view.findViewById(R.id.simRecyclerView);
        simRecyclerView.setHasFixedSize(true);
        simRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        myRecyclerAdapter = new simItemAdapter(view.getContext(), mySimItems);
        simRecyclerView.setAdapter(myRecyclerAdapter);
        simRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerAdapter.notifyDataSetChanged();

        myRecyclerAdapter.setOnItemClickListener(new simItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Uri url = Uri.parse(mySimItems.get(position).get_viewItemURL());
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });

        //spinner
        sort_cat = view.findViewById(R.id.sort_cat);
        sort_method = view.findViewById(R.id.sort_method);

        adapter_cart = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_cart, android.R.layout.simple_spinner_item);
        adapter_cart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_cat.setAdapter(adapter_cart);

        adapter_method = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_method, android.R.layout.simple_spinner_item);
        adapter_method.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_method.setAdapter(adapter_method);

        sort_cat.setOnItemSelectedListener(this);
        sort_method.setOnItemSelectedListener(this);

        view.findViewById(R.id.progressBar_simliar).setVisibility(View.GONE);
        if (mySimItems.size() > 0) {
            view.findViewById(R.id.simScrollView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);

        } else {
            view.findViewById(R.id.NoSimTextView).setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.sort_cat) {
            cat_key = parent.getItemAtPosition(position).toString();
            //Toast.makeText(getContext(), "cat_key: " + cat_key, Toast.LENGTH_SHORT).show();

            if (cat_key.equals("Default")) {

                Collections.copy(mySimItems, backupItems);

                sort_method.setEnabled(false);
            } else {
                sort_method.setEnabled(true);
            }

        } else {
            method_key = parent.getItemAtPosition(position).toString();
            //Toast.makeText(getContext(), "method_key: " + method_key, Toast.LENGTH_SHORT).show();
        }

        if (cat_key.equals("Name") && method_key.equals("Ascending")) {
            Collections.sort(mySimItems, itemNameComparatorAsc);
        }
        if (cat_key.equals("Price") && method_key.equals("Ascending")) {
            Collections.sort(mySimItems, itemPriceComparatorAsc);
        }
        if (cat_key.equals("Days") && method_key.equals("Ascending")) {
            Collections.sort(mySimItems, itemDaysComparatorAsc);
        }
        if (cat_key.equals("Name") && method_key.equals("Descending")) {
            Collections.sort(mySimItems, itemNameComparatorDes);
        }
        if (cat_key.equals("Price") && method_key.equals("Descending")) {
            Collections.sort(mySimItems, itemPriceComparatorDes);
        }
        if (cat_key.equals("Days") && method_key.equals("Descending")) {
            Collections.sort(mySimItems, itemDaysComparatorDes);
        }

        myRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
