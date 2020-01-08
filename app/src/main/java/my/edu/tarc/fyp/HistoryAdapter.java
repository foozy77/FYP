package my.edu.tarc.fyp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private Context mContext;
    private List<History> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemClickListener mListener;

    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;

    public HistoryAdapter(Context mContext,List<History> mData) {

        this.mContext = mContext;
        this.mData=mData;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View historyView = inflater.inflate(R.layout.history_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(historyView, mListener);
        return viewHolder;
    }

    /*public TestData onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View serviceView = inflater.inflate(R.layout.service_list_expand, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(serviceView);
        return new TestData(viewHolder);

    }*/

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView transID, transDateTime, transAmt, transCust;

        ViewHolder(View itemView, HistoryAdapter.OnItemClickListener listener) {
            super(itemView);
            transID= itemView.findViewById(R.id.trans_ID);
            transDateTime= itemView.findViewById(R.id.trans_DateTime);
            transAmt= itemView.findViewById(R.id.trans_Amt);
            transCust= itemView.findViewById(R.id.trans_Cust);
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


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        History history = mData.get(position);
        viewHolder.transID.setText(mData.get(position).getTransID());
        viewHolder.transDateTime.setText(mData.get(position).getTransDateTime().toString());
        viewHolder.transAmt.setText(String.valueOf(mData.get(position).getTransAmount()));
        //viewHolder.transCust.setText(mData.get(position).());

    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
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
