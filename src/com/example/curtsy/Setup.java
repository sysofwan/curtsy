package com.example.curtsy;

import java.util.concurrent.ExecutionException;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Setup extends Activity implements OnLocationAvailableListener {
	private Button b_save;
	private EditText et_phone;
	private EditText et_email;
	private LocationFragment locationFragment;
	private final String REQUEST_PATH = "";
	private RequestQueue queue;
	
	String ACTIVITY_TAG = "MainStatusActivity";

    /**Put a request path (adduser, addemail, addnumber)*/
//  private static final String STATUS_PATH = "getstatus";

    /**Put a request parameter (facebook_id, name, phone_no)*/
//  private static final String STATUS_ARGS = "?lat=%f&lon=%f";
//  private static final String REQUEST_PATH = STATUS_PATH + STATUS_ARGS;
//
//  private LocationFragment locationFragment;
//  private ListView listView;
//  private RequestQueue queue;
//  private ProgressDialog progressDialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_setup);	
		et_phone = (EditText) findViewById(R.id.edittext_phone);
		et_email = (EditText) findViewById(R.id.edittext_email);
		b_save = (Button) findViewById(R.id.button_save);
        b_save.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        		locationFragment = new LocationFragment();
              	transaction.add(locationFragment, LocationFragment.FRAGMENT_TAG);
              	transaction.commit();
        	}
        });
        
		
		

		
		//queue = Volley.newRequestQueue(this);
	}
		
		private void requestStatus() {
          if (locationFragment.isGpsAvailable()) {
                  /**We will add our request to Volley queue. As soon as a request is done, volley will call the
                   * onResponse function callback
                   * Note that the GsonRequestHandler is templated, just substitute with UserList.class
                   * List - because we have a list of nearby users.
                   * Note that the onResponse method passes in the class that already have the data we needed
                   * (the UserListClass)*/
              /*queue.add(new GsonRequestHandler<UserList>(buildPath(), UserList.class, null, null,
                      new Response.Listener<UserList>() {
                          @Override
                         public void onResponse(UserList userlist) {
                              //Log.d(ACTIVITY_TAG, "response callback running");
                              ListAdapter adapter = new StatusAdapter(MainStatusActivity.this, busRouteList.getResults());
                              listView.setAdapter(adapter);
                          }
                      }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError volleyError) {
                      //To change body of implemented methods use File | Settings | File Templates.
                  }
              }));*/
          }
      }

      /**
       * This is where we add all data the server needs (facebook_id, phone_no, lat, lon)*/
      private String buildPath() {
          if (locationFragment.isGpsAvailable()) {
              Location location = locationFragment.getCurrentLocation();
                  /**To get the latitude and longitude is as easy as this!*/
                  /**You guys figure out how to get other data! (especially facebook_id)*/
              double lat = location.getLatitude();
              double lon = location.getLongitude();
              String email = et_email.getText().toString();
              String phone = et_phone.getText().toString();
              String facebook_id = MainActivity.CurrUser.getId();
              Log.w("SETUP", "path=" + String.format(REQUEST_PATH, lat, lon));
              return String.format(REQUEST_PATH, lat, lon);
          }
          locationFragment.showSettingsAlert();
          return null;
      }

      /**Put a request path (adduser, addemail, addnumber)*/
      //  private static final String STATUS_PATH = "getstatus";

      /**Put a request parameter (facebook_id, name, phone_no)*/
      
	@Override
	public void onLocationAvailable() {
        Location location = locationFragment.getCurrentLocation();
        /**To get the latitude and longitude is as easy as this!*/
        /**You guys figure out how to get other data! (especially facebook_id)*/
        String lat = Double.valueOf(location.getLatitude()).toString();
        String lon = Double.valueOf(location.getLongitude()).toString();
        String email = et_email.getText().toString();
        String phone = et_phone.getText().toString();
        String facebook_id = MainActivity.CurrUser.getId();
        String name = MainActivity.CurrUser.getFirstName(); //last name is being ignored for now
        String call1 = String.format("adduser" + "?facebook_id=%s&name=%s&phone_no=%s&email=%s", facebook_id, name, phone, email);
        String call2 = String.format("postlocation" + "?facebook_id=%s&lat=%s&lon=%s", facebook_id, lat, lon);        
        
        /**Put a request path (adduser, addemail, addnumber)*/
        //  private static final String STATUS_PATH = "getstatus";

        /**Put a request parameter (facebook_id, name, phone_no)*/
		
		//send info to server
		
		//receive info from server
		
		//switch activities passing relevant info via static vals		
		
		
	}
		
}
