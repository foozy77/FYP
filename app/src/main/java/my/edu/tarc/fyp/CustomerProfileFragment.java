package my.edu.tarc.fyp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String URL =MainActivity.URL;
    SharedPreferences sharedPreferences;
    String userLogged;
    public static final String FILE_NAME = MainActivity.FILE_NAME;
    TextView custName, custEmail, custPhone, custGender, custRating, custAddress;
    ImageView custImg;
    String imageString;
    Bitmap bitmap;
    String passCustEmail;
    RatingBar custStar;

    private OnFragmentInteractionListener mListener;

    public CustomerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerProfileFragment newInstance(String param1, String param2) {
        CustomerProfileFragment fragment = new CustomerProfileFragment();
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
            passCustEmail = getArguments().getString("custEmailClicked");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_profile, container, false);

        custImg= view.findViewById(R.id.f_cust_img);
        custName= view.findViewById(R.id.f_cust_name);
        custGender= view.findViewById(R.id.f_cust_gender);
        custRating= view.findViewById(R.id.f_cust_rating);
        custPhone= view.findViewById(R.id.f_cust_phone);
        custEmail= view.findViewById(R.id.f_cust_email);
        custAddress= view.findViewById(R.id.f_cust_address);
        custStar=view.findViewById(R.id.f_cust_star);

        loadCust();
        // Inflate the layout for this fragment
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

    private void loadCust(){

        StringRequest request=new StringRequest(Request.Method.POST, URL+"f_loadCustomerInfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray customer = new JSONArray(response);
                    if (customer.length() != 0) {
                        for (int i = 0; i < customer.length(); i++) {
                            JSONObject cust = customer.getJSONObject(i);
                            custEmail.setText(passCustEmail);
                            custGender.setText(cust.getString("custGender"));
                            custRating.setText(cust.getString("custRating"));
                            custStar.setRating(Float.parseFloat(cust.getString("custRating")));
                            custName.setText(cust.getString("custName"));
                            custPhone.setText(cust.getString("custPhoneNum"));
                            custAddress.setText(cust.getString("custAddress"));
                            imageString = cust.getString("custImg");

                            if(imageString==null)
                            {
                                custImg.setImageResource(R.drawable.ic_pp);
                            }
                            else {
                                try {
                                    byte[] encodeByte = Base64.decode(imageString, Base64.DEFAULT);

                                    InputStream inputStream = new ByteArrayInputStream(encodeByte);
                                    bitmap = BitmapFactory.decodeStream(inputStream);

                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                custImg.setImageBitmap(bitmap);
                            }
                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("custEmail", "Abdul_Foy@nowhere.com");
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);
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
}
