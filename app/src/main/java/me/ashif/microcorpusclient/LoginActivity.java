package me.ashif.microcorpusclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import me.ashif.microcorpusclient.helper.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    public  int mStatusCode;
    private String currentUserLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (EditText) findViewById(R.id.usernametext);
        passwordText = (EditText) findViewById(R.id.passwordtext);
        loginButton = (Button) findViewById(R.id.btnlogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    if (username.startsWith("mgr")) {
                        verifyUser(username, password, AppConfig.MANAGER_LOGIN);
                        currentUserLevel = "MANAGER";
                    } else if (username.startsWith("emp")) {
                        verifyUser(username, password, AppConfig.EMPLOYEE_LOGIN);
                        currentUserLevel = "EMPLOYEE";
                    } else if (username.startsWith("cst")) {
                        verifyUser(username, password, AppConfig.CUSTOMER_LOGIN);
                        currentUserLevel = "CUSTOMER";
                    }
                } else if (username.startsWith("mgr") || username.startsWith("cst") || username.startsWith("emp")) {
                    CommonMethods.displayToast("Invalid Combination of username/password", LoginActivity.this);
                } else
                    CommonMethods.displayToast("Please enter valid credentials", LoginActivity.this);
//                   callActivity();
            }
        });
    }

    private void callActivity() {
        Intent mIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void verifyUser(final String username, final String password,String loginType) {
        final String tag_string_req = "req_login";
        progressDialog.setMessage("Logging in ...");
        showDialog();
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                loginType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                checkStatusCode();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Login Error: " + error.getMessage());
                hideDialog();
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 403:
                            CommonMethods.displayToast("Invalid username/password Combination", LoginActivity.this);
                            break;
                        case 500:
                            CommonMethods.displayToast("Internal Server Error,Please Try again later", LoginActivity.this);
                             break;
                        case 503:
                            CommonMethods.displayToast("Problem Connecting to remote Server",LoginActivity.this);
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void checkStatusCode() {
        switch (mStatusCode){
            case HttpURLConnection.HTTP_OK:
                //login success
                Intent mIntent = new Intent(LoginActivity.this,MainActivity.class);
                mIntent.putExtra("USER_LEVEL",currentUserLevel);
                startActivity(mIntent);
                finish();
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

