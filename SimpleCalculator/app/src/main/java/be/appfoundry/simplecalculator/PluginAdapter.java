package be.appfoundry.simplecalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PluginAdapter extends BaseAdapter {

	private List<Plugin> plugins = new ArrayList<>();

	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return plugins.size();
	}

	@Override
	public Plugin getItem(int position) {
		return plugins.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plugin, null);

		Plugin plugin = getItem(position);

		TextView operationSymbol = (TextView) convertView.findViewById(R.id.operation_symbol);

		operationSymbol.setText(plugin.getOperationSymbol());

		return convertView;
	}

}
