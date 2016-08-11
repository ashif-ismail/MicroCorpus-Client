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
        import android.view.Menu;
        import android.view.MenuItem;

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

    private String userLevel;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        userLevel = getIntent().getStringExtra("USER_LEVEL");
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
            Fragment fragment = new AddEmployeeFragment();
            replaceFragment(fragment);

        } else if (id == R.id.ViewEmployee) {
            Fragment fragment = new ViewEmployeeFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewCustomerDetails){
            Fragment fragment = new ViewCustomerFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewCollectionRep){
            Fragment fragment = new ViewCollectionFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewConnectionRep){
            Fragment fragment = new ViewConnectionFragment();
            replaceFragment(fragment);

        }
        //second row
        else if (id == R.id.AddCustomer) {
            Fragment fragment = new AddConnectionFragment();
            replaceFragment(fragment);
        } else if (id == R.id.AddCollectionDetails) {
            Fragment fragment = new AddCollectionFragment();
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
            Fragment fragment = new ViewCollectionFragment();
            replaceFragment(fragment);
        }
        else if (id == R.id.ViewCustomer) {
            Fragment fragment = new ViewCustomerFragment();
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
}
