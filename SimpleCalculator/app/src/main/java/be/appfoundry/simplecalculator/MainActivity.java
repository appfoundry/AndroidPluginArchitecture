package be.appfoundry.simplecalculator;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.appfoundry.simplecalculatorplugin.IPluginInterface;

public class MainActivity extends AppCompatActivity {

	private EditText number1;
	private EditText number2;
	private TextView operationSymbol;
	private TextView result;
	private ListView list;

	private PluginAdapter pluginAdapter;

	private IPluginInterface pluginInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		number1 = (EditText) findViewById(R.id.number_1);
		number2 = (EditText) findViewById(R.id.number_2);
		operationSymbol = (TextView) findViewById(R.id.operation_symbol);
		result = (TextView) findViewById(R.id.result);
		list = (ListView) findViewById(R.id.list);

		pluginAdapter = new PluginAdapter();
		list.setAdapter(pluginAdapter);
		list.setOnItemClickListener(onPluginClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();

		pluginAdapter.setPlugins(findPlugins());

		IntentFilter packageFilter = new IntentFilter();
		packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		packageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		packageFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		packageFilter.addDataScheme("package");

		registerReceiver(packageChangeReceiver, packageFilter);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(packageChangeReceiver);

		super.onPause();
	}

	private List<Plugin> findPlugins() {
		List<ResolveInfo> pluginServices = getPackageManager().queryIntentServices(
				new Intent("be.appfoundry.simplecalculator.PLUGIN"),
				PackageManager.GET_META_DATA
		);

		List<Plugin> plugins = new ArrayList<>();
		for(ResolveInfo pluginService : pluginServices) {
			Plugin plugin = new Plugin(pluginService);
			plugins.add(plugin);
		}

		return plugins;
	}

	private BroadcastReceiver packageChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			pluginAdapter.setPlugins(findPlugins());
		}
	};

	private AdapterView.OnItemClickListener onPluginClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Plugin plugin = pluginAdapter.getItem(position);

			operationSymbol.setText(plugin.getOperationSymbol());

			Intent bindIntent = new Intent();
			bindIntent.setClassName(plugin.getServicePackageName(), plugin.getServiceName());
			bindService(bindIntent, pluginServiceConnection, Context.BIND_AUTO_CREATE);
		}
	};

	private ServiceConnection pluginServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pluginInterface = IPluginInterface.Stub.asInterface(service);

			double number1Value = Double.valueOf(number1.getText().toString());
			double number2Value = Double.valueOf(number2.getText().toString());
			double value = 0;

			try {
				value = pluginInterface.calculate(number1Value, number2Value);
				result.setText(String.valueOf(value));
			} catch(RemoteException e) {
				result.setText("Error");
			}

			unbindService(pluginServiceConnection);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			pluginInterface = null;
		}
	};

}
