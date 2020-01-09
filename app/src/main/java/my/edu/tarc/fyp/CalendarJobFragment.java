package my.edu.tarc.fyp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarJobFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button removeApp, rescheduleApp;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;
    EditText cjLoc, cjCustName, cjAppDate, cjAppTime, cjSerName, cjSerDuration, cjAppRequest;
    String newcjLoc, newcjCustName, newcjAppDate, newcjAppTime, newcjSerName, newcjSerDuration, newcjAppRequest;
    String appIDFound, serIDFound, custEmailFound, appDateFound;
    final Calendar myCalendar = Calendar.getInstance();
    String url;
    SharedPreferences sharedPreferences;
    String userLogged;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TimePickerDialog picker;
    private TimePickerDialog mTimePicker;
    int minutes;
    Calendar mcurrentTime;


    private OnFragmentInteractionListener mListener;

    public CalendarJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarJobFragment newInstance(String param1, String param2) {
        CalendarJobFragment fragment = new CalendarJobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            custEmailFound = getArguments().getString("passCustEmail");
            appIDFound = getArguments().getString("passAppID");
            serIDFound = getArguments().getString("passSerID");
            appDateFound = getArguments().getString("passAppDate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_job, container, false);

        sharedPreferences = getContext().getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged","");

        removeApp = (Button) view.findViewById(R.id.btnDeleteApp);
        rescheduleApp = (Button) view.findViewById(R.id.btnRescheduleApp);

        cjAppDate = (EditText) view.findViewById(R.id.cj_appDate);
        cjCustName = (EditText) view.findViewById(R.id.cj_cust);
        cjAppRequest = (EditText) view.findViewById(R.id.cj_appRequest);
        cjLoc = (EditText) view.findViewById(R.id.cj_loc);
        cjAppTime = (EditText) view.findViewById(R.id.cj_seTime);
        cjSerName = (EditText) view.findViewById(R.id.cj_serChose);
        cjSerDuration = (EditText) view.findViewById(R.id.cj_serDuration);

        url=URL+"f_trydisplayJob?appDate="+appDateFound+"&beauEmail="+userLogged;;


        cjAppDate.setFocusable(false);
        cjAppTime.setFocusable(false);
        //cjLoc.setFocusable(false);

        cjCustName.setEnabled(false);
        cjAppRequest.setEnabled(false);
        cjSerName.setEnabled(false);
        cjSerDuration.setEnabled(false);


        removeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialog();
            }
        });

        rescheduleApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rescheduleAppointment();
            }
        });

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

        cjAppDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        cjAppTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(selectedHour<9||selectedHour>21)
                        {
                            cjAppTime.setText(" ");
                            //Toast.makeText(getActivity(),"Out of service Time",Toast.LENGTH_SHORT).show();
                            Toast toast = Toast.makeText(getActivity(),"           Out of service Time\nService time is from 0900 - 2100", Toast.LENGTH_SHORT );
                            View view = toast.getView();
                            TextView text = (TextView) view.findViewById(android.R.id.message);
                            text.setTextColor(Color.parseColor("#FF0000"));

                            toast.show();
                        }
                        else
                            cjAppTime.setText(String.format("%02d:%02d",selectedHour, selectedMinute));
                        //Toast.makeText(BookingActivity.this, "-->"+timeData.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();


            }
        });

        //loadJob();
        showOneJob();

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        //SimpleDateFormat sdf2 = new SimpleDateFormat(passDateFormat, Locale.ENGLISH);
        cjAppDate.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */



    private void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to remove the appointment?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                cancelAppointment();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cancelAppointment(){

        //eSID.setEnabled(false);
        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_removeApp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray appts = new JSONArray(response);
                    JSONObject appt = appts.getJSONObject(0);
                    int result = appt.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getActivity(), "error.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Appointment Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), CalendarActivity.class);
                        getActivity().startActivity(intent);
                        startActivity(new Intent(getActivity(), CalendarActivity.class));

                    }
                } catch(Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appID", appIDFound);
                return params;

            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }


    private void rescheduleAppointment(){



        newcjSerName = cjSerName.getText().toString().trim();
        newcjSerDuration = cjSerDuration.getText().toString().trim();
        newcjAppRequest = cjAppRequest.getText().toString().trim();
        newcjCustName = cjCustName.getText().toString().trim();
        newcjAppDate = cjAppDate.getText().toString().trim();
        newcjAppTime = cjAppTime.getText().toString().trim();
        newcjLoc = cjLoc.getText().toString().trim();

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_rescheduleApp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray newService = new JSONArray(response);
                    JSONObject service = newService.getJSONObject(0);
                    int result = service.getInt("result");

                    if (result == 0) {
                        Toast.makeText(getActivity(), "error.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Appointment Updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), CalendarActivity.class);
                        getActivity().startActivity(intent);
                        startActivity(new Intent(getActivity(), CalendarActivity.class));

                    }
                } catch(Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appID", appIDFound);
                params.put("appDate",newcjAppDate);
                params.put("startTime",newcjAppTime);
                params.put("appLocation",newcjLoc);
                return params;

            }
        };
        Volley.newRequestQueue(getContext()).add(request);
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
                            cjCustName.setText(cust.getString("custName"));

                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("custEmail", custEmailFound);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);

    }

    private void loadService(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_loadService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray service = new JSONArray(response);
                    if (service.length() != 0) {
                        for (int i = 0; i < service.length(); i++) {
                            JSONObject ser = service.getJSONObject(i);
                            cjSerName.setText(ser.getString("serviceName"));
                            //Double thePrice = ser.getDouble("servicePrice");
                            cjSerDuration.setText(ser.getString("serviceDuration") + "minutes" );

                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("serviceID", serIDFound);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void loadJob()
    {

        /*if("".equals(passDate))
        {
            passDate=formattedTodayDate;
        }*/
        //jobs.clear();
        Toast.makeText(getActivity(),appIDFound+" "+userLogged,Toast.LENGTH_SHORT).show();

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray joblist = new JSONArray(response);
                    if (joblist.length() != 0) {
                        for (int i = 0; i < joblist.length(); i++) {
                            JSONObject job = joblist.getJSONObject(i);
                            cjAppDate.setText(appDateFound);
                            cjAppTime.setText(job.getString("startTime"));
                            cjLoc.setText(job.getString("appLocation"));
                            cjCustName.setText(job.getString("custName"));
                            cjSerName.setText(job.getString("serviceName"));
                            cjSerDuration.setText(job.getString("serviceDuration")+ " minutes");
                            cjAppRequest.setText(job.getString("appRequest"));

                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error.Response", response);
                //Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });/*{

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appDate",passDate);
                params.put("beauEmail",userLogged);
                Log.i("Sending",params.toString());
                return params;
            }

        };*/
        // Adding request to request queue
        Volley.newRequestQueue(getActivity()).add(strReq);
    }


    public void showOneJob()
    {
        Toast.makeText(getActivity(),appIDFound+" "+userLogged,Toast.LENGTH_SHORT).show();

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_showOneJob", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray joblist = new JSONArray(response);
                    if (joblist.length() != 0) {
                        for (int i = 0; i < joblist.length(); i++) {
                            JSONObject job = joblist.getJSONObject(i);
                            cjAppDate.setText(appDateFound);
                            cjAppTime.setText(job.getString("startTime"));
                            cjLoc.setText(job.getString("appLocation"));
                            cjCustName.setText(job.getString("custName"));
                            cjSerName.setText(job.getString("serviceName"));
                            cjSerDuration.setText(job.getString("serviceDuration")+ " minutes");
                            cjAppRequest.setText(job.getString("appRequest"));

                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(),
                            "????",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Error:" + "AuthFailureError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Error:" + "ServerError", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Error:" + "NetworkError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Error:" + "ParseError", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appID", appIDFound);
                params.put("beauEmail", userLogged);
                Log.i("Sending",params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    //supposed to validate date to reschedule

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
