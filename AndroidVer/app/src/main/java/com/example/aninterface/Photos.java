package com.example.aninterface;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Photos extends Fragment {

    private ArrayList<String> googleURL = new ArrayList<>();

    private resultItems item;

    private View view;

    public Photos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_photos, container, false);

        view.findViewById(R.id.googleScrollView).setVisibility(View.GONE);
//        view.findViewById(R.id.progressBar_photos).setVisibility(View.VISIBLE);
        view.findViewById(R.id.googleErrorTextView).setVisibility(View.GONE);

        final LinearLayout googleLinearLayout = (LinearLayout) view.findViewById(R.id.googleLinearLayout);
        googleLinearLayout.setOrientation(LinearLayout.VERTICAL);

        item = MainActivity.getInstance().readItem(getActivity());
        //googleURL = item.getGoogleImages();
        googleURL = MainActivity.getInstance().readStringArrayList(getContext(), "Google");

        for(int i = 0; i< googleURL.size(); i++){

            ImageView imageView = new ImageView(getActivity());
            imageView.setMinimumWidth(container.getWidth());
            Picasso.with(getActivity()).load(googleURL.get(i)).resize(container.getWidth(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics())).centerInside().error(R.drawable.placeholder).into(imageView);
//            LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            imageView.setLayoutParams(paramsImage);
//            imageView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
            CardView card = new CardView(getActivity());
            card.addView(imageView);
            card.setMinimumWidth(container.getWidth());
            //card.setMinimumHeight(imageView.getHeight());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 20, 10, 20);
            card.setLayoutParams(params);
            //card.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
            googleLinearLayout.addView(card);



        }

        view.findViewById(R.id.progressBar_photos).setVisibility(View.GONE);

        if(googleURL.size()>0){
            view.findViewById(R.id.googleScrollView).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.googleErrorTextView).setVisibility(View.VISIBLE);
        }

        return view;
    }



}
