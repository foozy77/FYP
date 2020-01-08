package my.edu.tarc.fyp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String userLogged;
    String custEmailFound;
    List<History> allHistory =new ArrayList<>();
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;

    String myFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    TextView transID, transDateTime, transAmt, transCust;

    RecyclerView rvHistory;
    HistoryAdapter recyclerAdapter;

    private String tID, tMethod, tCom, tpromo, tAppID;
    private double tRating, tAmount;
    private LocalDateTime tDateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged","");

        transID= (TextView)findViewById(R.id.trans_ID);
        transDateTime= (TextView)findViewById(R.id.trans_DateTime);
        transAmt= (TextView)findViewById(R.id.trans_Amt);
        transCust= (TextView)findViewById(R.id.trans_Cust);

        loadHistory();




    }


    public void loadHistory()
    {
        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_tryLoadHistory", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray history = new JSONArray(response);
                    if (history.length() != 0) {
                        for (int i = 0; i < history.length(); i++) {
                            JSONObject trans = history.getJSONObject(i);
                            tID = trans.getString("transID");
                            tDateTime = LocalDateTime.parse(trans.getString("transDateTime"));
                            tAmount=Double.parseDouble(trans.getString("transAmount"));

                            History historyFound = new History(tID,tAmount,tDateTime);
                            allHistory.add(historyFound);

                        }
                    }
                    rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
                    recyclerAdapter = new HistoryAdapter(getBaseContext(), allHistory);
                    rvHistory.setAdapter(recyclerAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                    rvHistory.setLayoutManager(layoutManager);

                }catch(Exception ex){
                    Toast.makeText(getBaseContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });/* {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("beauEmail", userLogged);
                return params;
            }
        };*/
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

}
