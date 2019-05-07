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

public class wishItemAdapter extends RecyclerView.Adapter<wishItemAdapter.wishItemViewHolder> {

    private Context myContext;
    private ArrayList<resultItems> myWishItems = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public wishItemAdapter(Context myContext, ArrayList<resultItems> myWishItems) {
        this.myContext = myContext;
        this.myWishItems = myWishItems;
    }

    @NonNull
    @Override
    public wishItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new wishItemViewHolder(LayoutInflater.from(myContext).inflate(R.layout.result_item, viewGroup, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull wishItemViewHolder wishItemViewHolder, int i) {
        resultItems current = myWishItems.get(i);

        String image;
        String title;
        String zipcode;
        String shipping;
        String condition;
        String price;
        final int position = i;

        image = current.getImage();
        title = current.getTitle();
        zipcode = current.getZipcode();
        shipping = current.getShipping();
        condition = current.getCondition();
        price = current.getPrice();

        //itemId = current.getItemId();
        //wishItemViewHolder.resultImage.setImageBitmap(null);
        Picasso.with(myContext).load(image).fit().centerInside().error(R.drawable.placeholder).into(wishItemViewHolder.resultImage);
        wishItemViewHolder.resultTitle.setText(title);
        wishItemViewHolder.resultZipcode.setText(zipcode);
        wishItemViewHolder.resultShipping.setText(shipping.equals("0.00")? "Free Shipping" : (shipping.equals("N/A")?shipping:"$"+shipping));
        wishItemViewHolder.resultCondition.setText(condition);
        wishItemViewHolder.resultPrice.setText(price.equals("N/A")? price :"$"+price);

        wishItemViewHolder.wishToggle.setChecked(true);

        wishItemViewHolder.itemView.setTag(current);

    }

    @Override
    public int getItemCount() {
        return myWishItems.size();
    }

    public class wishItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView resultImage;
        public TextView resultTitle;
        public TextView resultZipcode;
        public TextView resultShipping;
        public TextView resultCondition;
        public TextView resultPrice;
        public ToggleButton wishToggle;

        public wishItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            resultImage = itemView.findViewById(R.id.resultImage);
            resultTitle = itemView.findViewById(R.id.resultTitle);
            resultZipcode = itemView.findViewById(R.id.resultZipcode);
            resultShipping = itemView.findViewById(R.id.resultShipping);
            resultCondition = itemView.findViewById(R.id.resultCondition);
            resultPrice = itemView.findViewById(R.id.resultPrice);
            wishToggle = itemView.findViewById(R.id.wishToggle);

            wishToggle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(pos);
                        }
                    }
                }
            });

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
        }
    }

//    public ArrayList<resultItems> readWish() {
//        SharedPreferences localWishList = myContext.getSharedPreferences("localWishList", MODE_PRIVATE);
//        Gson gson = new Gson();
//        Type itemType = new TypeToken<ArrayList<resultItems>>() {
//        }.getType();
//        String jsonRead = localWishList.getString("localWishList", gson.toJson(new ArrayList<resultItems>()));
//        ArrayList<resultItems> myWishItems = gson.fromJson(jsonRead, itemType);
//        return myWishItems;
//    }
//
//    public void saveWish(ArrayList<resultItems> myWishItems) {
//        SharedPreferences localWishList = myContext.getSharedPreferences("localWishList", MODE_PRIVATE);
//        Gson gson = new Gson();
//        Type itemType = new TypeToken<ArrayList<resultItems>>() {
//        }.getType();
//        SharedPreferences.Editor edit = localWishList.edit();
//        String jsonWrite = gson.toJson(myWishItems, itemType);
//        edit.putString("localWishList", jsonWrite);
//        edit.commit();
//    }
}
