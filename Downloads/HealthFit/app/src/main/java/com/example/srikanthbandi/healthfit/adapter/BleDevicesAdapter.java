package com.example.srikanthbandi.healthfit.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;




public class BleDevicesAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Context mContext;

    private final ArrayList<BluetoothDevice> leDevices;
    private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<>();

    public BleDevicesAdapter(Context context) {
        leDevices = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void addDevice(BluetoothDevice device, int rssi) {
        if (!leDevices.contains(device)) {
            leDevices.add(device);
        }
        rssiMap.put(device, rssi);
    }

    public BluetoothDevice getDevice(int position) {
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
            viewHolder = new ViewHolder();
            viewHolder.txt_dialog_device_address = (TextView) view.findViewById(R.id.txt_dialog_device_address);
            viewHolder.txt_dialog_device_name = (TextView) view.findViewById(R.id.txt_dialog_device_name);
            viewHolder.txt_dialog_device_address.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(mContext));
            viewHolder.txt_dialog_device_name.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(mContext));
            //viewHolder.txt_dialog_heart_connected = (TextView) view.findViewById(R.id.txt_dialog_heart_connected);
            //viewHolder.txt_dialog_heart_connected.setTypeface(Utility.setTypeFace_fontawesome(mContext));
            //viewHolder.txt_dialog_device_name.setTypeface(Utility.setTypeFace_setTypeFace_proximanova_regular(mContext));
            //viewHolder.txt_dialog_device_address.setTypeface(Utility.setTypeFace_setTypeFace_proximanova_regular(mContext));
            viewHolder.mLlyone = (LinearLayout) view.findViewById(R.id.mLlyone);
            viewHolder.mLlytwo = (LinearLayout) view.findViewById(R.id.mLlytwo);
            viewHolder.mLlythree = (LinearLayout) view.findViewById(R.id.mLlythree);
            viewHolder.mLlyfour = (LinearLayout) view.findViewById(R.id.mLlyfour);
            viewHolder.mLlyfive = (LinearLayout) view.findViewById(R.id.mLlyfive);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mLlyfive.setVisibility(View.VISIBLE);
        viewHolder.mLlyfour.setVisibility(View.VISIBLE);
        viewHolder.mLlythree.setVisibility(View.VISIBLE);
        viewHolder.mLlytwo.setVisibility(View.VISIBLE);
        viewHolder.mLlyone.setVisibility(View.VISIBLE);
        BluetoothDevice device = leDevices.get(i);
        String deviceName = device.getName();

        if (deviceName != null && deviceName.length() > 0) {
            if(deviceName.equalsIgnoreCase("heart rate sensor")){
                deviceName = "Fitmetrix HR Monitor";
            }
            viewHolder.txt_dialog_device_name.setText(deviceName);
        } else {
            viewHolder.txt_dialog_device_name.setText(R.string.unknown_device);
        }
        viewHolder.txt_dialog_device_address.setText(device.getAddress());
       // viewHolder.txt_dialog_heart_connected.setTextColor(Utility.getThemeColor(mContext));
        //viewHolder.txt_dialog_heart_connected.setText("");
        int value = rssiMap.get(device) - (2 * rssiMap.get(device));
        if (value > 24 && value < 31) {
            viewHolder.mLlyfive.setVisibility(View.VISIBLE);
            viewHolder.mLlyfour.setVisibility(View.VISIBLE);
            viewHolder.mLlythree.setVisibility(View.VISIBLE);
            viewHolder.mLlytwo.setVisibility(View.VISIBLE);
            viewHolder.mLlyone.setVisibility(View.VISIBLE);
//full
        } else if (value >= 31 && value <= 50) {
            viewHolder.mLlyfive.setVisibility(View.GONE);

        } else if (value > 50 && value <= 68) {
            viewHolder.mLlyfive.setVisibility(View.GONE);
            viewHolder.mLlyfour.setVisibility(View.GONE);

        } else if (value > 68 && value < 72) {
            viewHolder.mLlyfive.setVisibility(View.GONE);
            viewHolder.mLlyfour.setVisibility(View.GONE);
            viewHolder.mLlythree.setVisibility(View.GONE);

        } else if (value > 71 && value < 80) {
            viewHolder.mLlyfive.setVisibility(View.GONE);
            viewHolder.mLlyfour.setVisibility(View.GONE);
            viewHolder.mLlythree.setVisibility(View.GONE);
            viewHolder.mLlytwo.setVisibility(View.GONE);
        }
        else {
            viewHolder.mLlyfive.setVisibility(View.GONE);
            viewHolder.mLlyfour.setVisibility(View.GONE);
            viewHolder.mLlythree.setVisibility(View.GONE);
            viewHolder.mLlytwo.setVisibility(View.GONE);
            viewHolder.mLlyone.setVisibility(View.GONE);
        }
        return view;

    }

    private static class ViewHolder {
        TextView txt_dialog_device_name;
        TextView txt_dialog_device_address;
        TextView txt_dialog_heart_connected;
        LinearLayout mLlyone;
        LinearLayout mLlytwo;
        LinearLayout mLlythree;
        LinearLayout mLlyfour;
        LinearLayout mLlyfive;
    }
}
