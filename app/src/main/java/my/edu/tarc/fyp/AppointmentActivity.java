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

//import com.google.android.gms.maps.SupportMapFragment;

public class AppointmentActivity extends BaseActivity {

    LinearLayout dynamicContent,bottonNavBar;
    ArrayList<TestData> appt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_appointment);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_appointment, null);
        dynamicContent.addView(wizard);


        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.appointment);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_appointment_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));


        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        RecyclerView rvAppointment = (RecyclerView) wizard.findViewById(R.id.rvAppointment);
        appt = TestData.createContactsList(10);
        AppointmentAdapter adapter = new AppointmentAdapter(appt);
        rvAppointment.setAdapter(adapter);
        rvAppointment.setLayoutManager(new LinearLayoutManager(this));



    }



}
