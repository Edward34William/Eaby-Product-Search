package com.example.aninterface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.GestureDetector;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.d;

public class fragAdapterDetail extends FragmentStatePagerAdapter {
    private final String[] tabs = new String[]{"Product", "Shipping", "Photos", "Similar"};
    private resultItems item = null;

    //detail
    private ArrayList<String> galleyPhotos;
    private String subtitle;
    private String brand = null;
    private ArrayList<String> specifications;

    //sold by
    private String storeName;
    private String FeedbackScore;
    private String Popularity;
    private String FeedbackStar;

    //return
    private String ShippedBy;
    private String RefundMe;
    private String ReturnWithin;
    private String Policy;

    public fragAdapterDetail(FragmentManager fm, resultItems item) {
        super(fm);
        this.item = item;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0){
            ProductInfo productInfo = new ProductInfo();
            Bundle bundle = new Bundle();
            bundle.putString("title", item.getTitle().toString());
            //bundle.putString("price", "$" + String.format("%.2f", item.getPrice()));
            bundle.putString("shipping", (item.getShipping().equals("0.00"))?"Free Shipping": ((item.getShipping().equals("N/A")?item.getShipping():"$"+item.getShipping())));
            productInfo.setArguments(bundle);
            return productInfo;
        }else if(i == 1){
            Shipping shipping = new Shipping();
            Bundle bundle = new Bundle();
            //bundle.putString("condition", item.getCondition());
            bundle.putString("shipping", (item.getShipping().equals("0.00"))?"Free Shipping": ((item.getShipping().equals("N/A")?item.getShipping():"$"+item.getShipping())));
            //bundle.putString("gobalShipping", item.getGobalShipping());
            //bundle.putString("handlingTime", item.getHandlingTime());
            //bundle.putString("storeName", item.getStoreName());
            //bundle.putString("storeURL", item.getStoreURL());
            bundle.putInt("feedbackScore", item.getFeedbackScore());
            bundle.putInt("popularity", item.getPopularity());
            bundle.putString("feedbackStar", item.getFeedbackStar());

            shipping.setArguments(bundle);
            return shipping;
        }else if(i == 2){
            Photos photos = new Photos();
            return photos;
        }else if(i == 3){
            Similar similar = new Similar();
            return similar;
        }else{
            return null;
        }
    }

    @Override
    public int getCount() {
        return tabs.length;

    }
}
