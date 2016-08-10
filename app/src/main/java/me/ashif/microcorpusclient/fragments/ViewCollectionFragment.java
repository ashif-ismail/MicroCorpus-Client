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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.adapter.CollectionAdapter;
import me.ashif.microcorpusclient.adapter.CustomerAdapter;
import me.ashif.microcorpusclient.config.AppConfig;
import me.ashif.microcorpusclient.config.AppController;
import me.ashif.microcorpusclient.helper.CommonMethods;
import me.ashif.microcorpusclient.model.Collection;
import me.ashif.microcorpusclient.model.Customer;

public class ViewCollectionFragment extends Fragment {

    private ProgressDialog progressDialog;
    private static final String TAG = ViewCollectionFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CollectionAdapter collectionAdapter;
    private List<Collection> collectionList = new ArrayList<>();

    public ViewCollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_collection, container, false);
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
}
