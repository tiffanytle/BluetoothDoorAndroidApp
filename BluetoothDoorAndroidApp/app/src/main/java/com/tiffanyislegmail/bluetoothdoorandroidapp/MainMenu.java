/*----------------------------------------------------------------
 * Author:       Tiffany Le and Thuan Chu
 * File Name:    MainMenu.java
 * Created On:   09/10/2014
 * Last updated: 12/03/2014
 *
 * Description:  User chooses which action to execute in the main
 *               menu. Following actions include unlocking door,
 *               locking door, and resetting the password.
 *----------------------------------------------------------------*/
package com.tiffanyislegmail.bluetoothdoorandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainMenu extends Activity implements View.OnClickListener {
    public static final String TAG = "writeException";
    public boolean connected       = false;

    // Bluetooth address & info
    public String address = "20:14:03:24:51:82"; // Device address
    private static final UUID MY_UUID  = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    BluetoothDevice device;

    // SharedPreferences
    private shared_preferences sharedPrefs = new shared_preferences();
    Activity context = this;

    TextView      userNameBox, btProgressText;
    ProgressBar   btProgressBar;
    String        userName;

    String btEnable  = "Enabling Bluetooth...";
    String btPair    = "Pairing to Android Door...";
    String btConnect = "Connecting to Android Door...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialize TextView
        userNameBox    = (TextView) findViewById(R.id.displayUserName);
        btProgressText = (TextView) findViewById(R.id.btProgressText);

        // Initialize ProgressBar
        btProgressBar  = (ProgressBar) findViewById(R.id.bluetoothProgress);
        btProgressBar.setVisibility(View.GONE);

        // Initialize shared prefs to retrieving user name
        userName = sharedPrefs.getUserName(context);
        userNameBox.setText(userName);

        //intents for checking state of bluetooth devices
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);

        Button clickResetPassword = (Button) findViewById(R.id.resetPasswordBtn);
        Button LockDoorBtn        = (Button) findViewById(R.id.lockDoorBtn);
        Button UnlockDoorBtn      = (Button) findViewById(R.id.unlockDoorBtn);

        // Set on click listener for buttons
        clickResetPassword.setOnClickListener(this);
        LockDoorBtn.setOnClickListener(this);
        UnlockDoorBtn.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lockDoorBtn:
                btProgressBar.setVisibility(View.VISIBLE);
                enableBluetooth();
                if(findAndPairDevice()) {
                    try {
                        connectAndSend("l"); // send string "l" to lock device
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("BLUETOOTH Lock", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Lock failed! Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Android Door is out of range.",
                            Toast.LENGTH_SHORT).show();
                btProgressBar.setVisibility(View.INVISIBLE);
                btProgressText.setText("");
                break;

            case R.id.unlockDoorBtn:
                btProgressBar.setVisibility(View.VISIBLE);
                enableBluetooth();
                if(findAndPairDevice()) {
                    try {
                        connectAndSend("u"); // send string "u" to lock device
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("BLUETOOTH unLock", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Unlock failed! Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Android Door is out of range.",
                            Toast.LENGTH_SHORT).show();
                btProgressBar.setVisibility(View.INVISIBLE);
                btProgressText.setText("");
                break;

            case R.id.resetPasswordBtn:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage("Are you sure you want to reset your password?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener () {
                            public void onClick (DialogInterface dialog, int id) {
                                Intent intent_reset = new Intent(context, CreateNewUser.class);
                                startActivity(intent_reset);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }

            });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
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
    Gives user 10 seconds to accept enabling bluetooth.
     */
    private void enableBluetooth() {
        btProgressText.setText(btEnable);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1000);
            long start = System.currentTimeMillis();
            long end = start + 10*1000; // 10 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end); //wait 10 seconds
        }
    }

    /*
    This function looks for and pairs with designated bluetooth device.
    It will wait for 15 seconds for user to accept bluetooth pairing.
    It will return true if the device is paired, and false otherwise.
     */
    private boolean findAndPairDevice() {
        boolean pairedSuccess;
        device = mBluetoothAdapter.getRemoteDevice(address);
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            pairDevice(device);
            long start = System.currentTimeMillis();
            long end = start + 15*1000; // 15 * 1000 ms/sec = 15sec delay
            while (System.currentTimeMillis() < end); //wait 15 seconds
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                pairedSuccess = false;
            } else {
                btProgressText.setText(btPair);
                pairedSuccess = true;
            }
        } else { // Device is already paired
            btProgressText.setText(btPair);
            pairedSuccess = true;
        }
        return pairedSuccess;
    }

    /*
    This function will establish connection between bluetooth devices and send
    a message to lock or unlock the door.
     */
    private void connectAndSend(String action) {
        device = mBluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket mmSocket = null;
        byte[] toSend = action.getBytes();

        // Check if device is paired correctly. If paired, connect to device
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            // Attempt to create BluetoothSocket
            try {
                mmSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            } catch (IOException e1) {
               // Toast.makeText(getApplicationContext(), "Catch 1", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Socket not created");
                e1.printStackTrace();
            }
            // Connect to BluetoothSocket
            try {
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mmSocket.connect();
                Toast.makeText(getApplicationContext(), "connecting" , Toast.LENGTH_SHORT).show();
                btProgressText.setText(btConnect);
            } catch (IOException e) {
                try {
                    Log.e("","trying fallback...");
                    mmSocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket",
                            new Class[] {int.class}).invoke(device,1);
                    mmSocket.connect();

                    Log.e("", "Connected");
                }
                catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }

            // Attempt to send special character to lock or unlock
                try {
                    OutputStream mmOutStream = mmSocket.getOutputStream();
                    mmOutStream.write(toSend);
                    mmSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception during write", e);
                    //Toast.makeText(getApplicationContext(), "Catch 3", Toast.LENGTH_SHORT).show();
                }

        }
        else
            Toast.makeText(getApplicationContext(), "Attempted Connect, device not paired.",
                    Toast.LENGTH_SHORT).show();
    }

    //The BroadcastReceiver that listens for bluetooth status broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

                //Do something if connected
                Toast.makeText(getApplicationContext(), "BT Connected", Toast.LENGTH_SHORT).show();
                connected = true; //BT state connected
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Do something if disconnected
                Toast.makeText(getApplicationContext(), "BT Disconnected", Toast.LENGTH_SHORT).show();

                connected = false; //BT state disconnected
            }
        }
    };
    /*
    This function creates a bond between two devices, pairing them, allowing communication between
    the devices.
     */
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
