package me.ashif.microcorpusclient.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.adapter.CustomerAdapter;
import me.ashif.microcorpusclient.adapter.EmployeeAdapter;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;
import me.ashif.microcorpusclient.model.Customer;
import me.ashif.microcorpusclient.model.Employee;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCustomerFragment extends Fragment {

    private ProgressDialog progressDialog;
    private static final String TAG = ViewCustomerFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList = new ArrayList<>();

    public ViewCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Customer Details");
        return inflater.inflate(R.layout.fragment_view_customer, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Retrieving Customer Details...");
        progressDialog.show();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.CustomerList);
        customerAdapter = new CustomerAdapter(getActivity(),customerList);
        recyclerView.setAdapter(customerAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        try{
            makeJsonRequest(AppConfig.GET_CUSTOMER_DETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onActivityCreated(savedInstanceState);
    }



    private void makeJsonRequest(String endpoint) throws JSONException{
        JsonArrayRequest requestReq = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hideDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Customer customer = new Customer();
                                customer.setCustomerID(obj.getString("customerID"));
                                customer.setDateOfConn(obj.getString("dateOfConn"));
                                customer.setInitialAmount(obj.getInt("initialAmount"));
                                customer.setDateOfDue(obj.getString("dateOfDue"));
                                customer.setInstallmentAmount(obj.getInt("installmentAmount"));
                                customer.setTotalAmount(obj.getInt("totalAmount"));
                                customer.setCustomerType(obj.getInt("customerType"));
                                customer.setCustomerName(obj.getString("customerName"));
                                customer.setCustomerGuardian(obj.getString("customerGuardian"));
                                customer.setUsername(obj.getString("username"));
                                customer.setPassword(obj.getString("password"));
                                customer.setMobileNo(String.valueOf(obj.getLong("mobileNo")));
                                customer.setConnectedBy(obj.getString("connectedBy"));
                                customer.setAddress(obj.getString("address"));
                                customerList.add(customer);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        customerAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonMethods.displayToast("Failed Retreiving Data,Please Try Again",getActivity());
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(requestReq);
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
