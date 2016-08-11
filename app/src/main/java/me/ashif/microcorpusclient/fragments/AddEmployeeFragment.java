package me.ashif.microcorpusclient.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class AddEmployeeFragment extends Fragment {
    private static final String TAG = AddEmployeeFragment.class.getSimpleName();
    private static EditText empFirstName, empLastName, empEmailAdd, empJoinDate, empID, empPassword, empPhone, empQual, empAddress;
    private String userLevel;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;
    public  int mStatusCode;
    private static String userSuffix;
    private FloatingActionButton fab;

    public AddEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Add Employee");
        return inflater.inflate(R.layout.fragment_add_employee, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        userSuffix = "emp_";

        empFirstName = (EditText) getActivity().findViewById(R.id.textFirstName);
        empLastName = (EditText) getActivity().findViewById(R.id.textLastName);
        empEmailAdd = (EditText) getActivity().findViewById(R.id.textEmail);
        empID = (EditText) getActivity().findViewById(R.id.textEmpID);
        empJoinDate = (EditText) getActivity().findViewById(R.id.textDatePicker);
        empPassword = (EditText) getActivity().findViewById(R.id.textPassword);
        empPhone = (EditText) getActivity().findViewById(R.id.textPhone);
        empQual = (EditText) getActivity().findViewById(R.id.textDegree);
        empAddress = (EditText) getActivity().findViewById(R.id.textAddress);

        final Button submitButton = (Button) getActivity().findViewById(R.id.btnsubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForms()) {
                    saveEmpDetails();
                    submitButton.setEnabled(false);
                    submitButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            submitButton.setEnabled(true);
                        }
                    }, 10000);
                } else
                    CommonMethods.displayToast("Some of the Information Required are Missing,Please verify and submit Again"
                            , getActivity());
            }
        });
    }

    private void saveEmpDetails() {
        final String tag_string_req = "req_submit_emp";
        progressDialog.setMessage("Submitting Details ...");
        showDialog();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_EMPLOYEE_DETAILS, new Response.Listener<String>() {
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
                            CommonMethods.displayToast("Invalid username/password Combination", getActivity());
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
                params.put("firstName", empFirstName.getText().toString());
                params.put("lastName", empLastName.getText().toString());
                params.put("email", empEmailAdd.getText().toString());
                params.put("dateOfJoin", empJoinDate.getText().toString());
                params.put("empID", empID.getText().toString());
                params.put("username",userSuffix.concat(empFirstName.getText().toString().concat(empID.getText().toString())));
                params.put("password", empPassword.getText().toString());
                params.put("phoneNumber", empPhone.getText().toString());
                params.put("Qualification", empQual.getText().toString());
                params.put("address", empAddress.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        clearForm();
    }

    private void clearForm() {
        empAddress.setText("");
        empEmailAdd.setText("");
        empFirstName.setText("");
        empID.setText("");
        empJoinDate.setText("");
        empPassword.setText("");
        empLastName.setText("");
        empPhone.setText("");
        empQual.setText("");
    }

    private boolean validateForms() {
        if (empFirstName.getText().toString().isEmpty() || empLastName.getText().toString().isEmpty() ||
                empEmailAdd.getText().toString().isEmpty() || empID.getText().toString().isEmpty() ||
                empJoinDate.getText().toString().isEmpty() || empPassword.getText().toString().isEmpty() ||
                empPhone.getText().toString().isEmpty() || empQual.getText().toString().isEmpty() ||
                empAddress.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }
    private void checkStatusCode() {
        switch (mStatusCode) {
            case HttpURLConnection.HTTP_OK:
                //submission success
                Toast.makeText(getActivity(), "Successfully Submitted the Information", Toast.LENGTH_SHORT).show();
                clearForm();
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
