package com.example.aninterface;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;

public class Shipping extends Fragment {


    private View view;
    private int popularity;
    private String storeNameString;
    private String storeURLString;
    private String feedbackStar;
    private int feedbackScore;
    private String shipping;
    private String GlobalShipping;
    private String HandlingTime;
    private String ConditionDescription;
    private String Policy;
    private String ReturnsWithin;
    private String RefundMode;
    private String ShippedBy;

    public Shipping() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shipping, container, false);

        view.findViewById(R.id.nestscrollview_shipping).setVisibility(View.GONE);
        view.findViewById(R.id.progressBar_shipping).setVisibility(View.VISIBLE);

        popularity = getArguments().getInt("popularity");
        storeNameString = MainActivity.getInstance().readString(getActivity(), "StoreName") ;//getArguments().getString("storeName")
        storeURLString = MainActivity.getInstance().readString(getActivity(), "StoreURL") ;//getArguments().getString("storeURL")
        feedbackStar = getArguments().getString("feedbackStar");
        feedbackScore = getArguments().getInt("feedbackScore");

        shipping = getArguments().getString("shipping");
        GlobalShipping = MainActivity.getInstance().readString(getActivity(), "GlobalShipping");
        HandlingTime = MainActivity.getInstance().readString(getActivity(), "HandlingTime");
        ConditionDescription = MainActivity.getInstance().readString(getActivity(), "ConditionDescription");
        Policy = MainActivity.getInstance().readString(getActivity(), "Policy");
        ReturnsWithin = MainActivity.getInstance().readString(getActivity(), "ReturnsWithin");
        RefundMode = MainActivity.getInstance().readString(getActivity(), "RefundMode");
        ShippedBy = MainActivity.getInstance().readString(getActivity(), "ShippedBy");


        //-------------------------------------sold

        hideSoldLabel();

        if (popularity != -1) {
            CircularScoreView circularScoreView = (CircularScoreView) view.findViewById(R.id.popularity);
            //circularScoreView.setScore((int)MainActivity.getInstance().readDouble(getActivity(), "Popularity"));
            circularScoreView.setScore(popularity);
            showSoldLabel();
        }else{
            view.findViewById(R.id.popularity).setVisibility(View.GONE);
            view.findViewById(R.id.popularityLabel).setVisibility(View.GONE);
        }

        if (storeNameString != null && !storeNameString.isEmpty()) {
            TextView storeName = (TextView) view.findViewById(R.id.storeName);
            storeName.setMovementMethod(LinkMovementMethod.getInstance());

            String storeUrl;
            if(storeURLString != null && !storeURLString.isEmpty()){
                storeUrl = "<a href=\"" + storeURLString + "\">"
                        + storeNameString
                        + "</a>";
            }else{
                storeUrl = storeNameString;
            }

            storeName.setText(Html.fromHtml(storeUrl));
            showSoldLabel();
        }else{
            view.findViewById(R.id.storeName).setVisibility(View.GONE);
            view.findViewById(R.id.storeNameLabel).setVisibility(View.GONE);
        }

        if ( feedbackStar != null && !feedbackStar.isEmpty()) {
            ImageView feedback = (ImageView) view.findViewById(R.id.feedback);

            if(feedbackStar.contains("Shooting")){
                feedback.setBackgroundResource(R.drawable.star_shoot);
            }else{
                feedback.setBackgroundResource(R.drawable.star);
            }

            if(feedbackStar.contains("Red")){
                feedback.getBackground().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("Blue")){
                feedback.getBackground().setColorFilter(Color.parseColor("#0000FF"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("Green")){
                feedback.getBackground().setColorFilter(Color.parseColor("#008000"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("Purple")){
                feedback.getBackground().setColorFilter(Color.parseColor("#800080"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("Silver")){
                feedback.getBackground().setColorFilter(Color.parseColor("#C0C0C0"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("Yellow")){
                feedback.getBackground().setColorFilter(Color.parseColor("#FBC02D"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("Turquoise")){
                feedback.getBackground().setColorFilter(Color.parseColor("#40E0D0"), PorterDuff.Mode.MULTIPLY);
            }

            if(feedbackStar.contains("None")){
                feedback.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.MULTIPLY);
            }

            showSoldLabel();
        }else{
            view.findViewById(R.id.feedback).setVisibility(View.GONE);
            view.findViewById(R.id.feedbackLabel).setVisibility(View.GONE);
        }


        //need
        if ( feedbackScore != -1) {
            TextView feedbackScoreTextView =  view.findViewById(R.id.feedbackScore);
            feedbackScoreTextView.setText(feedbackScore + "");
            showSoldLabel();
        }else{
            view.findViewById(R.id.feedbackScore).setVisibility(View.GONE);
            view.findViewById(R.id.feedBackScoreLabel).setVisibility(View.GONE);
        }


        //------------------------------shipping

        hideShippingLabel();

        if (shipping!= null && !shipping.isEmpty()) {
            TextView shippingTextView =  view.findViewById(R.id.shipping);
            shippingTextView.setText(shipping);
            showShippingLabel();
        } else {
            view.findViewById(R.id.shipping).setVisibility(View.GONE);
            view.findViewById(R.id.shippingCostLabel).setVisibility(View.GONE);
        }

        if (GlobalShipping != null && !GlobalShipping.isEmpty()) {
            TextView globalShipping =  view.findViewById(R.id.globalShipping);
            globalShipping.setText(GlobalShipping);
            showShippingLabel();
        } else {
            view.findViewById(R.id.globalShipping).setVisibility(View.GONE);
            view.findViewById(R.id.globalShippingLabel).setVisibility(View.GONE);
        }


        if (HandlingTime != null && !HandlingTime.isEmpty()) {
            TextView handlingTime =  view.findViewById(R.id.handlingTime);
            handlingTime.setText(HandlingTime);
            showShippingLabel();
        } else {
            view.findViewById(R.id.handlingTime).setVisibility(View.GONE);
            view.findViewById(R.id.handlingTimeLabel).setVisibility(View.GONE);
        }


        if (ConditionDescription != null && !ConditionDescription.isEmpty()) {
            TextView condition =  view.findViewById(R.id.condition);
            condition.setText(ConditionDescription);
            showShippingLabel();
        } else {
            view.findViewById(R.id.condition).setVisibility(View.GONE);
            view.findViewById(R.id.conditionLabel).setVisibility(View.GONE);
        }

        //--------------------------return

        hideReturnLabel();


        if (Policy != null && !Policy.isEmpty()) {
            TextView policy =  view.findViewById(R.id.policy);
            if(Policy.equals("ReturnsNotAccepted")){
                policy.setText("Returns Not Accepted");
                showReturnLabel();
            }else{
                policy.setText(Policy);
                showReturnLabel();
            }

        } else {
            view.findViewById(R.id.policy).setVisibility(View.GONE);
            view.findViewById(R.id.policyLabel).setVisibility(View.GONE);
        }

        if (ReturnsWithin!= null && !ReturnsWithin.isEmpty()) {
            TextView returnWithin =  view.findViewById(R.id.returnWithin);
            returnWithin.setText(ReturnsWithin);
            showReturnLabel();
        } else {
            view.findViewById(R.id.returnWithin).setVisibility(View.GONE);
            view.findViewById(R.id.returnWithinLabel).setVisibility(View.GONE);
        }


        if (RefundMode!= null && !RefundMode.isEmpty()) {
            TextView refundMode =  view.findViewById(R.id.refundMode);
            refundMode.setText(RefundMode);
            showReturnLabel();
        } else {
            view.findViewById(R.id.refundMode).setVisibility(View.GONE);
            view.findViewById(R.id.refundModeLabel).setVisibility(View.GONE);
        }


        if (ShippedBy!= null && !ShippedBy.isEmpty()) {
            TextView shippedBy =  view.findViewById(R.id.shippedBy);
            shippedBy.setText(ShippedBy);
            showReturnLabel();
        } else {
            view.findViewById(R.id.shippedBy).setVisibility(View.GONE);
            view.findViewById(R.id.shippedByLabel).setVisibility(View.GONE);
        }

        view.findViewById(R.id.nestscrollview_shipping).setVisibility(View.VISIBLE);
        view.findViewById(R.id.progressBar_shipping).setVisibility(View.GONE);

        return view;

    }

    private void hideSoldLabel(){
        view.findViewById(R.id.soldByLabel).setVisibility(View.GONE);
        view.findViewById(R.id.soldByIcon).setVisibility(View.GONE);
    }

    private void showSoldLabel(){
        view.findViewById(R.id.soldByLabel).setVisibility(View.VISIBLE);
        view.findViewById(R.id.soldByIcon).setVisibility(View.VISIBLE);
    }

    private void hideShippingLabel(){
        view.findViewById(R.id.ShippingHR).setVisibility(View.GONE);
        view.findViewById(R.id.ShippingLabel).setVisibility(View.GONE);
        view.findViewById(R.id.ShippingIcon).setVisibility(View.GONE);
    }

    private void showShippingLabel(){
        view.findViewById(R.id.ShippingHR).setVisibility(View.VISIBLE);
        view.findViewById(R.id.ShippingLabel).setVisibility(View.VISIBLE);
        view.findViewById(R.id.ShippingIcon).setVisibility(View.VISIBLE);
    }

    private void hideReturnLabel(){
        view.findViewById(R.id.ReturnIcon).setVisibility(View.GONE);
        view.findViewById(R.id.ReturnHR).setVisibility(View.GONE);
        view.findViewById(R.id.ReturnLabel).setVisibility(View.GONE);
    }

    private void showReturnLabel(){
        view.findViewById(R.id.ReturnIcon).setVisibility(View.VISIBLE);
        view.findViewById(R.id.ReturnHR).setVisibility(View.VISIBLE);
        view.findViewById(R.id.ReturnLabel).setVisibility(View.VISIBLE);
    }
}
