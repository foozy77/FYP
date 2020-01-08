package my.edu.tarc.fyp;

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

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends BaseActivity implements CustomerProfileFragment.OnFragmentInteractionListener {

    LinearLayout dynamicContent,bottonNavBar;
    List<Customer> customer = new ArrayList<>();
    Customer custAdd;
    String URL =MainActivity.URL;
    RecyclerView rvCustomer;
    CustomerAdapter recyclerAdapter;

    TextView fCustName, fCustGender, fCustRating, fCustPhone, fCustAddress, fCustEmail;
    ImageView fCustImg;

    //TextView

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

        /*customer.add(new Customer("foo@email.com","foo"));
        customer.add(new Customer("foo1@email.com","foo1"));
        customer.add(new Customer("foo2@email.com","foo2"));*/
        //rvCustomer.setHasFixedSize(true);

        fCustEmail = findViewById(R.id.f_cust_email);
        fCustAddress = findViewById(R.id.f_cust_address);
        fCustGender = findViewById(R.id.f_cust_gender);
        fCustImg = findViewById(R.id.f_cust_img);
        fCustName = findViewById(R.id.f_cust_name);
        fCustPhone = findViewById(R.id.f_cust_phone);
        fCustRating = findViewById(R.id.f_cust_rating);

        loadAllCust();

    }

    private void loadAllCust(){
        StringRequest request=new StringRequest(Request.Method.GET, URL+"f_displayAllCust", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //customer.clear();
                    JSONArray customerList=new JSONArray(response);
                    for(int i=0;i<customerList.length();i++) {
                        if(customerList.length()==0)
                        {
                            Toast.makeText(CustomerActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONObject cust = customerList.getJSONObject(i);
                            //txt.setText(service.getString("typeName"));
                            String custName = cust.getString("custName");
                            String custEmail = cust.getString("custEmail");
                            //String custPhoneNum = cust.getString("custPhoneNum");
                            String custImg = cust.getString("custImg");
                            custAdd = new Customer(custEmail, custName, custImg);
                            customer.add(custAdd);
                            //Toast.makeText(getBaseContext(),custAdd.getCustEmail(),Toast.LENGTH_SHORT).show();

                        }
                    }

                    rvCustomer = (RecyclerView) findViewById(R.id.rvCustomer);
                    recyclerAdapter = new CustomerAdapter(getBaseContext(), customer);
                    rvCustomer.setAdapter(recyclerAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                    rvCustomer.setLayoutManager(layoutManager);

                    recyclerAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            Bundle bundle = new Bundle();
                            bundle.putString("position", Integer.toString(position));
                            bundle.putString("custEmailClicked", customer.get(position).getCustEmail());// -- to get all info

                            findViewById(R.id.rvCustomer).setVisibility(View.GONE);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment fragment = new CustomerProfileFragment();
                            fragmentTransaction.replace(R.id.fragment_customer_container,fragment);
                            fragment.setArguments(bundle);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            Toast.makeText(CustomerActivity.this,Integer.toString(position) + " = " +customer.get(position).getCustEmail(),Toast.LENGTH_SHORT).show();

                           /* Fragment fragment = new CustomerProfileFragment();
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.fragment_customer_container,fragment);
                            transaction.commit();*/
                        }
                    });


                }catch(Exception ex){
                    Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
            findViewById(R.id.rvAppointment).setVisibility(View.VISIBLE);
        }
    }
}
