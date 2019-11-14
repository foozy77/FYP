package my.edu.tarc.fyp;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TestData {

    private String mName;
    private String mDesc;
    private String mPrice;
    private String mDuration;
    private boolean mOnline;
    private TextView textView;

    public TestData(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public TestData(String name, String desc, String price, String duration, boolean online) {
        this.mName = name;
        this.mDesc = desc;
        this.mPrice = price;
        this.mDuration = duration;
        this.mOnline = online;
    }

    public TestData(ServiceAdapter.ViewHolder viewHolder) {


    }


    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getDuration() {
        return mDuration;
    }

    private static int lastContactId = 0;

    public static ArrayList<TestData> createContactsList(int num) {
        ArrayList<TestData> testData = new ArrayList<TestData>();

        for (int i = 1; i <= num; i++) {
            testData.add(new TestData("Number " + ++lastContactId, i <= num/ 2));
        }

        return testData;
    }



    public void expand() {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
        Log.i("Adapter", "expand");
    }

    public void collapse() {
        Log.i("Adapter", "collapse");
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
    }


}

