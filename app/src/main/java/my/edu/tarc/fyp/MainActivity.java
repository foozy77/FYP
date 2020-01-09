package my.edu.tarc.fyp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import android.text.format.Formatter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements RegisterFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener{

    //private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    //private String URL="jdbc:mysql//192.168.43.224:3306/innodb";

    private static final int REQUEST_PDF = 1;
    public static final int PICK_IMAGE = 1;
    public static final String FILE_NAME = "my.edu.tarc.fyp";//package name
    public static final String URL = "http://192.168.0.143:5000/";
    private String username = "admin";
    private String pass = "mate4beauty";
    String logged, showBeauName;

    private TextView txt, txt2, txt3, txt4, lEmail, lPassword;
    private SharedPreferences sharedPreferences;

    //Intent intentImg = new Intent();
    //private RequestQueue mRequestQueue;
    //private SessionManager session;


    final String[] picSelection = {"Take Picture", "Choose from Album"};
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_GALLERY_PHOTO = 11;
    ImageView image, tryImg;
    Bitmap bitmap;
    String imageString;
    AlertDialog.Builder window;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        //check sharedpreferences
         logged = sharedPreferences.getString("logged", "");
        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged","hc@gmail.com");
        editor.commit();*/

        //if user is logged in
        if(!logged.isEmpty()){
            Intent intent = new Intent(getApplicationContext(), AppointmentActivity.class);
            startActivity(intent);
            finish();
        }

        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnRegister = (Button) findViewById(R.id.register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.mainGone).setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragment_main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.mainGone).setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new LoginFragment();
                fragmentTransaction.replace(R.id.fragment_main_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        //WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        //String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress();
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
            findViewById(R.id.mainGone).setVisibility(View.VISIBLE);
        }
    }

}


