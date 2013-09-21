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
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.model.*;

public class MainActivity extends FragmentActivity {
		final String TAG = "MainActivity";
		public static GraphUser CurrUser;
		//private MainFragment mainFragment;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    /*
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }*/
	    
        
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @SuppressWarnings("deprecation")
		@Override
	      public void call(Session session, SessionState state, Exception exception) {
	    	  Log.d(TAG,"3" + state);
	        if (session.isOpened()) {

	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	            	CurrUser = user;
	                Intent intent = new Intent(MainActivity.this, AroundMe.class);
					startActivity(intent);
	              }
	            }
	          });
	        }
	      }
	    });
	  }
	    
	  @Override
	  public void onResume() {
	      super.onResume();
	  }


	  @Override
	  public void onPause() {
	      super.onPause();
	  }

	  @Override
	  public void onDestroy() {
	      super.onDestroy();
	  }

	  @Override
	  public void onSaveInstanceState(Bundle outState) {
	      super.onSaveInstanceState(outState);
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


