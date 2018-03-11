package org.usc.teacher;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;

import com.usc.tools.UscCommonObservable;

/**
 * This class implements ITeacherCallBack and used for receiving messages from the teacher service
 */
public class TeacherClientCallBack extends ITeacherCallBack.Stub {

	private Context context;
	public UscCommonObservable onBytesMessage = new UscCommonObservable();
	public UscCommonObservable onTextMessage = new UscCommonObservable();
	public UscCommonObservable onStudentUpdated = new UscCommonObservable();
	public UscCommonObservable onThumbnailUpdated = new UscCommonObservable();

	public static TeacherClientCallBack get() {
		return Holder.INSTANCE;
	}

	public static class Holder {
		final static TeacherClientCallBack INSTANCE = new TeacherClientCallBack();
	}

	private TeacherClientCallBack() {
	}

	public void init(Context context) {
		this.context = context;

	}


	@Override
	public void studentConnected(String studentID) throws RemoteException {
		// Toast.makeText(context, "studentConnected: " + studentID,
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void studentDisconnected(String studentID) throws RemoteException {
		// Toast.makeText(context, "studentDisconnected: " + studentID,
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void studentUpdated(String studentID) throws RemoteException {
		onStudentUpdated.notifyObservers(studentID);
		// Toast.makeText(context, "studentUpdated: " + studentID,
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void studentImageUpdated(String studentID, Bitmap b) throws RemoteException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("studentID", studentID);
		map.put("image", b);
		onThumbnailUpdated.notifyObservers(map);
	}

	/**
	 * Event for text message arrived, notify to all listeners
	 * @param text - message as string
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void onTextMessage(String text) throws RemoteException {
		onTextMessage.notifyObservers(text);
	}

	/**
	 * Event for bytes array message arrived, notify to all listeners
	 * @param bytes - message as bytes array
	 * @throws RemoteException - remote-invocation errors
	 */
	@Override
	public void onBytesMessage(byte[] bytes) throws RemoteException {
		onBytesMessage.notifyObservers(bytes);
	}
}
