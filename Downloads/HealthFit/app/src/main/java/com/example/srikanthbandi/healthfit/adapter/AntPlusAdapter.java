package com.example.srikanthbandi.healthfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.utility.Utility;

import java.util.ArrayList;


/**
 * Created by steven on 9/5/13.
 * Modified by olli on 3/28/2014.
 */
public class AntPlusAdapter extends BaseAdapter {
	private final LayoutInflater inflater;
	private Context mContext;

	private final ArrayList<String> leDevices;
	//private final HashMap<String,Integer> rssiMap = new HashMap<>();

	public AntPlusAdapter(Context context) {
		leDevices = new ArrayList<>();
		inflater = LayoutInflater.from(context);
		mContext = context;
	}

	public void addDevice(String device) {
		if (!leDevices.contains(device)) {
			leDevices.add(device);
		}
		//rssiMap.put(device,);
	}

	public String getDevice(int position) {
		return leDevices.get(position);
	}

	public void clear() {
		leDevices.clear();
	}

	@Override
	public int getCount() {
		return leDevices.size();
	}

	@Override
	public Object getItem(int i) {
		return leDevices.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		// General ListView optimization code.
		if (view == null) {
			view = inflater.inflate(R.layout.row_item_device, viewGroup,false);
			viewHolder = new AntPlusAdapter.ViewHolder();
			viewHolder.txt_dialog_device_address = (TextView) view.findViewById(R.id.txt_dialog_device_address);
			viewHolder.txt_dialog_device_name = (TextView) view.findViewById(R.id.txt_dialog_device_name);
			viewHolder.txt_dialog_device_address.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(mContext));
			viewHolder.txt_dialog_device_name.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(mContext));
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		String device = leDevices.get(i);
		//final String deviceName = device.getName();
		if (device != null && device.length() > 0) {
			viewHolder.txt_dialog_device_name.setText(device);
		}
		else {
			viewHolder.txt_dialog_device_name.setText(R.string.unknown_device);
		}
		//viewHolder.txt_dialog_device_address.setText(device.getAddress());
		//viewHolder.txt_dialog_heart_connected.setText("");

		return view;
	}

	private static class ViewHolder {
		TextView txt_dialog_device_name;
		TextView txt_dialog_device_address;
		TextView txt_dialog_heart_connected;
	}
}
