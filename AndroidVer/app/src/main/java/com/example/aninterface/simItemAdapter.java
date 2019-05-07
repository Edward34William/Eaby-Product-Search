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

public class simItemAdapter extends RecyclerView.Adapter<simItemAdapter.simItemViewHolder> {

    private Context myContext;
    private ArrayList<simItem> mySimItems = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public simItemAdapter(Context myContext, ArrayList<simItem> mySimItems) {
        this.myContext = myContext;
        this.mySimItems = mySimItems;
    }

    @NonNull
    @Override
    public simItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new simItemViewHolder(LayoutInflater.from(myContext).inflate(R.layout.similar_item, viewGroup, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull simItemViewHolder simItemViewHolder, int i) {
        simItem current = mySimItems.get(i);

        String image;
        String title;
        double shipping;
        double price;
        int daysLeft;
        String url;
        final int position = i;

        image = current.getImage();
        title = current.getTitle();
        shipping = current.getShipping();
        price = current.getPrice();
        daysLeft = current.getDaysleft();
        url = current.get_viewItemURL();

        //simItemViewHolder.simImage.setImageBitmap(null);
        Picasso.with(myContext).load(image).fit().centerInside().error(R.drawable.placeholder).into(simItemViewHolder.simImage);
        simItemViewHolder.simTitle.setText(title);

        if(shipping == 0){
            simItemViewHolder.simShipping.setText("Free Shipping");
        }else{
            simItemViewHolder.simShipping.setText("$"+String.format("%.2f", shipping));
        }

        simItemViewHolder.simPrice.setText("$"+String.format("%.2f", price));

        if(daysLeft>1){
            simItemViewHolder.simDaysLeft.setText(daysLeft+" Days Left");
        }else{
            simItemViewHolder.simDaysLeft.setText(daysLeft+" Day Left");
        }

    }
    @Override
    public int getItemCount() {
        return mySimItems.size();
    }

    public class simItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView simImage;
        public TextView simTitle;
        public TextView simShipping;
        public TextView simPrice;
        public TextView simDaysLeft;

        public simItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            
            simImage = itemView.findViewById(R.id.simImage);
            simTitle = itemView.findViewById(R.id.simTitle);
            simShipping = itemView.findViewById(R.id.simShipping);
            simPrice = itemView.findViewById(R.id.simPrice);
            simDaysLeft = itemView.findViewById(R.id.simDaysleft);

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
}




