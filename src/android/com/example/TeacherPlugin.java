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


import java.util.Observable;
import java.util.Observer;

import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.lang.Boolean;
import org.json.JSONArray;

import com.usc.activation.ActivationClient;
import com.usc.tools.CommonTools;
import com.example.TeacherPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.usc.teacher.TeacherClient;
import org.usc.teacher.TeacherClientCallBack;
import android.widget.EditText;
import android.app.AlertDialog;
import android.text.Editable;
import android.graphics.Color;

public class TeacherPlugin {

	public PluginResult teacherActivation(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
    result = new PluginResult(PluginResult.Status.OK, true);;
		if (!TeacherClient.get().active) {
        // disable buttons
          result = new PluginResult(PluginResult.Status.OK, true);
          return result;
      }
      return result;
	}

  public void screenBraodCast(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
    Runnable runnable = new Runnable() {
      public void run() {
        try {
          Context ctcx = cordova.getActivity().getApplicationContext();
          TeacherClient.get().screenBroadcast();
          Toast.makeText(ctcx, "Screenbroadcast success", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
          
        }
      };
    };

    cordova.getActivity().runOnUiThread(runnable);
  }

  public void remoteAssistance(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
    Runnable runnable = new Runnable() {
      public void run() {
        try {
          Context ctcx = cordova.getActivity().getApplicationContext();
          List<String> students = TeacherClient.get().getStudents();

          String selected = "";

          for (String student : students) {
            Map studentData = TeacherClient.get().getStudentData(student);

            if ((Boolean.parseBoolean((String) studentData.get("selected")))) {
              selected = student;
            }
          }

          if (selected.isEmpty()) {
            Toast.makeText(ctcx, "Please select a student first", Toast.LENGTH_SHORT).show();
            return;
          }
          TeacherClient.get().remoteAssitance();
          Toast.makeText(ctcx, "RemoteAssistance on " + selected + " success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
          
        }
      };
    };
    cordova.getActivity().runOnUiThread(runnable);
  }

  public void attention(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
    Runnable runnable = new Runnable() {
      public void run() {
        try {
          Context ctcx = cordova.getActivity().getApplicationContext();
          TeacherClient.get().attention();
          Toast.makeText(ctcx, "attention success", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
          
        }
      };
    };

    cordova.getActivity().runOnUiThread(runnable);
  }

  public void teacherStop(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
    Runnable runnable = new Runnable() {
        public void run() {
          try {
            Context ctcx = cordova.getActivity().getApplicationContext();
            TeacherClient.get().stop();
            Toast.makeText(ctcx, "stop success", Toast.LENGTH_LONG).show();
          } catch (Exception e) {
            
          }
        };
    };

    cordova.getActivity().runOnUiThread(runnable);
  }

  public void setTeacherName(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
     Runnable runnable = new Runnable() {
         public void run() {

          Context ctcx = cordova.getActivity();
          try {

            AlertDialog.Builder alert = new AlertDialog.Builder(ctcx, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

            alert.setTitle("Set Name");
            alert.setMessage("Enter new name here:");

            // Set an EditText view to get user input
            final EditText input = new EditText(ctcx);
            input.setTextColor(Color.parseColor("#000000"));
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                try {
                  TeacherClient.get().setStringPrefrence("editname_preference", value);
                  Toast.makeText(ctcx, "Set Name success", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                  
                }

              }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
              }
            });

            alert.show();

        } catch (Exception e) {
          
        }

         };
      };

      cordova.getActivity().runOnUiThread(runnable);
  }

  PluginResult result = null;

	public PluginResult connectTeacherObserver(String action, JSONArray args, CordovaInterface cordova) throws JSONException {
      result = new PluginResult(PluginResult.Status.OK, true);;      
      TeacherClient.get().onConnected.addObserver(new Observer() {

        @Override
        public void update(Observable observable, Object data) {
          cordova.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
              result = new PluginResult(PluginResult.Status.OK, false);
            }
          });

          try {
            TeacherClient.get().registerCallBack(TeacherClientCallBack.get());
          } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }
      });
      return result;
	}

}