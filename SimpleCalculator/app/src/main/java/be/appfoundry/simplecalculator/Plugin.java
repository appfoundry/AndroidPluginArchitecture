package be.appfoundry.simplecalculator;

import android.content.pm.ResolveInfo;
import android.os.Bundle;

public class Plugin {

	private String serviceName;
	private String servicePackageName;
	private String operationSymbol;
	private int apiLevel;

	public Plugin(ResolveInfo resolveInfo) {
		serviceName = resolveInfo.serviceInfo.name;
		servicePackageName = resolveInfo.serviceInfo.packageName;

		Bundle metaData = resolveInfo.serviceInfo.metaData;
		operationSymbol = metaData.getString("be.appfoundry.simplecalculator.META_DATA_OPERATION_SYMBOL", "");
		apiLevel = metaData.getInt("be.appfoundry.simplecalculator.META_DATA_API_LEVEL", 0);
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServicePackageName() {
		return servicePackageName;
	}

	public String getOperationSymbol() {
		return operationSymbol;
	}

	public int getApiLevel() {
		return apiLevel;
	}
}
