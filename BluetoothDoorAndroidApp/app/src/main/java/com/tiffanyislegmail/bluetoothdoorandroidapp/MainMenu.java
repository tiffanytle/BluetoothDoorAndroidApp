package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;


public class MainMenu extends Activity implements View.OnClickListener {
    public static final String TAG = "writeException";
    public String address = "20:14:03:24:51:82"; //device address
    private static final UUID MY_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mBluetoothAdapter;
    public DataOutputStream os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //enable bluetooth
        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1000);
            long start = System.currentTimeMillis();
            long end = start + 10*1000; // 15 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end); //wait 10 seconds
        }

        //external bluetooth device
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket tmp = null;
        BluetoothSocket mmSocket = null;

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            pairDevice(device);
            Toast.makeText(getApplicationContext(), "Pairing Device", Toast.LENGTH_SHORT).show();
            long start = System.currentTimeMillis();
            long end = start + 15*1000; // 15 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end); //wait 15 seconds
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                Toast.makeText(getApplicationContext(), "Device not found, enable BT and restart", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Paired", Toast.LENGTH_SHORT).show();
        }

        //if device is paired already, start making connection
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                mmSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.d(TAG, "Socket not created");
                e1.printStackTrace();
            }
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG, "Cannot connect");
                } catch (IOException e1) {
                    Log.d(TAG, "Socket not closed");
                    e1.printStackTrace();
                }
            }
        }

        //filters
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(BTReceiver, filter1);
        this.registerReceiver(BTReceiver, filter2);
        this.registerReceiver(BTReceiver, filter3);

        Button clickResetPassword = (Button) findViewById(R.id.resetPasswordBtn);
        Button clickBluetooth = (Button) findViewById(R.id.bluetooth);
        Button LockDoorBtn = (Button) findViewById(R.id.lockDoorBtn);
        Button UnlockDoorBtn = (Button) findViewById(R.id.unlockDoorBtn);
        clickResetPassword.setOnClickListener(this);
        clickBluetooth.setOnClickListener(this);
        LockDoorBtn.setOnClickListener(this);
        UnlockDoorBtn.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lockDoorBtn:
                Toast.makeText(getApplicationContext(), "Locking", Toast.LENGTH_SHORT).show();
                try {
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});

                    BluetoothSocket clientSocket = (BluetoothSocket) m.invoke(device, 1);

                    clientSocket.connect();

                    os = new DataOutputStream(clientSocket.getOutputStream());

                    new clientSock().start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("BLUETOOTH Lock", e.getMessage());
                }
                break;

            case R.id.unlockDoorBtn:
                Toast.makeText(getApplicationContext(), "UnLocking", Toast.LENGTH_SHORT).show();
                try {
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});

                    BluetoothSocket clientSocket = (BluetoothSocket) m.invoke(device, 1);

                    clientSocket.connect();

                    os = new DataOutputStream(clientSocket.getOutputStream());

                    new clientSock1().start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("BLUETOOTH Lock", e.getMessage());
                }
                break;

            case R.id.resetPasswordBtn:
                Intent intent_reset = new Intent(v.getContext(), CreateNewUser.class);
                startActivity(intent_reset);
                break;
            case R.id.bluetooth:
                Intent intent1 = new Intent(v.getContext(), bluetooth.class);
                startActivity(intent1);
                break;
        }
    }

    public class clientSock extends Thread {
        public void run () {
            try {
                os.writeBytes("L"); // anything you want
                os.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }
    public class clientSock1 extends Thread {
        public void run () {
            try {
                os.writeBytes("U"); // anything you want
                os.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver BTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Do something if connected
                Toast.makeText(getApplicationContext(), "BT Connected", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Do something if disconnected
                Toast.makeText(getApplicationContext(), "BT Disconnected", Toast.LENGTH_SHORT).show();
            }
            //else if...
        }
    };

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendDataToPairedDevice(String message , BluetoothDevice device){
        byte[] toSend = message.getBytes();
        try {
            //UUID applicationUUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            OutputStream mmOutStream = socket.getOutputStream();
            mmOutStream.write(toSend);
            // Your Data is sent to  BT connected paired device ENJOY.
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }
}
