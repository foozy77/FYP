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
    String logged;

    private TextView txt, txt2, txt3, txt4, lEmail, lPassword;
    private SharedPreferences sharedPreferences;


    //Intent intentImg = new Intent();
    //private RequestQueue mRequestQueue;
    //private SessionManager session;


    //try cust value
    String tryEmail = "Sims4@example.com";
    Uri mImageCaptureUri;

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
        txt = (TextView) findViewById(R.id.sample);
        txt2 = (TextView) findViewById(R.id.sample2);
        image = (ImageView) findViewById(R.id.test_img);
        tryImg = (ImageView) findViewById(R.id.tryImg);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        //check sharedpreferences
         logged = sharedPreferences.getString("logged", "");
        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged","hc@gmail.com");
        editor.commit();*/

        //if user is logged in
        /*if(!logged.isEmpty()){
            Intent intent = new Intent(getApplicationContext(), AppointmentActivity.class);
            startActivity(intent);
            finish();
        }*/

        Button btnLogin = (Button) findViewById(R.id.login);
        Button btnRegister = (Button) findViewById(R.id.register);
        Button btn = (Button) findViewById(R.id.btn);
        Button findPic = (Button) findViewById(R.id.image);
        Button PDF = (Button) findViewById(R.id.pdf);
        Button upload = (Button) findViewById(R.id.upload);
        window = new AlertDialog.Builder(this);

        //loadService();
        loadAllCust();

        PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFile();
            }
        });

        findPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                window.setTitle("Pictures");
                window.setItems(picSelection, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                            {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                            }
                            else
                            {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }

                        }else if(which == 1){
                            pickImage();

                        }
                    }
                });

                window.show();

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AppointmentActivity.class));
            }
        });

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

    private void loadService(){
        StringRequest request=new StringRequest(Request.Method.GET, URL+"f_selectServiceType", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray serviceList=new JSONArray(response);
                    for(int i=0;i<serviceList.length();i++) {
                        JSONObject service = serviceList.getJSONObject(i);
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

    private void loadAllCust(){
        StringRequest request=new StringRequest(Request.Method.GET, URL+"f_displayAllCust", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //customer.clear();
                    JSONArray customerList=new JSONArray(response);
                    for(int i=0;i<customerList.length();i++) {
                        JSONObject cust = customerList.getJSONObject(i);
                        txt.setText(cust.getString("custEmail"));
                        txt2.setText(cust.getString("custName"));

                        //String custName = cust.getString("custName");
                        //String custEmail = cust.getString("custEmail");

                        //Customer custAdd = new Customer(custEmail,custName);
                        //customer.add(custAdd);
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
        });
        Volley.newRequestQueue(this).add(request);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_PHOTO);

    }

    public void pickFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"),REQUEST_PDF);

    }

    private void getImage(){
        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_getImage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //JSONArray serviceList=new JSONArray(response);
                    JSONArray allImg = new JSONArray(response);
                    JSONObject img = allImg.getJSONObject(0);
                    int result = img.getInt("result");
                    //for(int i=0;i<serviceList.length();i++) {
                    //JSONObject service = serviceList.getJSONObject(i);
                    //txt.setText(service.getString("typeName"));}

                    //if found means it matches sho no need compare la wey
                    if (result == 0) {

                        Toast.makeText(MainActivity.this, "error img", Toast.LENGTH_LONG).show();
                    } else {
                        //tryImg.setImageBitmap();
                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.putString("logged",email);
                        //editor.commit();
                        //startActivity(new Intent(MainActivity.this, AppointmentActivity.class));
                        Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_LONG);

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
        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("custEmail", tryEmail);
                //params.put("beauPassword", password);
                return params;
            }

        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Opening camera...", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == MainActivity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri camuri = data.getData();
            String docFilePath = getRealPathFromURI(camuri);
            image.setImageBitmap(photo);
        }
        else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Uri imgeuri = data.getData();
                String docFilePath = getRealPathFromURI(imgeuri);
                image = findViewById(R.id.test_img);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_PDF && resultCode == MainActivity.RESULT_OK)
        {
            Uri uri = data.getData();
            String displayName = null;
            displayName = getFileNameByUri(this, uri);
            txt2.setText(displayName);

            StringRequest request=new StringRequest(Request.Method.POST, URL+"f_tryThrowPDF", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray pdf = new JSONArray(response);
                        JSONObject canPDF = pdf.getJSONObject(0);
                        int result = canPDF.getInt("result");

                        //if found means it matches sho no need compare la wey
                        if (result == 0) {
                            Toast.makeText(MainActivity.this, "error pdf", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "pdf upload", Toast.LENGTH_LONG);

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
                    params.put("beauEmail","foo@gmail.com");
                    params.put("beauDoc",data.getDataString());
                    return params;
                }
            };
            Volley.newRequestQueue(getBaseContext()).add(request);

        }

        //converting image to base64 string
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/
    }

    /*public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void captureImage() {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
            //mImageCaptureUri = Uri.fromFile(f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }*/



    private void tryUploadPDF(){


    }



    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getBaseContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            //Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "do i not get it";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getFileNameByUri(Context context, Uri uri)
    {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0)
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        }
        else
        if (uri.getScheme().compareTo("file") == 0)
        {
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            }
            catch (URISyntaxException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            filepath = uri.getPath();
        }
        return filepath;
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


