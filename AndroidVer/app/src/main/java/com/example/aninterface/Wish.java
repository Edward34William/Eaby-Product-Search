package com.example.aninterface;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import static android.content.Context.MODE_PRIVATE;
import static android.util.Log.d;


public class Wish extends Fragment {

    private RecyclerView wishRecyclerView;
    private wishItemAdapter myRecyclerAdapter;
    private ArrayList<resultItems> myWishItems = new ArrayList<>();
    private View view;
    private RequestQueue myRequestQ;

    public Wish() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_wish, container, false);

        myWishItems = MainActivity.getInstance().getMyWishItems();

        myRequestQ = Volley.newRequestQueue(getActivity());

        wishRecyclerView = view.findViewById(R.id.wishRecyclerView);
        wishRecyclerView.setHasFixedSize(true);
        wishRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2)); //getApplicationContext()
        myRecyclerAdapter = new wishItemAdapter(view.getContext(), myWishItems);
        wishRecyclerView.setAdapter(myRecyclerAdapter);
        wishRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerAdapter.notifyDataSetChanged();
        myRecyclerAdapter.setOnItemClickListener(new wishItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                resultItems item = myWishItems.get(position);
                //item.setWish(true);
                load(item);
            }

            @Override
            public void onDeleteClick(int position) {
                remove(position);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        myRecyclerAdapter.notifyDataSetChanged();
        if(myWishItems.size() != 0){
            getActivity().findViewById(R.id.NoWishTextView).setVisibility(View.GONE);
        }else{
            getActivity().findViewById(R.id.NoWishTextView).setVisibility(View.VISIBLE);
        }
        super.onStart();
        updateTotalInfo();
    }
    

    public void remove(int position) {
        Toast.makeText(getActivity(), "Remove " + myWishItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        myWishItems.remove(position);
        myRecyclerAdapter.notifyItemRemoved(position);
        MainActivity.getInstance().setMyWishItems(myWishItems, getActivity());
        if(myWishItems.size() == 0){
            getActivity().findViewById(R.id.NoWishTextView).setVisibility(View.VISIBLE);
        }
        updateTotalInfo();
    }

    public void updateTotalInfo(){
        TextView totalPrice = view.findViewById(R.id.totalPrice);
        TextView totalNumber = view.findViewById(R.id.totalNumber);
        totalNumber.setText(myWishItems.size() + "");

        double total = 0;
        for(int i = 0; i< myWishItems.size(); i++){
            if(myWishItems.get(i).getPrice().equals("N/A")){
                total += 0;
            }else{
                total += Double.parseDouble(myWishItems.get(i).getPrice());
            }
        }
        totalPrice.setText("$" + String.format("%.2f", total));
    }

    public void load(final resultItems item) {
        MainActivity.getInstance().saveItem(getActivity(), item);
        Intent myIntent = new Intent(getActivity(), Detail.class);
        getActivity().startActivity(myIntent);

    }

}
