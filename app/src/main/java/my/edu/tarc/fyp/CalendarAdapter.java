package my.edu.tarc.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private Context mContext;
    private List<CalendarJob> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemClickListener mListener;
    Bitmap bitmap;

    public CalendarAdapter(Context mContext,List<CalendarJob> mData) {

        this.mContext = mContext;
        this.mData=mData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View appView = inflater.inflate(R.layout.calendar_job_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(appView, mListener);
        return viewHolder;
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        CalendarJob calendarJob = mData.get(position);
        //String duration = mData.get(position).getStartTime() + " - " + mData.get(position).getEndTime();
        viewHolder.jobLocation.setText("Location : " + mData.get(position).getAppLocation());
        viewHolder.jobTime.setText("Time : " +mData.get(position).getStartTime());
        viewHolder.jobCustName.setText("Customer : "+mData.get(position).getCustName());
        viewHolder.jobSerName.setText("Service : " +mData.get(position).getServiceName());
        //viewHolder.thePrice = mData.get(position).getServicePrice();
        viewHolder.jobSerPrice.setText("Price : RM " + String.format("%.2f",mData.get(position).getServicePrice()));
        viewHolder.jobAppStatus.setText("Status : " + mData.get(position).getAppStatus());
        String imgString = mData.get(position).getCustImage();
        if(imgString==null)
        {
            viewHolder.jobCustImg.setImageResource(R.drawable.ic_pp);
        }
        else {
            try {
                byte[] encodeByte = Base64.decode(imgString, Base64.DEFAULT);
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();

                InputStream inputStream = new ByteArrayInputStream(encodeByte);
                //bitmap.compress(Bitmap.CompressFormat.PNG, 1, stream);
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (Exception e) {
                e.getMessage();
            }
            viewHolder.jobCustImg.setImageBitmap(bitmap);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    //clicking stuff
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jobLocation, jobDate, jobTime, jobCustName, jobSerName, jobSerPrice, jobAppStatus;
        ImageView jobCustImg;
        //Double thePrice;
        //CardView cvApp;

        ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            jobLocation = itemView.findViewById(R.id.job_location);
            jobTime = itemView.findViewById(R.id.job_time);
            jobCustImg = itemView.findViewById(R.id.job_cust_image);
            jobSerName = itemView.findViewById(R.id.job_serName);
            jobSerPrice = itemView.findViewById(R.id.job_serPrice);
            jobCustName =itemView.findViewById(R.id.job_custName);
            jobAppStatus =itemView.findViewById(R.id.job_appStat);
            //cvApp = (CardView)itemView.findViewById(R.id.cvApp);

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

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateList(List<CalendarJob> data)
    {
        mData=data;
        notifyDataSetChanged();
    }
}
