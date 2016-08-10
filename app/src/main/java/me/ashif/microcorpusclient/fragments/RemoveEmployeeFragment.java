package me.ashif.microcorpusclient.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.ashif.microcorpusclient.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveEmployeeFragment extends Fragment {


    public RemoveEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_employee, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        Spinner empSpinner = (Spinner) getActivity().findViewById(R.id.SpinnerEmpID);
        final List<String> empID = new ArrayList<>();
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_item,empID);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        empSpinner.setAdapter(spinnerAdapter);

        super.onActivityCreated(savedInstanceState);
    }
}
