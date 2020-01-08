package my.edu.tarc.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;*/

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//implements OnMapReadyCallback

public class AppointmentDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String URL = MainActivity.URL;
    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    Button acceptApp, rejectApp;
    String appIDFound, cEmailFound, sIDFound;
    TextView appCust, appDate, appLoc, appTime, appSerChosen, appSerPrice;

    String newAppSTime, newAppDate;
    ArrayList<String> allAppTime = new ArrayList<String>();

    /*MapView mMapView;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";*/

    private OnFragmentInteractionListener mListener;

    public AppointmentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentDetailsFragment newInstance(String param1, String param2) {
        AppointmentDetailsFragment fragment = new AppointmentDetailsFragment();
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
            appIDFound = getArguments().getString("appIDClicked");
            cEmailFound = getArguments().getString("appCustEmailClicked");
            sIDFound = getArguments().getString("appSerClicked");
            newAppDate = getArguments().getString("appDateClicked");
            newAppSTime = getArguments().getString("appStartTimeClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_details, container, false);

        appCust = (TextView) view.findViewById(R.id.app_cust);
        appDate = (TextView) view.findViewById(R.id.appDate);
        appLoc = (TextView) view.findViewById(R.id.app_loc);
        appTime = (TextView) view.findViewById(R.id.app_seTime);
        appSerChosen = (TextView) view.findViewById(R.id.app_serChose);
        appSerPrice = (TextView) view.findViewById(R.id.app_serPrice);

        acceptApp = (Button) view.findViewById(R.id.btnAccept);
        rejectApp = (Button) view.findViewById(R.id.btnReject);

        //mMapView = (MapView) view.findViewById(R.id.mapView);
        //mMapView.onCreate(savedInstanceState);
        //mMapView.getMapAsync(this);
        //initMap(savedInstanceState);  //this replace all upthere

        loadAppInfo();
        loadCust();
        loadService();

        acceptApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAppointment();
            }
        });

        rejectApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectAppointment();
            }
        });

        return view;
    }

    /*public void initMap(Bundle savedInstanceState) {

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /*@Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }*/

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadAppInfo()
    {
        StringRequest request=new StringRequest(Request.Method.POST, URL+"loadAppDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray appointment = new JSONArray(response);
                    if (appointment.length() != 0) {
                        for (int i = 0; i < appointment.length(); i++) {
                            JSONObject app = appointment.getJSONObject(i);
                            appLoc.setText(app.getString("appLocation"));
                            String theDate = app.getString("appDate");
                            //Date theAppDate = new SimpleDateFormat("HH:mm:ss").parse(theDate);
                            //theDate
                            //SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                            //Date date = inputFormat.parse(theDate);
                            //appDate.setText(date.toString());
                            appDate.setText(app.getString("appDate"));
                            String duration = app.getString("startTime") + " - " + app.getString("endTime");
                            appTime.setText(duration);
                            Toast.makeText(getActivity(),theDate,Toast.LENGTH_SHORT).show();
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
                params.put("appID", appIDFound);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);

    }

    private void loadCust(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"loadCustomerInfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray customer = new JSONArray(response);
                    if (customer.length() != 0) {
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cust = customer.getJSONObject(i);
                            appCust.setText(cust.getString("custName"));
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
                params.put("custEmail", cEmailFound);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    private void loadService(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"loadService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray service = new JSONArray(response);
                    if (service.length() != 0) {
                        for (int i = 0; i < service.length(); i++) {
                            JSONObject ser = service.getJSONObject(i);
                            appSerChosen.setText(ser.getString("serviceName"));
                            Double thePrice = ser.getDouble("servicePrice");
                            appSerPrice.setText("RM " + String.format("%.2f", thePrice));

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
                params.put("serviceID", sIDFound);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void confirmAppointment() {

        int accept = validateAcceptApp();

        if (accept == 0) {
            Toast.makeText(getActivity(), "Appointment Time Clashed", Toast.LENGTH_LONG).show();
        } else {

            StringRequest request = new StringRequest(Request.Method.POST, URL + "confirmAppt", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray appList = new JSONArray(response);
                        JSONObject appFound = appList.getJSONObject(0);
                        int result = appFound.getInt("result");

                        if (result == 0) {
                            //progressDialog.dismiss();
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                        } else {
                            //progressDialog.hide();

                            Intent myIntent = new Intent(getActivity(), AppointmentActivity.class);
                            startActivity(myIntent);
                            Toast.makeText(getActivity(), "Appointment Accepted", Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //get the appointment ID from onclick
                    params.put("appID", appIDFound);
                    //params.put("appStatus", "Done");
                    return params;
                }
            };

            Volley.newRequestQueue(getActivity()).add(request);
        }
    }

    public int validateAcceptApp() {
        StringRequest request=new StringRequest(Request.Method.POST, URL+"tryGetAllAppTime", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //customer.clear();
                    JSONArray numApp=new JSONArray(response);
                    for(int i=0;i<numApp.length();i++) {
                        JSONObject appTime = numApp.getJSONObject(i);
                        String time1 = appTime.getString("startTime");
                        String time2 = appTime.getString("endTime");

                        /*for(int s=0;s<numApp.length();s+=2)
                        {
                            int e = s+1;
                            allAppTime.add(s,time1);
                            allAppTime.add(e,time2);
                        }*/
                        allAppTime.add(time1);
                        allAppTime.add(time2);
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
        });
        Volley.newRequestQueue(getActivity()).add(request);


        for(int loop=0; loop < allAppTime.size()/2; loop++) {
            int a=0, b=1;
            try {
                String string1 = allAppTime.get(a);
                Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                String string2 = allAppTime.get(b);
                Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);

                String someRandomTime = newAppSTime;
                Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();
                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    return 9;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;

    }

    public void rejectAppointment(){
        //RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request=new StringRequest(Request.Method.POST, URL+"rejectAppt", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray appList=new JSONArray(response);
                    JSONObject appFound = appList.getJSONObject(0);
                    int result = appFound.getInt("result");
                    //get index of array list to find appID then set to appIDFound
                    //appIDFound = appFound.getString("appID");

                    //if found means it matches sho no need compare la wey
                    if (result == 0) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                    } else {
                        //progressDialog.hide();

                        Intent myIntent = new Intent(getActivity(), AppointmentActivity.class);
                        startActivity(myIntent);
                        //Snackbar.make(getView(), "Appointment Rejected", Snackbar.LENGTH_LONG).setAction("Undo", null).show();
                        Toast.makeText(getActivity(), "Appointment Rejected", Toast.LENGTH_LONG).show();
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //get the appointment ID from onclick
                params.put("appID",appIDFound);
                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(request);

    }
}
