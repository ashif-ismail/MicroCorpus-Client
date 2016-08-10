package me.ashif.microcorpusclient.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddConnectionFragment extends Fragment {

    private static final String TAG = AddConnectionFragment.class.getSimpleName();
    EditText customerID,customerType,customerName,guardianName,doc,mobile,connectedBy,initialAmount,installmentAmount,totalPayment,address,username,dod,password;
    public  int mStatusCode;
    private ProgressDialog progressDialog;
    private static String userSuffix;

    public AddConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_connection_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        userSuffix = "cst_";
        progressDialog = new ProgressDialog(getActivity());
        customerID = (EditText) getActivity().findViewById(R.id.textCustomerID);
        customerType = (EditText) getActivity().findViewById(R.id.textCustomerFirstName);
        customerName = (EditText) getActivity().findViewById(R.id.textCustomerLastName);
        guardianName = (EditText) getActivity().findViewById(R.id.textfathername);
        doc = (EditText) getActivity().findViewById(R.id.textdoc);
        mobile = (EditText) getActivity().findViewById(R.id.Textmob);
        connectedBy = (EditText) getActivity().findViewById(R.id.TextCustomerCreatedBy);
        initialAmount = (EditText) getActivity().findViewById(R.id.TextInitialAmount);
        installmentAmount = (EditText) getActivity().findViewById(R.id.TextInstallmentAmount);
        totalPayment = (EditText) getActivity().findViewById(R.id.TextInstallmentType);
        address = (EditText) getActivity().findViewById(R.id.textCustomerAddress);
        username = (EditText) getActivity().findViewById(R.id.TextUsername);
        password = (EditText) getActivity().findViewById(R.id.TextPassword);
        dod = (EditText) getActivity().findViewById(R.id.textdod);

        Button submitButton = (Button) getActivity().findViewById(R.id.btnsubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForms()) {
                    saveConnection();
                } else
                    CommonMethods.displayToast("Some of the Information Required are Missing,Please verify and submit Again"
                            , getActivity());
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void saveConnection() {
        final String tag_string_req = "req_submit_emp";
        progressDialog.setMessage("Submitting Details ...");
        showDialog();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_CONNECTION_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Submission Response: " + response.toString());
                hideDialog();
                checkStatusCode();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Submission Error: " + error.getMessage());
                hideDialog();
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 403:
                            CommonMethods.displayToast("Access Forbidden", getActivity());
                            break;
                        case 500:
                            CommonMethods.displayToast("Internal Server Error,Please Try again later", getActivity());
                    }
                }
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to submission endpoint
                Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", customerID.getText().toString());
                params.put("customerType", customerType.getText().toString());
                params.put("customerName", customerName.getText().toString());
                params.put("customerGuardian", guardianName.getText().toString());
                params.put("dateOfConn", doc.getText().toString());
                params.put("mobileNo", mobile.getText().toString());
                params.put("connectedBy", connectedBy.getText().toString());
                params.put("initialAmount", initialAmount.getText().toString());
                params.put("installmentAmount", installmentAmount.getText().toString());
                params.put("totalAmount", totalPayment.getText().toString());
                params.put("address", address.getText().toString());
                params.put("username", userSuffix.concat(username.getText().toString()).concat(customerID.getText().toString()));
                params.put("password", password.getText().toString());
                params.put("dateOfDue", dod.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private boolean validateForms() {
        if (customerName.getText().toString().isEmpty() || customerType.getText().toString().isEmpty() ||
                customerID.getText().toString().isEmpty() || guardianName.getText().toString().isEmpty() ||
                doc.getText().toString().isEmpty() || mobile.getText().toString().isEmpty() ||
                connectedBy.getText().toString().isEmpty() || initialAmount.getText().toString().isEmpty() ||
                installmentAmount.getText().toString().isEmpty() || totalPayment.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty() || username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() || dod.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }
    private void checkStatusCode() {
        switch (mStatusCode) {
            case HttpURLConnection.HTTP_OK:
                //submission success
                Toast.makeText(getActivity(), "Successfully Submitted the Information", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
