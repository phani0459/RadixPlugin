package com.usc.tools;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class CommonTools {
	public static boolean isPackageExists(Context context, String targetPackage) {
		List<ApplicationInfo> packages;
		PackageManager pm;
		pm = context.getPackageManager();
		packages = pm.getInstalledApplications(0);
		for (ApplicationInfo packageInfo : packages) {
			if (packageInfo.packageName.equals(targetPackage))
				return true;
		}
		return false;
	}
}
