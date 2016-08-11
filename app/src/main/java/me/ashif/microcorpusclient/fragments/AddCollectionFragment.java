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
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;
import me.ashif.microcorpusclient.model.Collection;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCollectionFragment extends Fragment {

    private static final String TAG = AddCollectionFragment.class.getSimpleName();
    EditText customerID,employeeID,collectionAmount,doc;
    public  int mStatusCode;
    private String userName;
    private ProgressDialog progressDialog;

    public AddCollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Add Collection");
        return inflater.inflate(R.layout.fragment_add_collection_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if(bundle != null){
            userName = bundle.getString("USER_NAME");
        }
        progressDialog = new ProgressDialog(getActivity());
        customerID = (EditText) getActivity().findViewById(R.id.textCollectionID);
        employeeID = (EditText) getActivity().findViewById(R.id.textConnectionID);
        collectionAmount = (EditText) getActivity().findViewById(R.id.textCollectionAmount);
        doc = (EditText) getActivity().findViewById(R.id.textCollectionDatePicker);

        final Button submitButton = (Button) getActivity().findViewById(R.id.btnsubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForms()) {
                    saveCollection();
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

        super.onActivityCreated(savedInstanceState);
    }

    private void clearForm() {
        customerID.setText("");
        employeeID.setText("");
        collectionAmount.setText("");
        doc.setText("");
    }

    private void saveCollection() {
        final String tag_string_req = "req_submit_emp";
        progressDialog.setMessage("Submitting Details ...");
        showDialog();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_COLLECTION_DETAILS, new Response.Listener<String>() {
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
                        case 400:
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
                params.put("collectionAmount", collectionAmount.getText().toString());
                params.put("collectedBy", employeeID.getText().toString());
                params.put("dateOfCollection", doc.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//        clearForm();

    }

    private boolean validateForms() {
        if (customerID.getText().toString().isEmpty() || collectionAmount.getText().toString().isEmpty() ||
                customerID.getText().toString().isEmpty() || employeeID.getText().toString().isEmpty() ||
                doc.getText().toString().isEmpty()) {
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
