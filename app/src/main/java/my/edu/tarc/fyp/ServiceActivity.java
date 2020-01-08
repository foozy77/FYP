package my.edu.tarc.fyp;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceActivity extends BaseActivity implements AddServiceFragment.OnFragmentInteractionListener,ServiceExpandFragment.OnFragmentInteractionListener{

    LinearLayout dynamicContent,bottonNavBar;
    List<Service> allService=new ArrayList<>();
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;
    private String sID, sName, sDesc;
    private double sPrice;
    private int sDuration;
    ServiceAdapter adapter;
    SharedPreferences sharedPreferences;
    String userLogged;

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

        /*allService.add(new Service("Cut","Super Super Cut"));*/

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged", "");

        loadService();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                findViewById(R.id.rvService).setVisibility(View.GONE);
                findViewById(R.id.fab).setVisibility(View.GONE);

                // 1. Create Fragment Manager
                FragmentManager fragmentManager = getSupportFragmentManager();
                // 2. Create Fragment Transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 3. Create an instance of a fragment - change according to class name
                Fragment fragment = new AddServiceFragment();
                // 4. Perform fragment transaction
                fragmentTransaction.replace(R.id.fragment_service_container, fragment);
                // 5. Commit transaction
                fragmentTransaction.commit();
            }
        });

    }

    private void loadService(){
        //RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_showService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray serviceList=new JSONArray(response);
                    for(int i=0;i<serviceList.length();i++) {
                        if(serviceList.length()==0)
                        {
                            Toast.makeText(ServiceActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONObject service = serviceList.getJSONObject(i);
                            sID = (service.getString("serviceID"));
                            sName = (service.getString("serviceName"));
                            sDesc = (service.getString("serviceDesc"));
                            sDuration = (service.getInt("serviceDuration"));
                            sPrice = (service.getDouble("servicePrice"));
                            Service newService = new Service(sID, sName, sDesc, sPrice, sDuration);
                            allService.add(newService);
                        }
                    }
                    RecyclerView rvService = (RecyclerView) findViewById(R.id.rvService);
                    adapter = new ServiceAdapter(getBaseContext(),allService);
                    rvService.setAdapter(adapter);
                    rvService.setLayoutManager(new LinearLayoutManager(getBaseContext()));

                    adapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            findViewById(R.id.rvService).setVisibility(View.GONE);
                            findViewById(R.id.fab).setVisibility(View.GONE);
                            Bundle bundle = new Bundle();
                            bundle.putString("position", Integer.toString(position));
                            bundle.putString("serIDClicked", allService.get(position).getsID());// -- to get all info

                            //fragment to expand appointment details
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment fragment = new ServiceExpandFragment();
                            fragmentTransaction.replace(R.id.fragment_service_container, fragment);
                            fragment.setArguments(bundle);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                    });

                }catch(Exception ex){
                    Toast.makeText(ServiceActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("beauEmail", userLogged);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
            findViewById(R.id.rvService).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }
    }


}
