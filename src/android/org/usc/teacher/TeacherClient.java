package org.usc.teacher;

import java.util.List;
import java.util.Map;

import com.usc.tools.UscCommonObservable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author Radix tec
 * This claas impliments the ITeacherService to bind to the SDK service.
 * After the binding acomplish its used as a gate to all the teacher SDK methods
 */
public class TeacherClient extends ITeacherService.Stub {

	private Context context;
	protected ITeacherService teacherService;
	public UscCommonObservable onConnected = new UscCommonObservable();
	public boolean active;

	public static TeacherClient get() {
		return Holder.INSTANCE;
	}

	public static class Holder {
		final static TeacherClient INSTANCE = new TeacherClient();
	}

	private TeacherClient() {
	}

	/**
	 * Initialize the binding to service process and the TeacherClientCallBack that used for
	 * receiving messages from the teacher service
	 * @param context - the calling context application.
	 */
	public void init(Context context) {
		this.context = context;
		Intent intent = new Intent();
		intent.setClassName("com.usco.teacher", "org.usc.teacher.TeacherService");
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		TeacherClientCallBack.get().init(context);
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
			teacherService = ITeacherService.Stub.asInterface(service);
			active = true;
			onConnected.notifyObservers();
		}

		/**
		 * This is called when the connection with the service has been unexpectedly disconnected
		 * @param className - the name of the service whose connection has been lost.
		 */
		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			teacherService = null;
			active = false;
		}
	};
	private TeacherClientCallBack teacherClientCallBack;

	/**
	 * Waits for the binding to service will end
	 */
	public void waitforServer() {

		if (teacherService != null) {
			return;
		}
		for (int i = 0; i < 20; i++) {
			if (teacherService == null) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					
				}
			} else {
				break;
			}
		}

		if (teacherService != null) {
			return;
		}
		if (teacherService == null) {
			init(context);
		}
	}

	/**
	 * Register teacher callback handler.
	 * @param activityContext - the calling context application.
	 * @throws RemoteException - remote-invocation errors
	 */
	public void registerCallBack(Context activityContext) throws RemoteException {
		registerCallBack(TeacherClientCallBack.get());
	}


	/**
	 * Register teacher callback handler.
	 * @param cb - ITeacherCallBack, Callback object, used for receiving messages from the teacher
	 *              service
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void registerCallBack(ITeacherCallBack cb) throws RemoteException {
		teacherService.registerCallBack(cb);
	}

	ITeacherService getTeacerService() {
		waitforServer();
		return teacherService;
	}

	/*
	 * Send stop current operation to selected students ( or all if no one is selected )
	 */
	@Override
	public void stop() throws RemoteException {
		getTeacerService().stop();
	}

	/*
	 * Send attention command to selected students ( or all if no one is selected )
	 */
	@Override
	public void attention() throws RemoteException {
		getTeacerService().attention();

	}

	/* Select or deselect a student */
	@Override
	public void setStudentSelected(String studentID, boolean selected) throws RemoteException {
		getTeacerService().setStudentSelected(studentID, selected);

	}


	/**
	 * Get student information in a Map<String,String> with values: ip, os, handup: is hand raised,
	 * "true" or "false" values selected: is selected,"true" or "false" values
	 * @param studentID - student id as string
	 * @return - Map of student data as ip, os,handup and selected
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public Map getStudentData(String studentID) throws RemoteException {
		return getTeacerService().getStudentData(studentID);
	}

	/* Get connected students list */
	@Override
	public List<String> getStudents() throws RemoteException {
		return getTeacerService().getStudents();
	}

	/**
	 * Insert int value to the program preferences
	 * @param key - key as string
	 * @param value- int to store
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void setIntPrefrence(String key, int value) throws RemoteException {
		getTeacerService().setIntPrefrence(key, value);

	}

	/**
	 * Insert string value to the program preferences
	 * @param key - key as string
	 * @param value - string to store
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void setStringPrefrence(String key, String value) throws RemoteException {
		getTeacerService().setStringPrefrence(key, value);
	}

	/**
	 * Insert bool value to the program preferences
	 * @param key - key as string
	 * @param value - bool to store
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void setBoolPrefrence(String key, boolean value) throws RemoteException {
		getTeacerService().setBoolPrefrence(key, value);
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
		return getTeacerService().getIntPrefrence(key, def);
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
		return getTeacerService().getStringPrefrence(key, def);
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
		return getTeacerService().getBoolPrefrence(key, def);
	}

	/*
	 * Broadcast the teachers screen to selected students ( or all if no one is selected )
	 */
	@Override
	public void screenBroadcast() throws RemoteException {
		getTeacerService().screenBroadcast();
	}

	/* Remote control and view the first selected student */
	@Override
	public void remoteAssitance() throws RemoteException {
		getTeacerService().remoteAssitance();
	}

	/**
	 * Send byte array to selected students ( or all if no one is selected ),the student will
	 * receive the message in StudentClientCallBack the student will receive the message in
	 * StudentClientCallBack the student will receive the message in StudentClientCallBack
	 * onBytesMessage
	 * @param bytes -message
	 * @throws RemoteException
	 */
	@Override
	public void sendBytesMessage(byte[] bytes) throws RemoteException {
		getTeacerService().sendBytesMessage(bytes);
	}

	/**
	 * Send text to selected students ( or all if no one is selected ), the student will receive the
	 * message in StudentClientCallBack onTextMessage
	 * @param text
	 * @throws RemoteException
	 */
	@Override
	public void sendTextMessage(String text) throws RemoteException {
		getTeacerService().sendTextMessage(text);

	}

	/*
	 * Send file synchronously to selected students ( or all if no one is
	 *  src: full path of source file dest full path of destination
	 * file in the students device defaultFallBackLocaltion: location in
	 * students device were to place the file if the 'dest' value is not valid
	 * timeout: timeout in milliseconds to wait for send to complete or -1 for
	 * infinite
	 */

	/**
	 * Send file synchronously to selected students ( or all if no one is selected )
	 * @param src - full path of source file in the teacher device as string
	 * @param dest - full path of destination file in the students devices as string
	 * @param defaultFallBackLocaltion - location in students devices were to place the file if the
	 *                                    'dest' value is not valid
	 * @param timeout - timeout in milliseconds to wait for transfer to complete or -1 for infinite
	 * @return - error message if any
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public String sendFile(String src, String dest, String defaultFallBackLocaltion, int timeout) throws RemoteException {
		return getTeacerService().sendFile(src, dest, defaultFallBackLocaltion, timeout);
	}

	/**
	 * Collect file synchronously from selected students ( or all if no one is selected )
	 * @param src - full path of source file in the students device as string
	 * @param dest - full path of destination file in the teachers device as string
	 * @param defaultFallBackLocaltion - location in teachers device were to place the file if the
	 *                                    'dest' value is not valid
	 * @param timeout - timeout in milliseconds to wait for transfer to complete or -1 for infinite
	 * @return - error message if any
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public String collectFile(String src, String dest, String defaultFallBackLocaltion, int timeout) throws RemoteException {
		return getTeacerService().collectFile(src, dest, defaultFallBackLocaltion, timeout);
	}

	/**
	 * Hide teacher from application drawer and notification bar ( floating toolbar )
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void hideTeacher() throws RemoteException {
		getTeacerService().hideTeacher();
	}

	/**
	 * Show teacher in application drawer ( add floating toolbar via preferences )
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void showTeacher() throws RemoteException {
		getTeacerService().showTeacher();
	}

	public void activate() {

	}

	/**
	 * Only from Version 13.x
	 * Send chat message to selected students ( or all if no one is selected )
	 * @param message- message to send as string
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void sendChatMessage(String message) throws RemoteException {
		getTeacerService().sendChatMessage(message);
	}

	/**
	 * Only from Version 13.x
	 * Send popup message to selected students ( or all if no one is selected )
	 * @param message - message to send as string
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void sendPopupMessage(String message) throws RemoteException {
		getTeacerService().sendPopupMessage(message);
	}

	/**
	 * Only from Version 13.x
	 * Create a pop quiz file, 'answers'
	 * @param question- the question as string
	 * @param answers- should contain at least one and up to 4 possible answers as list of strings
	 * @param quizFileDest - should be a valid path for the created quiz to be saved as string
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void createAndSavePopQuiz(String question, List<String> answers, String quizFileDest) throws RemoteException {
		getTeacerService().createAndSavePopQuiz(question, answers, quizFileDest);
	}

	/**
	 * Only from Version 13.x
	 * Start a slide show of the selected students screens ( slides interval can be configured by
	 * 'prefrence_slide_show_interval' preference
	 * @throws RemoteException- remote-invocation errors
	 */
	@Override
	public void slideShow() throws RemoteException {
		getTeacerService().slideShow();
	}

	/**
	 * Only from Version 13.x
	 * Get students packages
	 * @return - list of maps List<Map<String,String>>, each package info will be stored in a hash
	 * map with the keys: 'package','isblocked'
	 * @throws RemoteException- remote-invocation errors
	 */
	@Override
	public List<Map> getAllStudentsPackages() throws RemoteException {
		return getTeacerService().getAllStudentsPackages();
	}

	/**
	 * Only from Version 13.x
	 * Set block or unblock for a package and send to selected students
	 * @param packageName- the name of the package to set block or unblock as string
	 * @param block- true for block or fale for unblock
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void blockUnBlockApp(String packageName, boolean block) throws RemoteException {
		getTeacerService().blockUnBlockApp(packageName, block);
	}

	/**
	 * Only from Version 13.x
	 * Send Reboot command to selected students
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void reboot() throws RemoteException {
		getTeacerService().reboot();
	}

	/**
	 * Only from Version 13.x
	 * Send shutdown command to selected students
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void shutdown() throws RemoteException {
		getTeacerService().shutdown();
	}

	/**
	 * Only from Version 13.x
	 * Send Wake command to selected students
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void wakeUp() throws RemoteException {
		getTeacerService().wakeUp();
	}

	/**
	 * Only from Version 13.x
	 * Send sleep command to selected students
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void sleep() throws RemoteException {
		getTeacerService().sleep();
	}

	/**
	 * Only from Version 13.x
	 * Remote install apk to selected students
	 * @param fullPathToApk - the full ptath to the package installation file
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void remoteInstall(String fullPathToApk) throws RemoteException {
		getTeacerService().remoteInstall(fullPathToApk);
	}

	/**
	 * Only from Version 13.x
	 * Block or release selected students web access
	 * @param isBlock - true for block or false for release
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void blockWeb(boolean isBlock) throws RemoteException {
		getTeacerService().blockWeb(isBlock);
	}

	/**
	 * Only from Version 13.x
	 * Save students attendance to file
	 * @param pathToAttendaceFile - full path of the file to save in
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void saveStudentAttendance(String pathToAttendaceFile) throws RemoteException {
		getTeacerService().saveStudentAttendance(pathToAttendaceFile);

	}

	/**
	 * Only from Version 13.x
	 * Open a collaborative whiteboard
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void openWhiteBoard() throws RemoteException {
		getTeacerService().openWhiteBoard();

	}

}
