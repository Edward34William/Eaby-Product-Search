package com.example.aninterface;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;


public class ProductInfo extends Fragment {

    private ArrayList<String> GalleryURL = new ArrayList<>();
    private ArrayList<String> bulletArray = new ArrayList<>();
    private resultItems item;

    private String Price;
    private String Title;
    private String Shipping;

    private String Subtitle;
    private String Brand;


    public ProductInfo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_info, container, false);

//        view.findViewById(R.id.nestscrollview_info).setVisibility(View.GONE);
//        view.findViewById(R.id.progressBar_info).setVisibility(View.VISIBLE);

        LinearLayout galleryLinearLayout = (LinearLayout) view.findViewById(R.id.galleryLinearLayout);

        Shipping = getArguments().getString("shipping");
        Title = getArguments().getString("title");
        Price = MainActivity.getInstance().readString(getActivity(), "Price");;
        Subtitle = MainActivity.getInstance().readString(getActivity(), "Subtitle");
        Brand = MainActivity.getInstance().readString(getActivity(), "Brand");
        bulletArray = MainActivity.getInstance().readStringArrayList(getContext(), "Specifications");

        item = MainActivity.getInstance().readItem(getActivity());
        //GalleryURL = item.getGalleyPhotos();
        GalleryURL = MainActivity.getInstance().readStringArrayList(getContext(), "ProductImages");

        for(int i = 0; i< GalleryURL.size(); i++){


//            ImageView imageView = new ImageView(getActivity());
//            //imageView.setMinimumWidth(container.getWidth());
//            Picasso.with(getActivity()).load(GalleryURL.get(i)).resize(container.getWidth(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 274, getResources().getDisplayMetrics())).centerInside().into(imageView);
//
//            CardView card = new CardView(getActivity());
//            card.addView(imageView);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            int mar = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
//            params.setMargins(3, 0,3, mar);
//            //params.width = 1200;
//            //params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 274, getResources().getDisplayMetrics());
//            card.setLayoutParams(params);
//            galleryLinearLayout.addView(card);

            CardView card = new CardView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int mar = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
            params.setMargins(3, 0,3, mar);
            params.width = 1000;
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 274, getResources().getDisplayMetrics());
            card.setLayoutParams(params);

            ImageView imageView = new ImageView(getActivity());
            imageView.setMinimumWidth(container.getWidth());
            Picasso.with(getActivity()).load(GalleryURL.get(i)).fit().centerInside().error(R.drawable.placeholder).into(imageView);

            card.addView(imageView);
            galleryLinearLayout.addView(card);
        }

        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
        TextView shippingTextView = (TextView) view.findViewById(R.id.shippingTextView);

        TextView subTitleTextView = (TextView) view.findViewById(R.id.subTitleTextView);
        TextView priceTextView2 = (TextView) view.findViewById(R.id.priceTextView2);
        TextView brandTextView = (TextView) view.findViewById(R.id.brandTextView);


        titleTextView.setText(Title);
        priceTextView.setText("$" + Price);
        shippingTextView.setText(Shipping);

        if(!Subtitle.isEmpty()){
            view.findViewById(R.id.subTitleLabel).setVisibility(View.VISIBLE);
            view.findViewById(R.id.hightlightsTextView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.highlightsIcon).setVisibility(View.VISIBLE);
            view.findViewById(R.id.highlightHR).setVisibility(View.VISIBLE);
            subTitleTextView.setVisibility(View.VISIBLE);
            subTitleTextView.setText(Subtitle);
        };

        if(!Price.isEmpty()){
            view.findViewById(R.id.priceTextView2Label).setVisibility(View.VISIBLE);
            view.findViewById(R.id.hightlightsTextView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.highlightsIcon).setVisibility(View.VISIBLE);
            view.findViewById(R.id.highlightHR).setVisibility(View.VISIBLE);
            priceTextView2.setVisibility(View.VISIBLE);
            priceTextView2.setText("$" + Price);
        };

        if(!Brand.isEmpty()){
            view.findViewById(R.id.brandTextViewLabel).setVisibility(View.VISIBLE);
            view.findViewById(R.id.hightlightsTextView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.highlightsIcon).setVisibility(View.VISIBLE);
            view.findViewById(R.id.highlightHR).setVisibility(View.VISIBLE);
            brandTextView.setVisibility(View.VISIBLE);
            brandTextView.setText(WordUtils.capitalizeFully(Brand));
        };

        if(bulletArray.size() > 0){
            view.findViewById(R.id.SpecTextViewLabel).setVisibility(View.VISIBLE);
            view.findViewById(R.id.SpecTextViewIcon).setVisibility(View.VISIBLE);
            view.findViewById(R.id.SpecTextViewHR).setVisibility(View.VISIBLE);

            for (int i = 0; i< bulletArray.size(); i++){
                LinearLayout bulletLinearLayout = view.findViewById(R.id.bulletLinearLayout);
                TextView tv = new TextView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                tv.setLayoutParams(params);
                tv.setText("\u2022 " + WordUtils.capitalizeFully(bulletArray.get(i)));
                tv.setMinHeight(10);
                bulletLinearLayout.addView(tv);
            }
        }


//        view.findViewById(R.id.nestscrollview_info).setVisibility(View.VISIBLE);
        view.findViewById(R.id.progressBar_info).setVisibility(View.GONE);
        return view;
    }

}
