package my.edu.tarc.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{

    private Context mContext;
    private List<Customer> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemClickListener mListener;
    Bitmap bitmap;

    public CustomerAdapter(Context mContext,List<Customer> mData) {
        this.mContext = mContext;
        this.mData=mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.customer_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view,mListener);
        return viewHolder;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView custName,custEmail;
        ImageView custImg;
        //Button btnViewProfile;

        // inflates the row layout from xml when needed


        ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);
            custName = itemView.findViewById(R.id.cust_name);
            custEmail = itemView.findViewById(R.id.cust_email);
            custImg = itemView.findViewById(R.id.cust_image);
            //btnViewProfile = itemView.findViewById(R.id.btnView);
            //mContext = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    /*public void bindData(Customer customer) {
        String imageString;
        custName.setText(customer.getCustName());
        custEmail.setText(customer.getCustEmail());
        imageString = customer.getCustImg();
        try{
            byte [] encodeByte=Base64.decode(imageString,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            bitmap= BitmapFactory.decodeStream(inputStream);

        }catch(Exception e){
            e.getMessage();
        }
        custImg.setImageBitmap(bitmap);
    }*/


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String imageString;
        viewHolder.custName.setText(mData.get(position).getCustName());
        viewHolder.custEmail.setText(mData.get(position).getCustEmail());
        imageString = mData.get(position).getCustImg();

        if(imageString==null)
        {
            viewHolder.custImg.setImageResource(R.drawable.ic_pp);
        }
        else {
            try {
                byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);

                InputStream inputStream = new ByteArrayInputStream(encodeByte);
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (Exception e) {
                e.getMessage();
            }
            viewHolder.custImg.setImageBitmap(bitmap);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    /*
    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }*/

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
