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
import me.ashif.microcorpusclient.adapter.ConnectionAdapter;
import me.ashif.microcorpusclient.adapter.CustomerAdapter;
import me.ashif.microcorpusclient.adapter.EmployeeAdapter;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;
import me.ashif.microcorpusclient.model.Connection;
import me.ashif.microcorpusclient.model.Customer;
import me.ashif.microcorpusclient.model.Employee;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewConnectionFragment extends Fragment {

    private ProgressDialog progressDialog;
    private static final String TAG = ViewConnectionFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ConnectionAdapter connectionAdapter;
    private List<Connection> connectionList = new ArrayList<>();

    public ViewConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_view_connection, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Retrieving Connection Details...");
        progressDialog.show();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.ConnectionList);
        connectionAdapter = new ConnectionAdapter(getActivity(),connectionList);
        recyclerView.setAdapter(connectionAdapter);
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
                                Connection connection = new Connection();
                                connection.setDoc(obj.getString("dateOfConn"));
                                connection.setInitialAmount(obj.getInt("initialAmount"));
                                connection.setCustomerType(obj.getInt("customerType"));
                                connection.setCustomerName(obj.getString("customerName"));
                                connection.setConnectedBy(obj.getString("connectedBy"));
                                connection.setAddress(obj.getString("address"));
                                connectionList.add(connection);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        connectionAdapter.notifyDataSetChanged();
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
