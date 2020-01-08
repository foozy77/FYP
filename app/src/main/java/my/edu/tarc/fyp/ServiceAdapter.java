package my.edu.tarc.fyp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/*import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;*/

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{

    private Context mContext;
    private List<Service> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private OnItemClickListener mListener;
    //private ServiceAdapter.OnItemClickListener mListener;

    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;
    TextView sList;

    public ServiceAdapter(Context mContext,List<Service> mData) {

        this.mContext = mContext;
        this.mData=mData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View serviceView = inflater.inflate(R.layout.service_list, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(serviceView, mListener);
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
        TextView serName;

        ViewHolder(View itemView, ServiceAdapter.OnItemClickListener listener) {
            super(itemView);
            serName = itemView.findViewById(R.id.service_list);
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
        Service service = mData.get(position);
        viewHolder.serName.setText(mData.get(position).getsName());

    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
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
