package my.edu.tarc.fyp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceExpandFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceExpandFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String FILE_NAME = MainActivity.FILE_NAME;
    String URL =MainActivity.URL;

    EditText eSID, eSName, eSDesc, eSPrice, eSDuration;
    Button edit, save, delete;
    String serIDFound;
    String newSID, newSName, newSDesc, newSPrice, newSDuration;

    private OnFragmentInteractionListener mListener;

    public ServiceExpandFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceExpandFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceExpandFragment newInstance(String param1, String param2) {
        ServiceExpandFragment fragment = new ServiceExpandFragment();
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
            serIDFound = getArguments().getString("serIDClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_expand, container, false);
        eSID=(EditText)view.findViewById(R.id.et_ID);
        eSName=(EditText)view.findViewById(R.id.et_name);
        eSDesc=(EditText)view.findViewById(R.id.et_desc);
        eSPrice=(EditText)view.findViewById(R.id.et_price);
        eSDuration=(EditText)view.findViewById(R.id.et_duration);

        //edit = (Button) view.findViewById(R.id.btnEditSer);
        save = (Button) view.findViewById(R.id.btnSaveSer);
        delete = (Button) view.findViewById(R.id.btnDeleteSer);

        setServiceInfo();
        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowEditText();
            }
        });*/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveService();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteService();
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

    private void allowEditText()
    {
        eSDuration.setClickable(true);
        eSName.setClickable(true);
        eSDesc.setClickable(true);
        eSPrice.setClickable(true);
    }

    private void setServiceInfo(){
        //set unchangable
        eSID.setEnabled(false);

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_displayService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray service=new JSONArray(response);
                    if(service.length()!=0)
                    {
                        for (int i = 0; i < service.length(); i++) {
                            JSONObject ser = service.getJSONObject(i);
                            eSID.setText(ser.getString("serviceID"));
                            eSName.setText(ser.getString("serviceName"));
                            eSDesc.setText(ser.getString("serviceDesc"));
                            eSDuration.setText(ser.getString("serviceDuration") + " minutes");
                            Double thePrice = ser.getDouble("servicePrice");
                            eSPrice.setText("RM " + String.format("%.2f", thePrice));
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
                params.put("serviceID", serIDFound);
                return params;

            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }

    private void deleteService(){

        eSID.setEnabled(false);
        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_deleteService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray newService = new JSONArray(response);
                    JSONObject service = newService.getJSONObject(0);
                    int result = service.getInt("result");
                    if (result == 0) {
                        Toast.makeText(getActivity(), "error.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Service Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), ServiceActivity.class);
                        getActivity().startActivity(intent);
                        startActivity(new Intent(getActivity(), ServiceActivity.class));

                    }
                } catch(Exception ex){
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
                params.put("serviceID", serIDFound);
                return params;

            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }

    private void saveService(){
        eSID.setEnabled(false);
        newSID = eSID.getText().toString().trim();
        newSName = eSName.getText().toString().trim();
        newSDesc = eSDesc.getText().toString().trim();
        newSDuration = eSDuration.getText().toString().trim();
        newSPrice = eSPrice.getText().toString().trim();

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_saveService", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray newService = new JSONArray(response);
                    JSONObject service = newService.getJSONObject(0);
                    int result = service.getInt("result");

                    if (result == 0) {
                        Toast.makeText(getActivity(), "error.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Details Saved", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), ServiceActivity.class);
                        getActivity().startActivity(intent);
                        startActivity(new Intent(getActivity(), ServiceActivity.class));

                    }
                } catch(Exception ex){
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
                params.put("serviceID", newSID);
                params.put("serviceName",newSName);
                params.put("servicePrice",newSPrice);
                params.put("serviceDuration",newSDuration);
                params.put("serviceDesc",newSDesc);
                return params;

            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
