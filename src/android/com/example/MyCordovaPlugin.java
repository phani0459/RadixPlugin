/**
 */
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
import org.json.JSONArray;

import com.usc.activation.ActivationClient;
import com.usc.tools.CommonTools;
import com.example.TeacherPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.usc.student.StudentClient;
import org.usc.student.StudentClientCallBack;
import android.widget.EditText;
import android.app.AlertDialog;
import android.text.Editable;
import android.graphics.Color;

public class MyCordovaPlugin extends CordovaPlugin {
  private static final String TAG = "MyCordovaPlugin";

  TeacherPlugin teacherPlugin;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    teacherPlugin = new TeacherPlugin();

    Log.d(TAG, "Initializing MyCordovaPlugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals("echo")) {
      String phrase = args.getString(0);
      // Echo back the first argument
      Log.d(TAG, phrase);
    } else if(action.equals("getDate")) {
      // An example of returning data back to the web layer
      final PluginResult result = new PluginResult(PluginResult.Status.OK, (new Date()).toString());
      callbackContext.sendPluginResult(result);
    } else if(action.equals("activation")) {
      // An example of returning data back to the web layer
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      ActivationClient.get().init(args.getString(0), ctcx);
    } else if(action.equals("selectTeacher")) {
      // An example of returning data back to the web layer
      StudentClient.get().selectTeacher(args.getString(0));
    } else if(action.equals("student-client")) {
      if (!StudentClient.get().active) {
        // disable buttons
          final PluginResult result = new PluginResult(PluginResult.Status.OK, true);
          callbackContext.sendPluginResult(result);
      }
    } else if(action.equals("check-app-availability")) {
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      boolean isServiceAvailable = CommonTools.isPackageExists(ctcx, args.getString(0));

      final PluginResult result = new PluginResult(PluginResult.Status.OK, isServiceAvailable);
      callbackContext.sendPluginResult(result); 

    } else if(action.equals("showToast")) {
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      Toast.makeText(ctcx, args.getString(0), 8).show();
    } else if (action.equals("getTeachers")) {
      final CordovaInterface cordova = this.cordova;

      Runnable runnable = new Runnable() {
         public void run() {

          try {
              /*Context ctcx = cordova.getActivity();
              List<String> teachers = StudentClient.get().getTeachers();
              if (teachers != null && teachers.size() > 0) {
                JSONArray jsArray = new JSONArray(teachers);  
                final PluginResult result = new PluginResult(PluginResult.Status.OK, jsArray);
                callbackContext.sendPluginResult(result);
              }*/

              Context ctcx = cordova.getActivity();
              List<String> teachers = new ArrayList<String>();
              teachers.add("Phani");
              teachers.add("kumar");
              teachers.add("uday");
              
              if (teachers != null && teachers.size() > 0) {
                JSONArray jsArray = new JSONArray(teachers);  
                final PluginResult result = new PluginResult(PluginResult.Status.OK, jsArray);
                callbackContext.sendPluginResult(result); 
              }
              
              /*arrayAdapter.clear();

              for (String teacher : teachers) {
                  Map studentData = StudentClient.get().getTeacherData(teacher);
                  TeacherItem teacherItem = new TeacherItem(teacher);
                  arrayAdapter.add(teacherItem);
              }

              String currentTeacher = StudentClient.get().currentTeacher();

              for (int i = 0; i < arrayAdapter.getCount(); i++) {
                  TeacherItem teacherItem = arrayAdapter.getItem(i);
                  listViewTeachers.setItemChecked(i, teacherItem.name.equalsIgnoreCase(currentTeacher));
              }*/

              
            } catch (Exception e) {
              
            }

         };
      };

      this.cordova.getActivity().runOnUiThread(runnable);
    
    } else if(action.equals("showActivationDialog")) {
      final CordovaInterface cordova = this.cordova;

      Runnable runnable = new Runnable() {
         public void run() {

          Context ctcx = cordova.getActivity();
          final EditText textEntryView = new EditText(ctcx);
          textEntryView.setText("USC-STUDENT-DEMO");
          textEntryView.setTextColor(Color.parseColor("#000000"));
          
          AlertDialog alertDialog = new AlertDialog.Builder(ctcx, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("Enter product ID").setView(textEntryView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

              try {
                boolean isActivated = ActivationClient.get().isActivated(ActivationClient.PRODUCT_TYPE_STUDENT);

                if (!isActivated) {
                    Editable text = textEntryView.getText();
                    boolean activate = ActivationClient.get().activate(text.toString(), ActivationClient.PRODUCT_TYPE_STUDENT);

                    if (activate)
                      Toast.makeText(ctcx, "activate success", Toast.LENGTH_LONG).show();
                    else
                      Toast.makeText(ctcx, "activate failed", Toast.LENGTH_LONG).show();  
                } else {
                  Toast.makeText(ctcx, "Activated", Toast.LENGTH_LONG).show();  
                }
                

              } catch (Exception e) {
                
              }
            }
          }).create();

          alertDialog.show();

         };
      };

