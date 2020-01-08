package my.edu.tarc.fyp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends BaseActivity implements CalendarJobFragment.OnFragmentInteractionListener{

    LinearLayout dynamicContent,bottonNavBar;
    String URL =MainActivity.URL;

    TextView dateEnter;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    final Calendar myCalendar = Calendar.getInstance();
    String passDate;
    List<Appointment> jobs = new ArrayList<>();
    private String appID,appStatus, appLocation, appDate, startTime, endTime, serviceID, custEmail;
    String formattedTodayDate;
    String passDateFormat = "yyyy-MM-dd";


    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;

    RecyclerView rvCalendar;
    CalendarAdapter recyclerAdapter;

    Bitmap bitmap;

    TextView jobTime, jobLoc, jobCust, jobService, jobPrice;
    ImageView jobCustImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calendar);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_calendar, null);
        dynamicContent.addView(wizard);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged","");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        formattedTodayDate = df.format(c);

        jobTime = (TextView)findViewById(R.id.job_time);
        jobCust = (TextView)findViewById(R.id.job_custName);
        jobLoc = (TextView)findViewById(R.id.job_location);
        jobPrice = (TextView)findViewById(R.id.job_serPrice);
        jobService = (TextView)findViewById(R.id.job_serName);
        jobCustImg = (ImageView) findViewById(R.id.job_cust_image);

        dateEnter= (TextView)findViewById(R.id.dateEnter);
        dateEnter.setText(formattedTodayDate);
        //passDate = df2.format(c);
        //try
        passDate = "2018-01-24";
        //Toast.makeText(CalendarActivity.this,"1 " + passDate + " - " +formattedTodayDate,Toast.LENGTH_SHORT).show();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateEnter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CalendarActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateEnter.addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                loadJob();
                //loadCust();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                loadJob();
                //loadCust();
            }
        });

        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.calendar);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_calendar_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        loadJob();
        //loadCust();

    }

    private void updateLabel() {
        String myFormat = "EEEE, yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat(passDateFormat, Locale.ENGLISH);
        dateEnter.setText(sdf.format(myCalendar.getTime()));
        passDate=sdf2.format(myCalendar.getTime());
    }

    public void loadJob()
    {

        /*if("".equals(passDate))
        {
            passDate=formattedTodayDate;
        }*/

        StringRequest strReq = new StringRequest(Request.Method.GET, URL+"f_trydisplayJob", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try{
                    JSONArray loadJobList=new JSONArray(response);
                    for(int i=0;i<loadJobList.length();i++) {
                        JSONObject loadedJob = loadJobList.getJSONObject(i);

                        appID = loadedJob.getString("appID");
                        appStatus = loadedJob.getString("appStatus");
                        appLocation = loadedJob.getString("appLocation");
                        appDate = loadedJob.getString("appDate");
                        startTime = loadedJob.getString("startTime");
                        endTime = loadedJob.getString("endTime");
                        serviceID = loadedJob.getString("appServiceID");
                        custEmail = loadedJob.getString("appCustEmail");

                        Appointment appointment = new Appointment(appID,appStatus, appLocation, appDate, startTime, endTime, serviceID, custEmail);
                        jobs.add(appointment);
                    }

                    rvCalendar = (RecyclerView) findViewById(R.id.rvCalendar);
                    recyclerAdapter = new CalendarAdapter(getBaseContext(), jobs);
                    rvCalendar.setAdapter(recyclerAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                    rvCalendar.setLayoutManager(layoutManager);

                    recyclerAdapter.setOnClickListener(new CalendarAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            //findViewById(R.id.rvCustomer).setVisibility(View.GONE);
                            Bundle bundle = new Bundle();
                            bundle.putString("position", Integer.toString(position));
                            bundle.putString("clCustEmail", jobs.get(position).getCustEmail());// -- to get all info
                            //bundle.putString("appLocClicked", customer.get(position).getCustName());

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment fragment = new CalendarJobFragment();
                            fragmentTransaction.replace(R.id.fragment_calendar_container, fragment);
                            fragment.setArguments(bundle);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            Toast.makeText(CalendarActivity.this,Integer.toString(position) + " = " +jobs.get(position).getCustEmail(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch(Exception ex){
                    Toast.makeText(CalendarActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error.Response", response);
                //Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }); /*{

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appDate",passDate);
                //params.put("beauEmail",userLogged);
                return params;
            }

        };*/
        // Adding request to request queue
        Volley.newRequestQueue(getBaseContext()).add(strReq);
    }

    private void loadCust(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_loadCustomerInfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray customer = new JSONArray(response);
                    if (customer.length() != 0) {
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cust = customer.getJSONObject(i);
                            jobCust.setText(cust.getString("custName"));
                            String imageString = cust.getString("custImg");
                            if (imageString == null) {
                                jobCustImg.setImageResource(R.drawable.ic_pp);
                            } else {
                                try {
                                    byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);

                                    InputStream inputStream = new ByteArrayInputStream(encodeByte);
                                    bitmap = BitmapFactory.decodeStream(inputStream);

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                jobCustImg.setImageBitmap(bitmap);
                            }
                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("custEmail", custEmail);
                return params;
            }
        };
        Volley.newRequestQueue(getBaseContext()).add(request);

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
