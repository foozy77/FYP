package my.edu.tarc.fyp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ProfileActivity extends BaseActivity {

    LinearLayout dynamicContent,bottonNavBar;
    String URL =MainActivity.URL;
    TextView txt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);

       dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_profile, null);
        dynamicContent.addView(wizard);
        txt= findViewById(R.id.beau_name);

        loadProfile();
        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.profile);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_profile_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));

    }

    private void loadProfile(){
        StringRequest request=new StringRequest(Request.Method.GET, URL+"loadProfileInfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray beautician=new JSONArray(response);
                    for(int i=0;i<beautician.length();i++){
                        JSONObject beau=beautician.getJSONObject(i);
                        txt.setText(beau.getString("beauName"));

                    }
                }catch(Exception ex){
                    Toast.makeText(ProfileActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}
