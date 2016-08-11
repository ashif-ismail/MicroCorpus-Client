package me.ashif.microcorpusclient.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.adapter.EmployeeAdapter;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;
import me.ashif.microcorpusclient.model.Employee;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeeFragment extends Fragment {

    private static final String TAG = ViewEmployeeFragment.class.getSimpleName();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList = new ArrayList<>();
    private com.github.clans.fab.FloatingActionMenu fab;
    private com.github.clans.fab.FloatingActionButton fabItem1;
    private com.github.clans.fab.FloatingActionButton fabItem2;
    private com.github.clans.fab.FloatingActionButton fabItem3;
    private String searchTerm = "";
    private ArrayList<Employee> tempList = new ArrayList<Employee>();

    public ViewEmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabItem1 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.menu_searchbyid);
        fabItem2 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.menu_searchbyname);
        fabItem3 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.menu_searchbynumber);
        fab = (com.github.clans.fab.FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fab.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Employee Details");
        return inflater.inflate(R.layout.fragment_view_employee, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Retrieving Employee Details...");
        progressDialog.show();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.EmployeeList);
        employeeAdapter = new EmployeeAdapter(getActivity(), employeeList);
        recyclerView.setAdapter(employeeAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        try {
            makeJsonRequest(AppConfig.GET_EMPLOYEE_DETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList.clear();
                showIDPrompt();
            }
        });
        fabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList.clear();
                showNamePrompt();
            }
        });
        fabItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList.clear();
                showNoPrompt();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void showNoPrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.dailoglayout, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        searchTerm = userInputDialogEditText.getText().toString();
                        searchByEmpNo();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void searchByEmpNo() {
        for(Employee employee : employeeList){
            if (employee.getPhone().equals(searchTerm)){
                tempList.add(employee);
                employeeAdapter = new EmployeeAdapter(getActivity(), tempList);
                recyclerView.setAdapter(employeeAdapter);
            }

        }
    }

    private void showNamePrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.dailoglayout, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        searchTerm = userInputDialogEditText.getText().toString();
                        searchByEmpName();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void searchByEmpName() {
        for(Employee employee : employeeList) {
            if (employee.getFirstName().concat(employee.getLastName()).toLowerCase().contains(searchTerm)) {
                tempList.add(employee);
                employeeAdapter = new EmployeeAdapter(getActivity(), tempList);
                recyclerView.setAdapter(employeeAdapter);
            }
        }
    }

    private void showIDPrompt() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.dailoglayout, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        searchTerm = userInputDialogEditText.getText().toString();
                        searchByEmpID();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void searchByEmpID() {
        for(Employee employee : employeeList){
            if (employee.getEmpID().equals(searchTerm)){
                tempList.add(employee);
                employeeAdapter = new EmployeeAdapter(getActivity(), tempList);
                recyclerView.setAdapter(employeeAdapter);
            }

        }

    }


    private void makeJsonRequest(String endpoint) throws JSONException {
        JsonArrayRequest requestReq = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hideDialog();
                        fab.setVisibility(View.VISIBLE);
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Employee employee = new Employee();
                                employee.setFirstName(obj.getString("firstName"));
                                employee.setLastName(obj.getString("lastName"));
                                employee.setEmail(obj.getString("email"));
                                employee.setDateOfJoin(obj.getString("dateOfJoin"));
                                employee.setEmpID(obj.getString("empID"));
                                employee.setPassword(obj.getString("password"));
                                employee.setUsername(obj.getString("username"));
                                employee.setPhone(obj.getString("phoneNumber"));
                                employee.setQualification(obj.getString("Qualification"));
                                employee.setAddress(obj.getString("address"));

                                employeeList.add(employee);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        employeeAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonMethods.displayToast("Failed Retreiving Data,Please Try Again", getActivity());
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
