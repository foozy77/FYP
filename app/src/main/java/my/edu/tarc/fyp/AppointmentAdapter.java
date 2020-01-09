package my.edu.tarc.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{

    private Context mContext;
    private List<Appointment> mData;
    private List<Customer> mCustomer;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemClickListener mListener;
    Bitmap bitmap;


    public AppointmentAdapter(Context mContext,List<Appointment> mData) {

        this.mContext = mContext;
        this.mData=mData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View appView = inflater.inflate(R.layout.appointment_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(appView, mListener);
        return viewHolder;
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Appointment appt = mData.get(position);
        String duration = mData.get(position).getStartTime() + " - " + mData.get(position).getEndTime();
        viewHolder.appLocation.setText("Location : " + mData.get(position).getAppLocation());
        viewHolder.appDate.setText("Date : " + mData.get(position).getAppDate());
        viewHolder.appTime.setText("Time : " + mData.get(position).getStartTime());
        String imgString = mData.get(position).getServiceImg();

        if (imgString == null) {
            viewHolder.appSerImg.setImageResource(R.drawable.default_ser_img);
        } else {
            try {
                byte[] encodeByte = Base64.decode(imgString, Base64.DEFAULT);

                InputStream inputStream = new ByteArrayInputStream(encodeByte);
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (Exception e) {
                e.getMessage();
            }
            viewHolder.appSerImg.setImageBitmap(bitmap);
        }
    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    //clicking stuff

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView appLocation, appDate, appTime;
        ImageView appSerImg;
        //CardView cvApp;

        ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            appLocation = itemView.findViewById(R.id.app_location);
            appDate = itemView.findViewById(R.id.app_date);
            appTime = itemView.findViewById(R.id.app_time);
            appSerImg = itemView.findViewById(R.id.app_ser_image);
            //cvApp = (CardView)itemView.findViewById(R.id.cvApp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
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

