package me.ashif.microcorpusclient;

        import android.app.AlertDialog;
        import android.app.Fragment;
        import android.app.FragmentTransaction;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;

        import com.android.volley.NetworkResponse;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonArrayRequest;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.lang.reflect.Method;
        import java.util.HashMap;
        import java.util.Map;

        import me.ashif.microcorpusclient.config.AppConfig;
        import me.ashif.microcorpusclient.config.AppController;
        import me.ashif.microcorpusclient.fragments.AddEmployeeFragment;
        import me.ashif.microcorpusclient.fragments.ViewCollectionFragment;
        import me.ashif.microcorpusclient.fragments.ViewConnectionFragment;
        import me.ashif.microcorpusclient.helper.CommonMethods;
        import me.ashif.microcorpusclient.fragments.AddCollectionFragment;
        import me.ashif.microcorpusclient.fragments.AddConnectionFragment;
        import me.ashif.microcorpusclient.fragments.ViewCustomerFragment;
        import me.ashif.microcorpusclient.fragments.ViewEmployeeFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String userLevel,userName;
    private NavigationView navigationView;
    private int mStatusCode,empID;
    private Fragment fragment;
    private Bundle myBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myBundle = new Bundle();
        getIDfromUsername();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        userLevel = getIntent().getStringExtra("USER_LEVEL");
        userName = getIntent().getStringExtra("USER_NAME");

        CommonMethods.displayToast("Successfully Logged in as " + userLevel, MainActivity.this);

        //HIDE ALL OTHER IRRELAVENT OPTIONS FROM NAV-DRAWER BASED ON USER LEVEL
        if(userLevel.equals("MANAGER"))
        {
            //HIDE ALL BUT MANAGER'S OPTIONS IN THE DRAWER
            nav_Menu.findItem(R.id.AddCustomer).setVisible(false);
            nav_Menu.findItem(R.id.AddCollectionDetails).setVisible(false);
            nav_Menu.findItem(R.id.ViewCustomer).setVisible(false);
            nav_Menu.findItem(R.id.SetEMIAlert).setVisible(false);
            nav_Menu.findItem(R.id.ViewCustomerInfo).setVisible(false);
            nav_Menu.findItem(R.id.PaymentInfo).setVisible(false);
            nav_Menu.findItem(R.id.ViewCollection).setVisible(false);

            Fragment fragment = new AddEmployeeFragment();
            replaceFragment(fragment);
        }
        else if(userLevel.equals("EMPLOYEE"))
        {
            //HIDE ALL BUT EMPLOYEE'S OPTIONS IN THE DRAWER
            nav_Menu.findItem(R.id.AddEmployee).setVisible(false);
            nav_Menu.findItem(R.id.ViewEmployee).setVisible(false);
            nav_Menu.findItem(R.id.ViewCustomerDetails).setVisible(false);
            nav_Menu.findItem(R.id.ViewCollectionRep).setVisible(false);
            nav_Menu.findItem(R.id.ViewConnectionRep).setVisible(false);
            nav_Menu.findItem(R.id.ViewCustomerInfo).setVisible(false);
            nav_Menu.findItem(R.id.PaymentInfo).setVisible(false);

            //getting empID from table
            getIDfromUsername();

            Fragment fragment = new AddConnectionFragment();
            replaceFragment(fragment);

        }
        else
        {
            //FOR ALL OTHER CASE/FOR CUSTOMER'S
            nav_Menu.findItem(R.id.AddCustomer).setVisible(false);
            nav_Menu.findItem(R.id.AddCollectionDetails).setVisible(false);
            nav_Menu.findItem(R.id.ViewCustomer).setVisible(false);
            nav_Menu.findItem(R.id.SetEMIAlert).setVisible(false);
            nav_Menu.findItem(R.id.AddEmployee).setVisible(false);
            nav_Menu.findItem(R.id.ViewEmployee).setVisible(false);
            nav_Menu.findItem(R.id.ViewCustomerDetails).setVisible(false);
            nav_Menu.findItem(R.id.ViewCollectionRep).setVisible(false);
            nav_Menu.findItem(R.id.ViewConnectionRep).setVisible(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.AddEmployee) {
            fragment = new AddEmployeeFragment();
            replaceFragment(fragment);

        } else if (id == R.id.ViewEmployee) {
            fragment = new ViewEmployeeFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewCustomerDetails){
            fragment = new ViewCustomerFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewCollectionRep){
            fragment = new ViewCollectionFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewConnectionRep){
            fragment = new ViewConnectionFragment();
            replaceFragment(fragment);

        }
        //second row
        else if (id == R.id.AddCustomer) {
            fragment = new AddConnectionFragment();
            fragment.setArguments(myBundle);
            replaceFragment(fragment);
        } else if (id == R.id.AddCollectionDetails) {
            fragment = new AddCollectionFragment();
            fragment.setArguments(myBundle);
            replaceFragment(fragment);
        } else if (id == R.id.SetEMIAlert) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("This feature is yet to be implemented");
            alertDialog.setTitle("MicroCorpus Client");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonMethods.displayToast("thanks",getApplicationContext());
                }
            });
            alertDialog.show();
        }
        else if (id == R.id.ViewCollection){
            fragment = new ViewCollectionFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewCustomer) {
            fragment = new ViewCustomerFragment();
            replaceFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void getIDfromUsername() {
        //make a rest call to endpoint and set the text as empID
        final String tag_string_req = "req_submit_emp";
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_EMPID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Submission Response: " + response.toString());
                //parse the result json and set the empID text
                try{
                    empID = Integer.parseInt(response.toString());
                    myBundle.putInt("EMP_ID",empID);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Submission Error: " + error.getMessage());
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            CommonMethods.displayToast("HTTP BAD Request",getApplicationContext());
                            break;
                        case 500:
                            CommonMethods.displayToast("Internal Server Error,Please Try again later", getApplicationContext());
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
                params.put("username",userName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
