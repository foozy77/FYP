package my.edu.tarc.fyp;


import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class ServiceActivity extends BaseActivity {

    LinearLayout dynamicContent,bottonNavBar;
    ArrayList<TestData> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_appointment);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_service, null);
        dynamicContent.addView(wizard);


        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.service);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_service_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        RecyclerView rvService = (RecyclerView) wizard.findViewById(R.id.rvService);
        // Initialize contacts
        service = TestData.createContactsList(10);
        // Create adapter passing in the sample user data
        ServiceAdapter adapter = new ServiceAdapter(service);
        // Attach the adapter to the recyclerview to populate items
        rvService.setAdapter(adapter);
        // Set layout manager to position the items
        rvService.setLayoutManager(new LinearLayoutManager(this));
        // That's all!


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                // 1. Create Fragment Manager
                FragmentManager fragmentManager = getSupportFragmentManager();
                 // 2. Create Fragment Transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 3. Create an instance of a fragment - change according to class name
                Fragment fragment = new AddServiceFragment();
                // 4. Perform fragment transaction
                fragmentTransaction.replace(R.id.fragment_container_add, fragment);
                // 5. Commit transaction
                fragmentTransaction.commit();
            }
        });



    }





}
