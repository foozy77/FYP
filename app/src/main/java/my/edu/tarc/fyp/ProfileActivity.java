package my.edu.tarc.fyp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity implements EditProfileFragment.OnFragmentInteractionListener{

    LinearLayout dynamicContent,bottonNavBar;
    String URL =MainActivity.URL;
    TextView beauName, beauEmail, beauPhone, beauSpec, beauRating, beauAddress ;
    ImageView beauImg;
    String imageString;
    SharedPreferences sharedPreferences;
    String userLogged;
    Bitmap bitmap;
    RatingBar beauStar;
    String showBeauName;
    public static final String FILE_NAME = MainActivity.FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_profile, null);
        dynamicContent.addView(wizard);
        beauImg= findViewById(R.id.beau_img);
        beauName= findViewById(R.id.beau_name);
        beauSpec= findViewById(R.id.beau_spec);
        beauRating= findViewById(R.id.beau_rating);
        beauPhone= findViewById(R.id.beau_phone);
        beauEmail= findViewById(R.id.beau_email);
        beauAddress= findViewById(R.id.beau_address);
        beauStar=findViewById(R.id.beau_star);
        //beauImg = findViewById(R.id.beau_img);
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged","");
        showBeauName = sharedPreferences.getString("showBeauName","");


        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.profile);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_profile_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        loadProfile();

        //if there is empty credentials, prompt to update profile

        Button btnEdit = findViewById(R.id.btnEditProfile);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findViewById(R.id.profile_gone).setVisibility(View.GONE);

                // 1. Create Fragment Manager
                FragmentManager fragmentManager = getSupportFragmentManager();
                // 2. Create Fragment Transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 3. Create an instance of a fragment - change according to class name
                Fragment fragment = new EditProfileFragment();
                // 4. Perform fragment transaction
                fragmentTransaction.replace(R.id.fragment_base, fragment);
                // 5. Commit transaction
                fragmentTransaction.commit();
            }
        });


    }

    private void loadProfile(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_loadProfileInfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray beautician=new JSONArray(response);
                    if(beautician.length()!=0)
                    {
                        for (int i = 0; i < beautician.length(); i++) {
                            JSONObject beau = beautician.getJSONObject(i);
                            //String user = beau.getString("beauEmail");
                            //beauSpec.setText(beau.getString("beauSpec"));
                            beauRating.setText(beau.getString("beauRating"));
                            beauName.setText(beau.getString("beauName"));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("showBeauName",beau.getString("beauName"));
                            editor.commit();
                            beauPhone.setText(beau.getString("beauPhoneNum"));
                            beauEmail.setText(beau.getString("beauEmail"));
                            beauAddress.setText(beau.getString("beauAddress"));
                            beauStar.setRating(Float.parseFloat(beau.getString("beauRating")));
                            imageString = beau.getString("beauImg");

                            if(imageString==null)
                            {
                                beauImg.setImageResource(R.drawable.ic_pp);
                            }
                            else {
                                try {
                                    byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);

                                    InputStream inputStream = new ByteArrayInputStream(encodeByte);
                                    bitmap = BitmapFactory.decodeStream(inputStream);

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                beauImg.setImageBitmap(bitmap);
                            }
                            //GET BEAU SPEC
                            String spec = beau.getString("sTypeID");
                            switch(spec) {
                                case "ST3019":
                                    //serviceType = "Nail";
                                    //sTypeID="ST3019";
                                    beauSpec.setText("Nail Artist");
                                    break;
                                case "ST4005":
                                    //serviceType = "Body-Massage";
                                    //sTypeID="ST4005";
                                    beauSpec.setText("Body Massager");
                                    break;
                                case "ST4473":
                                    //serviceType = "Hair";
                                    //sTypeID="ST4473";
                                    beauSpec.setText("Hair Artist");
                                    break;
                                case "ST8047":
                                    //serviceType = "Facial";
                                    //sTypeID="ST8047";
                                    beauSpec.setText("Face Aestheticians");
                                    break;
                            }

                        }
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
            findViewById(R.id.rvAppointment).setVisibility(View.VISIBLE);
        }
    }
}
