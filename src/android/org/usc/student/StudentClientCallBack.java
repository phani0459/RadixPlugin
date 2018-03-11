package org.usc.student;

import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;
import android.app.Activity;

public class StudentClientCallBack extends IStudentCallBack.Stub {
	public static StudentClientCallBack get() {
		return Holder.INSTANCE;
	}

	public static class Holder {
		final static StudentClientCallBack INSTANCE = new StudentClientCallBack();
	}

	private StudentClientCallBack() {
	}

	
	private Context context;

	public void init(Context context) {
		this.context = context;

	}

	@Override
	public void studentConnected(final String teacherID) throws RemoteException {
		showToast("studentConnected: " + teacherID);

	}

	private void showToast(final String toast) {
		((Activity) (context)).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
			}
		});

	}

	@Override
	public void studentDisconnected() throws RemoteException {
		showToast("studentDisconnected");

	}

	@Override
	public void onStopCommand() throws RemoteException {
		showToast("onStopCommand");

	}

	@Override
	public void onSilenceCommand(boolean silence) throws RemoteException {
		showToast("onSilenceCommand");

	}

	@Override
	public void onTextMessage(String text) throws RemoteException {
		showToast("onTextMessage: " + text);
	}

	@Override
	public void onBytesMessage(byte[] bytes) throws RemoteException {
		showToast("onBytesMessage, bytes recived: " + bytes.length);
	}
}
