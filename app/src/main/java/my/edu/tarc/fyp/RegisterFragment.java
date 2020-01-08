package my.edu.tarc.fyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final File FilePath = null;

    EditText rPassword, rConPassword, rName, rEmail, rPhone;
    String name, password, email, phone, addresss, sTypeID, conpassword;
    ProgressDialog progressDialog;
    String URL =MainActivity.URL;
    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    Button btnRegister, btnBrowse;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    RadioGroup radioGroupGender;
    RadioButton radioButtonMale, radioButtonFemale;
    TextView pdfName;
    String genderChose;
    String PdfNameHolder, PdfPathHolder, PdfID;
    Uri uri;
    private static final int REQUEST_PDF = 1;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        //btnBrowse = (Button) view.findViewById(R.id.btnChooseDoc);
        rPassword = (EditText)view.findViewById(R.id.rPassword);
        //rConPassword = (EditText)view.findViewById(R.id.rConPassword);
        rName = (EditText)view.findViewById(R.id.rName);
        rEmail = (EditText)view.findViewById(R.id.rEmail);
        rPhone = (EditText)view.findViewById(R.id.rPhone);

        radioGroupGender = view.findViewById(R.id.radioSex);
        radioButtonMale = view.findViewById(R.id.radioMale);
        radioButtonFemale = view.findViewById(R.id.radioFemale);

        spinner=(Spinner)view.findViewById(R.id.spinnerType);

        //pdfName = (TextView) view.findViewById(R.id.docName);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.specialisation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        sharedPreferences = getContext().getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        //spinner.setSelection(0);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "HEHE", Toast.LENGTH_LONG).show();
                validateRegister(v);

            }
        });

        /*btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFile();
            }
        });*/

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

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "HEHE", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PDF && resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            //uri = FilePath.getPath(this.getContext(),uri);
            //Log.i(TAG,"Selected File Path:" + selectedFilePath);

            if(uri != null && !uri.equals("")){
                pdfName.setText(uri.toString().trim());
            }else{
                Toast.makeText(this.getContext(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
            }
            //uri = data.getData();
            //pdfName.setText(uri.toString());
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //try to get filename then set text
            //pdfName.setText("selected");
            //Toast.makeText(this.getActivity(), uri.toString(), Toast.LENGTH_LONG).show();

            /*String FilePath = data.getData().getPath();
            String FileName = data.getData().getLastPathSegment();
            int lastPos = FilePath.length() - FileName.length();
            String Folder = FilePath.substring(0, lastPos);
            Toast.makeText(this.getActivity(), FileName, Toast.LENGTH_LONG).show();*/

        }
        else if(data == null)
        {
            Toast.makeText(this.getActivity(), "null", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this.getActivity(), "idk", Toast.LENGTH_LONG).show();
        }
    }

    /*public void PdfUploadFunction() {

        PdfNameHolder = PdfNameEditText.getText().toString().trim();

        PdfPathHolder = FilePath.getPath(this, uri);

        if (PdfPathHolder == null) {

            Toast.makeText(this.getActivity(), "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();

        } else {

            try {

                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "pdf")
                        .addParameter("name", PdfNameHolder)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }*/


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

    public void backToMain(View view)
    {
        /*if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else {
            //super.onBackPressed();
            view.findViewById(R.id.mainGone).setVisibility(View.VISIBLE);
        }*/
        Toast.makeText(getActivity(), "HEHE", Toast.LENGTH_LONG).show();
    }

    public void pickFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"),REQUEST_PDF);

    }

    public void validateRegister(View view){

        progressDialog = new ProgressDialog(getActivity());

        //String username = rUsername.getText().toString().trim();
        name = rName.getText().toString().trim();
        phone = rPhone.getText().toString().trim();
        email = rEmail.getText().toString().trim();
        password = rPassword.getText().toString().trim();
        //conpassword = rConPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            rName.setError("Enter your name");
            return;
        }

        if(TextUtils.isEmpty(phone)){
            rPhone.setError("Enter your phone number");
            return;
        }

        if(TextUtils.isEmpty(email)){
            rEmail.setError("Enter your email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            rPassword.setError("Enter new password");
            return;
        }

        /*if(TextUtils.isEmpty(conpassword)){
            rConPassword.setError("Re-enter password");
            return;
        }

        if(!password.equals(conpassword)){
            rPassword.setError("Re-enter password");
            rConPassword.setError("Password not match");
            rPassword.setText("");
            rConPassword.setText("");
            return;
        }*/

        int specialisation = spinner.getSelectedItemPosition();
        switch(specialisation) {
            case 0:
                //serviceType = "Nail";
                sTypeID="ST3019";
                break;
            case 1:
                //serviceType = "Body-Massage";
                sTypeID="ST4005";
                break;
            case 2:
                //serviceType = "Hair";
                sTypeID="ST4473";
                break;
            case 3:
                //serviceType = "Facial";
                sTypeID="ST8047";
                break;
        }

        int gender = radioGroupGender.getCheckedRadioButtonId();
        if(gender == R.id.radioMale)
        {
            genderChose="Male";
        }
        else if(gender == R.id.radioFemale)
        {
            genderChose="Female";
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        try{
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL+"f_register", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray newUser = new JSONArray(response);
                        JSONObject rUser = newUser.getJSONObject(0);
                        int result = rUser.getInt("result");

                        if (result==1) {
                            progressDialog.hide();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("logged",email);
                            editor.commit();
                            Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), AppointmentActivity.class));

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error While Registering", Toast.LENGTH_LONG).show();
                            //finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), "Error." + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //params.put("beauID", "B6");
                    params.put("beauName", name);
                    params.put("beauPhoneNum", phone);
                    params.put("beauEmail", email);
                    params.put("beauGender", genderChose);
                    params.put("beauPassword", password);
                    params.put("sTypeID", sTypeID);
                    return params;
                }
            };
            queue.add(postRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
