package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;


public class MainMenu extends Activity implements View.OnClickListener {
    public static final String TAG = "writeException";

    public DataOutputStream os;

    // Bluetooth addresses & info
    public String address = "20:14:03:24:51:82"; //device address
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SPP UUID
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    BluetoothDevice device;

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;

    TextView userNameBox;
    String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userNameBox = (TextView) findViewById(R.id.displayUserName);

        // Retrieving user name
        userName = sharedPrefs.getUserName(context);
        userNameBox.setText(userName);





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
                enableBluetooth();
                if(findAndPairDevice()) {
                    Toast.makeText(getApplicationContext(), "Success find and pair", Toast.LENGTH_SHORT).show();
                    try {
                        connectAndSend("l"); // send string "l" to lock device
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("BLUETOOTH Lock", e.getMessage());
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Android Door is out of range.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.unlockDoorBtn:
                Toast.makeText(getApplicationContext(), "Unlocking Door", Toast.LENGTH_SHORT).show();
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

    /*
    This function enables bluetooth if it is disabled.
     */
    private void enableBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1000);
            long start = System.currentTimeMillis();
            long end = start + 10*1000; // 15 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end); //wait 10 seconds
            Toast.makeText(getApplicationContext(), "Bluetooth is now enabled", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
    }

    private boolean findAndPairDevice() {
        boolean pairedSuccess;
        device = mBluetoothAdapter.getRemoteDevice(address);
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            pairDevice(device);
            long start = System.currentTimeMillis();
            long end = start + 15*1000; // 15 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end); //wait 15 seconds
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                Toast.makeText(getApplicationContext(), "Device NOT Paired", Toast.LENGTH_SHORT).show();
                pairedSuccess = false;
            } else {
                Toast.makeText(getApplicationContext(), "Device Paired", Toast.LENGTH_SHORT).show();
                pairedSuccess = true;
            }
        } else { // Device is already paired
            pairedSuccess = true;
            Toast.makeText(getApplicationContext(), "Device Already Paired", Toast.LENGTH_SHORT).show();
        }
        return pairedSuccess;
    }

    private void connectAndSend(String action) {
        device = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket mmSocket = null;
        byte[] toSend = action.getBytes();

        // Check if device is paired correctly. If paired, connect to device
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            Toast.makeText(getApplicationContext(), "Attempting to create socket", Toast.LENGTH_SHORT).show();
            // Attempt to create BluetoothSocket
            try {
                mmSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e1) {
                Toast.makeText(getApplicationContext(), "Catch 1", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Socket not created");
                e1.printStackTrace();
            }
            // Connect to BluetoothSocket
            try {
                mmSocket.connect();
                Toast.makeText(getApplicationContext(), "Attempting to connect", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Catch 2", Toast.LENGTH_SHORT).show();
                try {
                    // Close BluetoothSocket
                    mmSocket.close();
                    Log.d(TAG, "Cannot connect");
                } catch (IOException e1) {
                    Log.d(TAG, "Socket not closed");
                    e1.printStackTrace();
                }
            }

            // Attempt to send special character to lock or unlock
            try {
                OutputStream mmOutStream = mmSocket.getOutputStream();
                mmOutStream.write(toSend);
                Toast.makeText(getApplicationContext(), "Able to send.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
                Toast.makeText(getApplicationContext(), "Catch 3", Toast.LENGTH_SHORT).show();

            }
        }
        else
            Toast.makeText(getApplicationContext(), "Attempted Connect, device not paired.", Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
