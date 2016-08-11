package me.ashif.microcorpusclient.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

import me.ashif.microcorpusclient.MainActivity;
import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.adapter.CollectionAdapter;
import me.ashif.microcorpusclient.adapter.CustomerAdapter;
import me.ashif.microcorpusclient.adapter.EmployeeAdapter;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;
import me.ashif.microcorpusclient.model.Collection;
import me.ashif.microcorpusclient.model.Customer;

public class ViewCollectionFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ProgressDialog progressDialog;
    private static final String TAG = ViewCollectionFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CollectionAdapter collectionAdapter;
    private List<Collection> collectionList = new ArrayList<>();
    private com.github.clans.fab.FloatingActionMenu fab;
    private com.github.clans.fab.FloatingActionButton fabItem1;
    private List<Collection> tempList = new ArrayList<>();

    public ViewCollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Collection Reports");
        return inflater.inflate(R.layout.fragment_view_collection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        fabItem1 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.menu_searchbydaterange);
        fab = (com.github.clans.fab.FloatingActionMenu) view.findViewById(R.id.fabmenu);
        fabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ViewCollectionFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Retrieving Collection Details...");
        progressDialog.show();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.CollectionList);
        collectionAdapter = new CollectionAdapter(getActivity(),collectionList);
        recyclerView.setAdapter(collectionAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        try{
            makeJsonRequest(AppConfig.GET_COLLECTION_DETAILS);
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
                                Collection collection = new Collection();
                                collection.setCustomerID(obj.getString("customerID"));
                                collection.setCollectedBy(obj.getString("collectedBy"));
                                collection.setCollectionAmount(obj.getInt("collectionAmount"));
                                collection.setDateOfCollection(obj.getString("dateOfCollection"));
                                collectionList.add(collection);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        collectionAdapter.notifyDataSetChanged();
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd){
        Date d2 = null,d3 = null,d4=null;
        SimpleDateFormat f = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        String startDate = dayOfMonth+"/"+monthOfYear+"/"+year;
        String endDate = dayOfMonthEnd+"/"+monthOfYearEnd+"/"+yearEnd;
        try {
            d3 = f.parse(startDate);
            d4 = f.parse(endDate);
        }
        catch (ParseException ex)
        {
            Log.d("Parse Exception :",ex.getMessage());
        }
        for (int i = 0; i<collectionList.size();i++){
            String temp = collectionList.get(i).getDateOfCollection();
            try {
                d2 = f.parse(temp);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (d2.compareTo(d3) >= 0 && d2.compareTo(d4) <= 0) {
                tempList.add(collectionList.get(i));
                collectionAdapter = new CollectionAdapter(getActivity(), tempList);
                recyclerView.setAdapter(collectionAdapter);
            }
        }
        CommonMethods.displayToast("Displaying Data's from : "+dayOfMonth+"/"+monthOfYear+"/"+year+" to "+dayOfMonthEnd+"/"+monthOfYearEnd+"/"+yearEnd,getActivity());

    }
}