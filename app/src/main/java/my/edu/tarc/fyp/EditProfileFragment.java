package my.edu.tarc.fyp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ProgressDialog progressDialog;

    final String[] picSelection = {"Take Picture", "Choose from Album"};
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_GALLERY_PHOTO = 11;
    ImageView profilePic;
    Button btnSaveProfile;
    EditText eEmail, ePassword, eGender, eName, ePhone, eAddress;
    String newPass, newName, newPhone, newAddress, newGender, newImage;
    ImageView eBeauImg;
    TextView eBeauSpec;
    Bitmap bitmap;
    String imageString;
    AlertDialog.Builder window;
    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

        //get user
        sharedPreferences = getContext().getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        userLogged = sharedPreferences.getString("logged","");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profilePic = (ImageView) view.findViewById(R.id.edit_pic);
        btnSaveProfile = (Button) view.findViewById(R.id.btnSaveProfile);

        eEmail = (EditText)view.findViewById(R.id.edit_email);
        ePassword = (EditText)view.findViewById(R.id.edit_password);
        eName = (EditText)view.findViewById(R.id.edit_name);
        ePhone = (EditText)view.findViewById(R.id.edit_phone);
        eAddress = (EditText)view.findViewById(R.id.edit_address);
        eBeauSpec = (TextView)view.findViewById(R.id.show_spec);
        eBeauImg = (ImageView)view.findViewById(R.id.edit_pic);
        //eGender = (EditText)view.findViewById(R.id.rPhone);

        window = new AlertDialog.Builder(getContext());
        setInfo();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfilePic(v);

            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();

            }
        });


        return view;
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

    public void editProfilePic(View v)
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


    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_PHOTO);

    }

    private void setInfo(){

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
                            //beauRating.setText(beau.getString("beauRating"));
                            eName.setText(beau.getString("beauName"));
                            ePhone.setText(beau.getString("beauPhoneNum"));
                            eEmail.setText(beau.getString("beauEmail"));
                            eAddress.setText(beau.getString("beauAddress"));
                            ePassword.setText(beau.getString("beauPassword"));

                            eEmail.setEnabled(false);
                            imageString = beau.getString("beauImg");

                            if(imageString==null)
                            {
                                eBeauImg.setImageResource(R.drawable.ic_pp);
                            }
                            else {
                                try {
                                    byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);
                                    //ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                    InputStream inputStream = new ByteArrayInputStream(encodeByte);
                                    //bitmap.compress(Bitmap.CompressFormat.PNG, 1, stream);
                                    bitmap = BitmapFactory.decodeStream(inputStream);

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                eBeauImg.setImageBitmap(bitmap);
                            }


                            //GET BEAU SPEC
                            String spec = beau.getString("sTypeID");
                            switch(spec) {
                                case "ST3019":
                                    //serviceType = "Nail";
                                    //sTypeID="ST3019";
                                    eBeauSpec.setText("Nail Artist");
                                    break;
                                case "ST4005":
                                    //serviceType = "Body-Massage";
                                    //sTypeID="ST4005";
                                    eBeauSpec.setText("Body Massager");
                                    break;
                                case "ST4473":
                                    //serviceType = "Hair";
                                    //sTypeID="ST4473";
                                    eBeauSpec.setText("Hair Artist");
                                    break;
                                case "ST8047":
                                    //serviceType = "Facial";
                                    //sTypeID="ST8047";
                                    eBeauSpec.setText("Face Aestheticians");
                                    break;
                            }

                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(getContext()).add(request);
    }

    public void saveDetails()
    {
        newName = eName.getText().toString().trim();
        newPhone = ePhone.getText().toString().trim();
        newAddress = eAddress.getText().toString().trim();
        newPass =ePassword.getText().toString().trim();
        newImage = imageString;

        StringRequest strReq = new StringRequest(Request.Method.POST, URL+"f_editProfile", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray loginUser = new JSONArray(response);
                    JSONObject user = loginUser.getJSONObject(0);
                    int result = user.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getActivity(), "error.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Details Saved", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        getActivity().startActivity(intent);
                        startActivity(new Intent(getActivity(), ProfileActivity.class));

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
                params.put("beauEmail", userLogged);
                params.put("beauName", newName);
                params.put("beauPhoneNum", newPhone);
                params.put("beauAddress", newAddress);
                params.put("beauPassword", newPass);
                params.put("beauImg", newImage);
                //put in image string here to save to db
                return params;
            }

        };
        // Adding request to request queue
        Volley.newRequestQueue(getActivity()).add(strReq);
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
            profilePic.setImageBitmap(bitmap);
        }
        else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                profilePic.setImageBitmap(bitmap);
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
    }



}
