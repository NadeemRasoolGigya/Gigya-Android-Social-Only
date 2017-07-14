package com.example.nadeem.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;


import com.gigya.socialize.GSArray;
import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;
import com.gigya.socialize.android.GSLoginRequest;
import com.gigya.socialize.android.event.GSLoginUIListener;
import com.gigya.socialize.android.event.GSSocializeEventListener;
import com.gigya.socialize.android.GSSession;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    Button button;
    Button logoutbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGigya();
        addListenerOnButton();
        //GSAPI.getInstance().showLoginUI(null, null, null);

    }

    @Override
    public void onClick(View v) {

    }



    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.button1);
        logoutbutton = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GSObject params = new GSObject();
                params.put("enabledProviders", "facebook");
                GSAPI.getInstance().showLoginUI(params, new LoginUIListener(), null);


            }

        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Stores the session data in a GSSession object
                GSSession session = GSAPI.getInstance().getSession();
                //Make sure session != null before validating
                if (session != null && session.isValid()) {
                    // User is logged in!
                    GSAPI.getInstance().logout();
                    Toast.makeText(getApplicationContext(),
                            "You are logged out", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You are not logged in!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void initGigya() {


        GSAPI.getInstance().initialize(this, "3_yoGUs5Arh95ssMpgt_a6ydY1U-mrnUfbfVcY5r1UmHtUzZlnUQOnNGqNaTGXIUKH", "eu1.gigya.com");



        GSAPI.getInstance().setSocializeEventListener(new GSSocializeEventListener() {
            @Override
            public void onLogout(Object context) {
                Log.d(TAG, "Gigya logged out");

            }

            @Override
            public void onLogin(String provider, GSObject user, Object context) {
                Log.d(TAG, "Gigya logged in with " + provider);


            }

            @Override
            public void onConnectionRemoved(String provider, Object context) {
                Log.d(TAG, provider + " connection was removed");
            }

            @Override
            public void onConnectionAdded(String provider, GSObject user, Object context) {
                Log.d(TAG, provider + " connection was added");
            }
        });

        GSAPI.getInstance().sendRequest("socialize.getUserInfo", null, new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                if (response.getErrorCode() == 0) {
                    Log.d(TAG, "Already logged");

                } else {
                    Log.d(TAG, "Not logged");
                }
            }
        }, null);




    }





}

//Defining a Login event listener
class LoginUIListener implements GSLoginUIListener {
    //Fired when a user logs in with a social network
    public void onLogin(String provider, GSObject user, Object context) {
        String alertText = "";
        try {
            alertText = "Welcome " + user.getString("nickname") + ", you are now logged in.";
            //new AlertDialog.Builder(MainActivity.getContext()).setTitle("Alert").setMessage(alertText).setNeutralButton("Close", null).show();
        } catch (GSKeyNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void onClose(boolean arg0, Object arg1) {
        // TODO Auto-generated method stub
    }

    public void onLoad(Object arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onError(GSResponse response, Object context) {
        //new AlertDialog.Builder(MainActivity.getContext()).setTitle("Alert").setMessage("There was an error logging in.").setNeutralButton("Close", null).show();
        Log.w("GigyaExample","Error from Gigya API: " + response.getErrorMessage());

    }
}
