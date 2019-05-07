package com.example.aninterface;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;


public class resultItemAdapter extends RecyclerView.Adapter<resultItemAdapter.resultItemViewHolder> {

    private Context myContext;
    private ArrayList<resultItems> myResultItems;
    private ArrayList<resultItems> myWishItems;
    private resultItemAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onAddCartClick(int position);
        void onRemoveCartClick(int position);
    }

    public void setOnItemClickListener(resultItemAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public resultItemAdapter(Context myContext, ArrayList<resultItems> myResultItems) {
        this.myContext = myContext;
        this.myResultItems = myResultItems;
        //replaceWish();
    }

    @NonNull
    @Override
    public resultItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new resultItemViewHolder(LayoutInflater.from(myContext).inflate(R.layout.result_item, viewGroup, false), mListener);
    }

    public void replaceWish(){
        myWishItems = MainActivity.getInstance().getMyWishItems();
        for (int i = 0; i < myResultItems.size(); i++) {
            Iterator<resultItems> itr = myWishItems.iterator();

            while (itr.hasNext()) {
                resultItems rs = itr.next();
                if (rs.getItemId().equals(myResultItems.get(i).getItemId())) {
                    myResultItems.get(i).setWish(true);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull resultItemViewHolder resultItemViewHolder, int i) {

        resultItems current = myResultItems.get(i);

        String image = myResultItems.get(i).getImage();
        String title = myResultItems.get(i).getTitle();
        String zipcode = myResultItems.get(i).getZipcode();
        String shipping = myResultItems.get(i).getShipping();
        String condition = myResultItems.get(i).getCondition();
        String price = myResultItems.get(i).getPrice();
        boolean isWish = myResultItems.get(i).isWish();
        String itemId = myResultItems.get(i).getItemId();

        //resultItemViewHolder.resultImage.setImageBitmap(null);
        Picasso.with(myContext).load(image).fit().centerInside().error(R.drawable.placeholder).into(resultItemViewHolder.resultImage);
        resultItemViewHolder.resultTitle.setText(title);
        resultItemViewHolder.resultZipcode.setText(zipcode);
        resultItemViewHolder.resultShipping.setText((shipping.equals("0.00"))? "Free Shipping" : (shipping.equals("N/A")?shipping:"$"+shipping));
        resultItemViewHolder.resultCondition.setText(condition);
        resultItemViewHolder.resultPrice.setText(price.equals("N/A")? price :"$"+price);

        if (isWish) {
            resultItemViewHolder.wishToggle.setChecked(true);
        } else {
            resultItemViewHolder.wishToggle.setChecked(false);
        }

//        resultItemViewHolder.wishToggle.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                resultItems ri = new resultItems(image, title, zipcode, shipping, condition, price, true, itemId);
//
//                if (((ToggleButton) v).isChecked()) {
//
//                    d("Toggle", "Toggle button is On");
//
//                    current.setWish(true);
//                    myResultItems.set(i, current);
//
//                    d("myWishItems title", title);
//                    d("myWishItems", myWishItems.toString());
//
//                    myWishItems.add(ri);
//                    d("add",myWishItems.toString());
//                } else {
//
//                    d("Toggle", "Toggle button is Off");
//
//                    current.setWish(false);
//                    myResultItems.set(i, current);
//
//                    Iterator<resultItems> itr = myWishItems.iterator();
//
//                    while (itr.hasNext()) {
//                        resultItems rs = itr.next();
//                        if (rs.getItemId().equals(itemId)) {
//                            itr.remove();
//                        }
//                    }
//
//                    d("remove",myWishItems.toString());
//                }
//                MainActivity.getInstance().setMyWishItems(myWishItems, myContext);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return myResultItems.size();
    }

    public class resultItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView resultImage;
        public TextView resultTitle;
        public TextView resultZipcode;
        public TextView resultShipping;
        public TextView resultCondition;
        public TextView resultPrice;
        public ToggleButton wishToggle;

        public resultItemViewHolder(@NonNull View itemView, final resultItemAdapter.OnItemClickListener listener) {
            super(itemView);
            resultImage = itemView.findViewById(R.id.resultImage);
            resultTitle = itemView.findViewById(R.id.resultTitle);
            resultZipcode = itemView.findViewById(R.id.resultZipcode);
            resultShipping = itemView.findViewById(R.id.resultShipping);
            resultCondition = itemView.findViewById(R.id.resultCondition);
            resultPrice = itemView.findViewById(R.id.resultPrice);
            wishToggle = itemView.findViewById(R.id.wishToggle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

            wishToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            if(wishToggle.isChecked()){
                                listener.onAddCartClick(pos);
                            }else{
                                listener.onRemoveCartClick(pos);
                            }

                        }
                    }
                }
            });

        }
    }

//    public ArrayList<resultItems> readWish(){
//        SharedPreferences localWishList = myContext.getSharedPreferences("localWishList", MODE_PRIVATE);
//        Gson gson = new Gson();
//        Type itemType = new TypeToken<ArrayList<resultItems>>() {}.getType();
//        String jsonRead = localWishList.getString("localWishList", gson.toJson(new ArrayList<resultItems>()));
//        ArrayList<resultItems> myWishItems = gson.fromJson(jsonRead, itemType);
//        return myWishItems;
//    }
//
//    public void saveWish(ArrayList<resultItems> myWishItems){
//        SharedPreferences localWishList = myContext.getSharedPreferences("localWishList", MODE_PRIVATE);
//        Gson gson = new Gson();
//        Type itemType = new TypeToken<ArrayList<resultItems>>() {}.getType();
//        SharedPreferences.Editor edit = localWishList.edit();
//        String jsonWrite = gson.toJson(myWishItems, itemType);
//        edit.putString("localWishList", jsonWrite);
//        edit.commit();
//    }
}