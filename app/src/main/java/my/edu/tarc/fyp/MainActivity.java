package my.edu.tarc.fyp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import android.text.format.Formatter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {


    //private String URL="jdbc:mysql//192.168.43.224:3306/innodb";

    public static final String URL = "http://192.168.1.34:5000/";
    private String username = "admin";
    private String pass = "mate4beauty";
    private TextView txt, email, lPassword;
    //private SessionManager session;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.sample);
        email = (TextView)findViewById(R.id.editText);
        lPassword = (TextView)findViewById(R.id.editText2);

        Button btn = (Button) findViewById(R.id.button);
        loadService();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AppointmentActivity.class));
            }
        });

        //WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        //String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress();
    }

    private void loadService(){
        StringRequest request=new StringRequest(Request.Method.GET, URL+"selectServiceType", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try{
                        JSONArray serviceList=new JSONArray(response);
                        for(int i=0;i<serviceList.length();i++){
                            JSONObject service=serviceList.getJSONObject(i);
                            txt.setText(service.getString("typeName"));
                        }
                    }catch(Exception ex){
                        Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
/*
    private void validateLogin(){

        StringRequest strReq = new StringRequest(Request.Method.POST, URL+"login", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {



                        //session.setLogin(true);

                        /*String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");
                        String fullname=user.getString("fullName");
                        int bonusPoint=user.getInt("bonusPoint");
                        String hpNo=user.getString("hpNo");
                        String ic=user.getString("icNo");
                        String hpno=user.getString("hpNo");
                        String address=user.getString("address");
                        String imageString=user.getString("image");
                        User loguser=new User(uid,email,name,fullname,ic,bonusPoint,address,hpno,imageString);
                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                        new UserAsyncTask(loguser).execute();

                        // Launch main activity
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message

                        String errorMsg = jObj.getString("error_msg");

                        Toast toast = Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG);
                        View view = toast.getView();
                        // view.setBackgroundResource(R.drawable.card_gradient);
                        TextView text = (TextView) view.findViewById(android.R.id.message);
                        text.setTextColor(Color.parseColor("#FF0000"));

                        //Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));
                        //toast.show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", lPassword);

                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //mRequestQueue.add(stringRequest);
    }

*/

}