      this.cordova.getActivity().runOnUiThread(runnable);
    } else if (action.equals("setStudentName")) {
      final CordovaInterface cordova = this.cordova;

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
                  StudentClient.get().setStringPrefrence("editname_preference", value);
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

      this.cordova.getActivity().runOnUiThread(runnable);
    } else if(action.equals("connectBtn")) {


      final CordovaInterface cordova = this.cordova;

      Runnable runnable = new Runnable() {
         public void run() {

          Context ctcx = cordova.getActivity().getApplicationContext();
          try {
              boolean activated = ActivationClient.get().isActivated(ActivationClient.PRODUCT_TYPE_STUDENT);
              
              if (!activated) {
                Toast.makeText(ctcx, "please activate first", Toast.LENGTH_LONG).show();
              } else {
                try {
                  StudentClient.get().init(cordova.getActivity());
                  Toast.makeText(ctcx, "connect success", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                  Toast.makeText(ctcx, "connect failed", Toast.LENGTH_LONG).show();
                }
              }
            } catch (Exception e1) {
              Toast.makeText(ctcx, "activation failed: " + e1.toString(), Toast.LENGTH_LONG).show();  
            }
         };
      };

      this.cordova.getActivity().runOnUiThread(runnable);
    } else if (action.equals("connect-observer")) {
      final CordovaInterface cordova = this.cordova;
      StudentClient.get().onConnected.addObserver(new Observer() {

        @Override
        public void update(Observable observable, Object data) {
          cordova.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
              final PluginResult result = new PluginResult(PluginResult.Status.OK, false);
              callbackContext.sendPluginResult(result);
            }
          });

          try {
            StudentClient.get().registerCallBack(StudentClientCallBack.get());
          } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }
      });
    } else if(action.equals("registerCallBack")) {
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      this.cordova.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            try {
              StudentClient.get().registerCallBack(StudentClientCallBack.get());
              Toast.makeText(ctcx, "Register CallBack success", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
        
            }
        }
      });
    } else if(action.equals("customMessage")) {
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      this.cordova.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            try {
              StudentClient.get().sendTextMessage("A custom message");
              Toast.makeText(ctcx, "Custom Message success", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
      
            }   
        }
      });
    } else if(action.equals("stopAll")) {
      this.cordova.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            StudentClient.get().doStopCommand();
          } catch (Exception e) {
            
          }
        }
      });
    } else if(action.equals("raiseHand")) {
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      this.cordova.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            StudentClient.get().raiseHand(true);
            Toast.makeText(ctcx, "Hand Up", Toast.LENGTH_LONG).show();
          } catch (Exception e) {
            
          }
        }
      });
    } else if(action.equals("handDown")) {
      Context ctcx = this.cordova.getActivity().getApplicationContext();
      this.cordova.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            StudentClient.get().raiseHand(false);
            Toast.makeText(ctcx, "Hand Down", Toast.LENGTH_LONG).show();
          } catch (Exception e) {
            
          }
        }
      });
    } else if (action.equals("teacherActivation")) {
      teacherPlugin.teacherActivation(action + "saf", args, callbackContext, this.cordova);
    }
    return true;
  }

}
