package me.ashif.microcorpusclient.helper;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.net.HttpURLConnection;

import me.ashif.microcorpusclient.LoginActivity;
import me.ashif.microcorpusclient.MainActivity;

/**
 * Created by almukthar on 23/7/16.
 */
public class CommonMethods {

    public static void displayToast(String textToDisplay,Context context)
    {
        Toast.makeText(context,textToDisplay,Toast.LENGTH_SHORT).show();
    }

}
