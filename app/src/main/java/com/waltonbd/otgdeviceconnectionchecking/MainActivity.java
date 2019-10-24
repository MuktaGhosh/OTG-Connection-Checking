package com.waltonbd.otgdeviceconnectionchecking;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.example.USB_PERMISSION";
    private static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    String TAG = "OTG   ";
    // private usbReceiver myReceiver;
    private static final String ACTION_USB_PERMISSIONs = "com.blecentral.USB_PERMISSION";
    Context context;
    UsbManager usbManager;
    PendingIntent permissionIntent;
    Button btnpass, btnfail;
    TextView tv_otg;
    IntentFilter filter;

    BootUpReceiver bootupreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        tv_otg = findViewById(R.id.tv_otg);
        btnpass = findViewById(R.id.btn_pass);
        btnfail = findViewById(R.id.btn_fail);


        Connectioncheck();

        bootupreceiver = new BootUpReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_DETACHED);

        filter.setPriority(100);

        registerReceiver(bootupreceiver, filter);
        buttonclick();
    }


    public  void Connectioncheck()
    {
        tv_otg.setText("Please connect a OTG storage device for testing!");
        tv_otg.setTextColor(Color.RED);

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            tv_otg.setText("ALREADY connected !");
            tv_otg.setTextColor(Color.GREEN);
        }

    }
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(bootupreceiver);
    }


    public class BootUpReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("USB", "Decive Connected -> " + action);

            if (action.equalsIgnoreCase(ACTION_USB_ATTACHED)) {

                UsbDevice device = (UsbDevice) intent
                        .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    int vendorID = device.getVendorId();
                    int productID = device.getProductId();

                    //If Product and Vendor Id match then set boolean "true" in global variable
                    tv_otg.setText("External OTG storage device connected !");
                    tv_otg.setTextColor(Color.GREEN);
                    Log.e("true", "true");

                }

            } else if (action.equalsIgnoreCase(ACTION_USB_DETACHED)) {
                //When ever device Detach set your global variable to "false"
                tv_otg.setText("External OTG storage device disconnected !");
                tv_otg.setTextColor(Color.RED);
                Log.e("ACTION_USB_DETACHED", "ACTION_USB_DETACHED");
            }


        }
    }

    public void buttonclick() {

        btnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Passed !", Toast.LENGTH_SHORT).show();


            }
        });
        btnfail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
