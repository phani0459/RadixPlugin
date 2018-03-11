package com.usc.tools;

import java.util.Observable;
import android.util.Log;

public class UscCommonObservable extends Observable {
	@Override
	public void notifyObservers(Object data) {
		// TODO Auto-generated method stub
		setChanged();
		super.notifyObservers(data);
	}

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

}
