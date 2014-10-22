package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class bluetooth extends Activity {
    private Button bluetoothOn;
    private Button bluetoothOff;
    private Button bluetoothScan;
    private Button bluetoothPaired;
    private BluetoothAdapter BA;
    private ArrayAdapter<String> btArrayAdapter;
    ListView listDevicesFound;
    private Set<BluetoothDevice> pairedDevices;

    //private DeviceListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BA = manager.getAdapter();
        bluetoothOn = (Button) findViewById(R.id.btnOn);
        bluetoothOff = (Button) findViewById(R.id.btnOff);
        bluetoothPaired = (Button) findViewById(R.id.btnPaired);
        bluetoothScan = (Button) findViewById(R.id.btnScan);
        listDevicesFound = (ListView) findViewById(R.id.devicesFound);
        btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);
        //make bluetooth discoverable
        Intent discoverableIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        IntentFilter intFilter = new IntentFilter();
        intFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intFilter);  // Don't forget to unregister during onDestroy

        asd();
    }

    public void asd() {
        bluetoothOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!BA.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    Toast.makeText(getApplicationContext(), "Turned on"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Already on",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bluetoothOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                BA.disable();
                Toast.makeText(getApplicationContext(), "Turned off",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //scan for devices
        bluetoothScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!BA.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Turn on bluetooth first"
                            , Toast.LENGTH_SHORT).show();
                }
                if (BA.isDiscovering()) {
                    BA.cancelDiscovery();
                    Toast.makeText(getApplicationContext(), "Scan Stopped"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    btArrayAdapter.clear();
                    BA.startDiscovery();
                    Toast.makeText(getApplicationContext(), "Scan Started"
                            , Toast.LENGTH_SHORT).show();
                }

            }

        });
        //list devices
        bluetoothPaired.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pairedDevices = BA.getBondedDevices();

                for (BluetoothDevice bt : pairedDevices)
                    btArrayAdapter.add(bt.getName() + "\n" + bt.getAddress());

                Toast.makeText(getApplicationContext(), "Showing Paired Devices",
                        Toast.LENGTH_SHORT).show();
                //@SuppressWarnings("rawtypes")
                //final ArrayAdapter adapter = new ArrayAdapter(bluetooth.this,android.R.layout.simple_list_item_1, list);
                //listDevicesFound.setAdapter(adapter);
            }
        });


    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btArrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "New Device Found",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BA.isDiscovering()) {  //stop looking for devices
            BA.cancelDiscovery();
        }
    }

}
