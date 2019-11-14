package my.edu.tarc.fyp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class CustomerActivity extends BaseActivity {

    LinearLayout dynamicContent,bottonNavBar;
    ArrayList<TestData> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_appointment);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_customer, null);
        dynamicContent.addView(wizard);

        //get the reference of RadioGroup.
        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.customer);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_customer_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));



        RecyclerView rvCustomer = (RecyclerView) wizard.findViewById(R.id.rvCustomer);
        // Initialize contacts
        contacts = TestData.createContactsList(10);
        // Create adapter passing in the sample user data
        CustomerAdapter adapter = new CustomerAdapter(contacts);
        // Attach the adapter to the recyclerview to populate items
        rvCustomer.setAdapter(adapter);
        // Set layout manager to position the items
        rvCustomer.setLayoutManager(new LinearLayoutManager(this));
        // That's all!

    }
}
