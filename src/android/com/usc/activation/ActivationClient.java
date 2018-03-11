package com.usc.activation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * @author admin
 */
public class ActivationClient extends IActivationService.Stub {

    public static final int PRODUCT_TYPE_TEACHER = 2;
    public static final int PRODUCT_TYPE_STUDENT = 1;
    
    private Context context;
    protected IActivationService activationService;
    protected boolean active;
    private String packageName;

    public static ActivationClient get() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        final static ActivationClient INSTANCE = new ActivationClient();
    }

    private ActivationClient() {
    }

    /**
     * Initilize the bind to activation service
     * The biding process calls the ServiceConnection.onServiceConnected method when it finished
     * @param packageName - the package name to activate as string(com.usco.student for student and com.usco.teacher for teacher).
     * @param context     - the calling context application.
     */
    public void init(String packageName, Context context) {

        this.packageName = packageName;
        this.context = context;

        Intent intent = new Intent();
        intent.setClassName(packageName, "com.usc.activation.ActivationService");
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * A callbacks class for monitoring the state of the service.
     * If you are going to implement your own IActivationService you will get called here on service connected and disconnected and you need to notify other relevant code parts.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        /**
         * Called when the connection with the service has been established, giving us the object we can use to interact with the service. We are communicating with the service using a Messenger, so here we get a client-side representation of that from the raw IBinder object.
         * @param className - The concrete component name of the service that has been connected.
         * @param service - The IBinder of the Service's communication channel, which you can now make calls on.
         */
        public synchronized void onServiceConnected(ComponentName className, IBinder service) {
            activationService = IActivationService.Stub.asInterface(service);
            active = true;
        }

        /**
         * This is called when the connection with the service has been unexpectedly disconnected
         * @param className - The concrete component name of the service whose connection has been lost.
         */
        public void onServiceDisconnected(ComponentName className) {
            activationService = null;
            active = false;
        }
    };

    public synchronized void waitforServer() {
        if (activationService != null) {//binding ecomplishd
            return;
        }
        for (int i = 0; i < 20; i++) {
            if (activationService == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    
                }
            }
        }

        if (activationService != null) {
            return;
        }
        init(ActivationClient.this.packageName, context);
    }

    /**
     * Checks if the SDK is activated.
     *
     * @param ProductType - student or teacher as int (PRODUCT_TYPE_STUDENT(1) or PRODUCT_TYPE_TEACHER(2))
     * @return - true if activated and false for not activated
     * @throws RemoteException - binder remote-invocation errors
     */
    
    public boolean isActivated(int ProductType) throws RemoteException {
        waitforServer();
        return activationService.isActivated(ProductType);
    }

    /**
     * Activate the SDK for the given productID
     *
     * @param productID   - the licence you got from Radix tec
     * @param ProductType - student or teacher as int (PRODUCT_TYPE_STUDENT(1) or PRODUCT_TYPE_TEACHER(2))
     * @return - true for activation success false for activation failed
     * @throws RemoteException - binder remote-invocation errors
     */
    
    public boolean activate(String productID, int ProductType) throws RemoteException {
        waitforServer();
        return activationService.activate(productID, ProductType);
    }

}
