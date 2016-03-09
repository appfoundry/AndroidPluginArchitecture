package be.appfoundry.simplecalculatorplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

public abstract class PluginService extends Service {

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IPluginInterface.Stub mBinder = new IPluginInterface.Stub() {
		@Override
		public double calculate(double number1, double number2) throws RemoteException {
			return getResult(number1, number2);
		}
	};

	protected abstract double getResult(double number1, double number2);

}
