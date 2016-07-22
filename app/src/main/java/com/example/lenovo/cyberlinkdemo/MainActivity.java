package com.example.lenovo.cyberlinkdemo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

public class MainActivity extends AppCompatActivity implements NotifyListener, SearchResponseListener, DeviceChangeListener {


    Button send;
    ControlPoint controlPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initControl();
        initEvent();
    }

    private void init() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Log.e("TAG", wm.getConnectionInfo() + "");
        WifiManager.MulticastLock multicastLock = wm.createMulticastLock("multicastLock");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();
        send = (Button) findViewById(R.id.send);

    }

    private void initControl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                controlPoint=new ControlPoint();
                controlPoint.addSearchResponseListener(MainActivity.this);
                //controlPoint.addNotifyListener(MainActivity.this);
                controlPoint.addDeviceChangeListener(MainActivity.this);
                controlPoint.start();
            }
        }).start();
    }

    private void initEvent(){

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Device", "find");
                        controlPoint.search("ssdp:all");
                    }
                }).start();


            }
        });
    }

    @Override
    public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
        String uuid = ssdpPacket.getUSN();
        String target = ssdpPacket.getST();
        String location = ssdpPacket.getLocation();
        String server=ssdpPacket.getServer();
        String host=ssdpPacket.getHost();
        Log.e("Device uid", uuid);
        Log.e("Device target", target);
        Log.e("Device location", location);
        Log.e("Device server", server);
        Log.e("Device host", host);
    }

    @Override
    public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
        String uuid = ssdpPacket.getUSN();
        String target = ssdpPacket.getST();
        String location = ssdpPacket.getLocation();
        String server=ssdpPacket.getServer();
        String host=ssdpPacket.getHost();
        Log.e("Device uid", uuid);
        Log.e("Device target", target);
        Log.e("Device location", location);
        Log.e("Device server", server);
        Log.e("Device host", host);
    }

    @Override
    public void deviceAdded(Device device) {
        String devName = device.getFriendlyName();
        Log.e("Device name", devName);
    }

    @Override
    public void deviceRemoved(Device device) {
        String devName = device.getFriendlyName();
        Log.e("Device name", devName);
    }
}
