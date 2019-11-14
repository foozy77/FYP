package my.edu.tarc.fyp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;*/

import java.util.List;



public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>{

    private List<TestData> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ServiceAdapter(List<TestData> testData) {
        mData = testData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View serviceView = inflater.inflate(R.layout.service_list, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(serviceView);
        return viewHolder;
    }

    public TestData onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View serviceView = inflater.inflate(R.layout.service_list_expand, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(serviceView);
        return new TestData(viewHolder);

    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        TestData service = mData.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.myTextView;
        textView.setText(service.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.service_list);
            itemView.setOnClickListener(this);
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
