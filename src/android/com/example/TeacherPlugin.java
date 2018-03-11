package com.example;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;

public class TeacherPlugin {

	public void teacherActivation(String action, JSONArray args, final CallbackContext callbackContext, CordovaInterface cordova) throws JSONException {
		Context ctcx = cordova.getActivity().getApplicationContext();
      	Toast.makeText(ctcx, action + "asasdasd", 8).show();
	}


}