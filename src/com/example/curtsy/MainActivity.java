package com.example.curtsy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import com.google.gson.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.model.*;

public class MainActivity extends Activity {
		final String TAG = "MainActivity";

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,TAG +"1");
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
    	Log.d(TAG,TAG +"2");
        try {/////////////////////////
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.curtsy", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", "KEY" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }/////////////////////
	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @SuppressWarnings("deprecation")
		@Override
	      public void call(Session session, SessionState state, Exception exception) {
	    	  Log.d(TAG,"3" + state);
	        if (session.isOpened()) {
	        	Log.d(TAG,"4");

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	            	  Log.d(TAG,"5");
	                TextView welcome = (TextView) findViewById(R.id.textView1);
	                welcome.setText("Hello " + user.getName() + "!");
	              }
	            }
	          });
	        }
	      }
	    });
	  }

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
	  
	  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}


