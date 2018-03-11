package org.usc.student;

import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.usc.tools.UscCommonObservable;

public class StudentClient extends IStudentService.Stub {


    private Context context;
    protected IStudentService studentService;
    public boolean active;
    public UscCommonObservable onConnected = new UscCommonObservable();

    public static StudentClient get() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        final static StudentClient INSTANCE = new StudentClient();
    }

    private StudentClient() {
    }

    /**
     * Initialize the binding to service process and the StudentClientCallBack that used for
     * receiving messages from the student service
     * @param context - the calling context application.
     */
    public void init(Context context) {

        this.context = context;

        Intent intent = new Intent();
        intent.setClassName("com.usco.student", "org.usc.student.StudentService");
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        StudentClientCallBack.get().init(context);
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        /**
         * This is called when the connection with the service has been established, giving us the
         * object we can use to interact with the service. We are communicating with the service
         * using a Messenger, so here we get a client-side representation of that from the raw
         * IBinder object.
         *
         * @param className - the name of the service that has been connected.
         * @param service - communication channel, which you can now make calls on.
         */
        public void onServiceConnected(ComponentName className, IBinder service) {
            studentService = IStudentService.Stub.asInterface(service);
            active = true;
            onConnected.notifyObservers();
        }

        /**
         * This is called when the connection with the service has been unexpectedly disconnected
         * @param className - the name of the service whose connection has been lost.
         */
        public void onServiceDisconnected(ComponentName className) {
            studentService = null;
            active = false;
        }
    };
    private StudentClientCallBack studentClientCallBack;

    /**
     * Waits for the binding to service will end
     */
    public void waitforServer() {
        if (studentService == null) {
            init(context);

        }
        for (int i = 0; i < 20; i++) {
            if (studentService == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    
                }
            } else {
                break;
            }
        }
        if (studentService == null) {
            init(context);
        }
        for (int i = 0; i < 20; i++) {
            if (studentService == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    
                }
            } else {
                break;
            }
        }
    }

    // register callback to get messages from teacher
    @Override
    public void registerCallBack(IStudentCallBack cb) throws RemoteException {
        studentService.registerCallBack(cb);

    }

    IStudentService getStudentService() {
        waitforServer();
        return studentService;
    }

    /**
     * Get teacher information
     * @param teacherID -teacher id as string
     * @return- information Map<String,String> with values: ip, os
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public Map getTeacherData(String teacherID) throws RemoteException {
        return getStudentService().getTeacherData(teacherID);
    }

    /**
     * Gets the current connected teacher or null if no teacher is connected
     * @return - connected teacher is as string
     * @throws RemoteException
     */
    @Override
    public String currentTeacher() throws RemoteException {
        return getStudentService().currentTeacher();
    }

    //

    /**
     * Get a list of detected teacher
     * @return - List of detected teachers as List<Strign>
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public List<String> getTeachers() throws RemoteException {
        return getStudentService().getTeachers();
    }

    /**
     * Connect to teacher by teacher ID
     * @param teacherID - teacher id as string
     * @throws RemoteException
     */
    @Override
    public void selectTeacher(String teacherID) throws RemoteException {
        getStudentService().selectTeacher(teacherID);
    }

    /**
     * Insert int value to the program preferences
     * @param key - key as string
     * @param value- int to store
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void setIntPrefrence(String key, int value) throws RemoteException {
        getStudentService().setIntPrefrence(key, value);
    }

    /**
     * Insert string value to the program preferences
     * @param key - key as string
     * @param value - string to store
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void setStringPrefrence(String key, String value) throws RemoteException {
        getStudentService().setStringPrefrence(key, value);
    }

    /**
     * Insert bool value to the program preferences
     * @param key - key as string
     * @param value - bool to store
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void setBoolPrefrence(String key, boolean value) throws RemoteException {
        getStudentService().setBoolPrefrence(key, value);
    }

    /**
     * Get int value from the program preferences for the given key
     * @param key - key as string
     * @param def - default int value to return when failed
     * @return- int value
     * @throws RemoteException
     */
    @Override
    public int getIntPrefrence(String key, int def) throws RemoteException {
        return getStudentService().getIntPrefrence(key, def);
    }

    /**
     * Get string value from the program preferences for the given key
     * @param key - key as string
     * @param def - default string value to return when failed
     * @return- string value
     * @throws RemoteException
     */
    @Override
    public String getStringPrefrence(String key, String def) throws RemoteException {
        return getStudentService().getStringPrefrence(key, def);
    }

    /**
     * Get bool value from the program preferences for the given key
     * @param key - key as string
     * @param def - default bool value to return when failed
     * @return- bool value
     * @throws RemoteException
     */
    @Override
    public boolean getBoolPrefrence(String key, boolean def) throws RemoteException {
        return getStudentService().getBoolPrefrence(key, def);
    }

    /**
     * Send byte array to teacher, the teacher will receive the message in TeacherClientCallBack
     * onBytesMessage
     * @param bytes - message as byte array
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void sendBytesMessage(byte[] bytes) throws RemoteException {
        getStudentService().sendBytesMessage(bytes);

    }

    /**
     * Send text to teacher, the teacher will receive the message in TeacherClientCallBack
     * onTextMessage
     * @param text - message as string
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void sendTextMessage(String text) throws RemoteException {
        getStudentService().sendTextMessage(text);
    }

    /**
     * Raise hand and unraise on connected teacher
     * @param isRaise - true for raise and false for unraise
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void raiseHand(boolean isRaise) throws RemoteException {
        getStudentService().raiseHand(isRaise);
    }

    @Override
    public void doStopCommand() throws RemoteException {
        getStudentService().doStopCommand();

    }

    /**
     * Hide student from application drawer and notification bar ( floating toolbar )
     * @throws RemoteException - remote-invocation errors
     */
    @Override
    public void hideStudent() throws RemoteException {
        getStudentService().hideStudent();

    }

    @Override
    public void showStudent() throws RemoteException {
        getStudentService().showStudent();

    }

    @Override
    public void sendFile(String srcFile) throws RemoteException {
        getStudentService().sendFile(srcFile);

    }

}
