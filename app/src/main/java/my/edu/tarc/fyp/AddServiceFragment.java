package my.edu.tarc.fyp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayAdapter<String> adapter;
    String[] arraySpinner = new String[] {
            "Hair Service", "Facial Service", "Nail Service"
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences sharedPreferences;
    String userLogged;
    int serID;
    int serNum=10000, value;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;
    EditText sName, sDesc, sPrice, sDuration;
    Button btnCreate, btnBrowse;
    String serName, serDesc, serPrice, serDuration, serviceID="";
    final String[] picSelection = {"Take Picture", "Choose from Album"};
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_GALLERY_PHOTO = 11;
    AlertDialog.Builder window;
    ImageView serImgChose;
    Bitmap bitmap;
    String imageString;

    private OnFragmentInteractionListener mListener;

    public AddServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddServiceFragment newInstance(String param1, String param2) {
        AddServiceFragment fragment = new AddServiceFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add, container, false);

        sharedPreferences = getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged","");
        serID = sharedPreferences.getInt("increment",-1);

        sName=(EditText)v.findViewById(R.id.et_name);
        sDesc=(EditText)v.findViewById(R.id.et_desc);
        sPrice=(EditText)v.findViewById(R.id.et_price);
        sDuration=(EditText)v.findViewById(R.id.et_duration);
        btnCreate=(Button)v.findViewById(R.id.btnCreate);
        //btnBrowse=(Button)v.findViewById(R.id.btnBrowseImg);
        //serImgChose=(ImageView)v.findViewById(R.id.insert_image);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createService(v);
            }
        });

        /*btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelection(v);
            }
        });*/
        /*Spinner s = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);*/

        return v;
    }

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

    public void createService(View view)
    {
        serName = sName.getText().toString().trim();
        serDesc = sDesc.getText().toString().trim();
        serDuration = sDuration.getText().toString().trim();
        serPrice = sPrice.getText().toString().trim();

        value=serID++;
        serviceID = "SC"+ Integer.toString(serID);
        value=serID++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("increment",value);
        editor.commit();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL+"f_createNewService", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray loginUser = new JSONArray(response);
                    JSONObject user = loginUser.getJSONObject(0);
                    int result = user.getInt("result");

                    if (result == 0) {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error while creating" , Toast.LENGTH_LONG).show();
                    } else {
                        //save new ID into sharedpref
                        Toast.makeText(getActivity(), "Successfully created", Toast.LENGTH_LONG);
                        startActivity(new Intent(getActivity(), ServiceActivity.class));

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error.Response", response);
                //Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("serviceID", serviceID);
                params.put("serviceName", serName);
                params.put("serviceDesc", serDesc);
                params.put("servicePrice", serPrice);
                params.put("serviceDuration", serDuration);
                //params.put("serviceImg", imageString);
                params.put("beauEmail", userLogged);
                return params;
            }

        };
        // Adding request to request queue
        Volley.newRequestQueue(getActivity()).add(strReq);

    }

    /*public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_PHOTO);

    }

    public void makeSelection(View v)
    {
        window.setTitle("Pictures");
        window.setItems(picSelection, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if (ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "Opening camera...", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            serImgChose.setImageBitmap(bitmap);
        }
        else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                serImgChose.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //Toast.makeText(getContext(), "can convert", Toast.LENGTH_LONG).show();
    }*/

}
