package my.edu.tarc.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.android.gms.maps.SupportMapFragment;

public class AppointmentActivity extends BaseActivity implements AppointmentDetailsFragment.OnFragmentInteractionListener {

    LinearLayout dynamicContent,bottonNavBar;
    List<Appointment> appt = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String userLogged;
    AppointmentAdapter adapter;
    RecyclerView rvApp;

    String URL =MainActivity.URL;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    TextView apptDate, apptLocation, apptTime;
    ImageView showSerImg;

    private String appID,appStatus, appLocation, appDate, startTime, endTime, serviceID, custEmail, serviceImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_appointment);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_appointment, null);
        dynamicContent.addView(wizard);

        apptDate= (TextView)findViewById(R.id.app_date);
        apptLocation= (TextView)findViewById(R.id.app_location);
        apptTime =(TextView)findViewById(R.id.app_time);
        showSerImg=(ImageView)findViewById(R.id.app_ser_image);
        rvApp = findViewById(R.id.rvAppointment);

        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.appointment);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_appointment_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged", "");
        /*if(userLogged != null){
            Toast.makeText(AppointmentActivity.this,"Welcome, "+ userLogged,Toast.LENGTH_SHORT).show();
        }*/


        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);


        showAllAppointment();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showAllAppointment(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_trydisplayPendingAppt", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //appt.clear();
                    JSONArray loadAppList=new JSONArray(response);
                    for(int i=0;i<loadAppList.length();i++) {
                        if(loadAppList.length()==0)
                        {
                            Toast.makeText(AppointmentActivity.this,"No appointment(s) found",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONObject loadedJob = loadAppList.getJSONObject(i);
                            appID = loadedJob.getString("appID");
                            appStatus = loadedJob.getString("appStatus");
                            appLocation = loadedJob.getString("appLocation");
                            appDate = loadedJob.getString("appDate");
                            startTime = loadedJob.getString("startTime");
                            endTime = loadedJob.getString("endTime");
                            serviceID = loadedJob.getString("appServiceID");
                            custEmail = loadedJob.getString("appCustEmail");
                            serviceImg = loadedJob.getString("serviceImg");

                            Appointment appointment = new Appointment(appID, appStatus, appLocation, appDate, startTime, endTime, serviceID, custEmail, serviceImg);
                            appt.add(appointment);


                        }
                    }
                    loadRV();

                }catch(Exception ex){
                    Toast.makeText(AppointmentActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(AppointmentActivity.this,"2 "+error.getMessage(),Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(AppointmentActivity.this,
                            "????",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(AppointmentActivity.this, "Error:" + "AuthFailureError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(AppointmentActivity.this, "Error:" + "ServerError", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(AppointmentActivity.this, "Error:" + "NetworkError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(AppointmentActivity.this, "Error:" + "ParseError", Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("beauEmail","Adam.Acker24@example.com");
                params.put("beauEmail",userLogged);
                return params;
            }

        };
        // Adding request to request queue
        Volley.newRequestQueue(this).add(request);
    }


    public void loadRV()
    {
        RecyclerView rvAppointment = (RecyclerView) findViewById(R.id.rvAppointment);
        adapter = new AppointmentAdapter(getBaseContext(),appt);
        rvAppointment.setAdapter(adapter);
        rvAppointment.setLayoutManager(new LinearLayoutManager(getBaseContext()));


        adapter.setOnItemClickListener(new AppointmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Bundle bundle = new Bundle();
                bundle.putString("position", Integer.toString(position));
                bundle.putString("appIDClicked", appt.get(position).getAppID());// -- to get all info
                bundle.putString("appLocClicked", appt.get(position).getAppLocation());
                bundle.putString("appCustEmailClicked", appt.get(position).getCustEmail()); // -- get Name from email
                bundle.putString("appDateClicked", appt.get(position).getAppDate());
                bundle.putString("appStartTimeClicked", appt.get(position).getStartTime());
                bundle.putString("appEndTimeClicked", appt.get(position).getEndTime());
                bundle.putString("appSerClicked", appt.get(position).getServiceID());
                //bundle.putString("appSerPriceClicked", appt.get(position).getAppID()); -- find from serID

                findViewById(R.id.pendingWord).setVisibility(View.GONE);
                findViewById(R.id.rvAppointment).setVisibility(View.GONE);
                //fragment to expand appointment details
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new AppointmentDetailsFragment();
                fragmentTransaction.replace(R.id.fragment_base, fragment);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Toast.makeText(AppointmentActivity.this,Integer.toString(position) + " = " +appt.get(position).getAppID(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
            findViewById(R.id.rvAppointment).setVisibility(View.VISIBLE);
        }
    }

}
