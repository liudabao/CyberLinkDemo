package com.example.lenovo.cyberlinkdemo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.cyberlinkdemo.util.HttpUtil;
import com.example.lenovo.cyberlinkdemo.util.ParseUtil;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SearchResponseListener{


    Button send;
    Button receive;
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
        receive=(Button)findViewById(R.id.receive);

    }

    private void initControl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                controlPoint=new ControlPoint();
                controlPoint.addSearchResponseListener(MainActivity.this);
                //controlPoint.addNotifyListener(MainActivity.this);
                //controlPoint.addDeviceChangeListener(MainActivity.this);
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
                        controlPoint.search("urn:schemas-upnp-org:service:AVTransport:1");
                    }
                }).start();


            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getDevice(controlPoint);
                Device device=controlPoint.getDevice("客厅的小米盒子");
                Log.e("device receive", device.getDeviceType());
                File file=new File(Environment.getExternalStorageDirectory(), "Download/test.mp4");
                Action action=device.getAction("SetAVTransportURI");
                action.setArgumentValue("InstanceID", "0");
                action.setArgumentValue("CurrentURI", "http://192.168.168.103/"+file.getPath());
                action.setArgumentValue("CurrentURIMetaData", "");
                action.setArgumentValue("Header_SOAPACTION", "urn:upnp-org:serviceId:AVTransport#SetAVTransportURI");
                if(action.postControlAction()){

                    ArgumentList list=action.getOutputArgumentList();
                    int size=list.size();
                    for(int i=0;i <size; i++){
                        Argument argument=list.getArgument(i);
                        Log.e("Argument", argument.getName()+" "+argument.getValue());
                    }
                }
                else {
                    Log.e("Action", "failed");
                }

            }
        });
    }


    @Override
    public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
        String uuid = ssdpPacket.getUSN();
        String target = ssdpPacket.getST();
        String location = ssdpPacket.getLocation();
        String server=ssdpPacket.getServer();
        String host=ssdpPacket.getHost();
       // Log.e("Device uid", uuid);
      //  Log.e("Device target", target);
        Log.e("Device location", location);
       // Log.e("Device server", server);
        Log.e("Device host", host);
        String data= HttpUtil.get(location);

        ParseUtil.parseXml(data);
        //Log.e("url", data);
    }

    private void getDevice(ControlPoint controlPoint){
        DeviceList rootDevList = controlPoint.getDeviceList();
        int nRootDevs = rootDevList.size();
        for (int n=0; n<nRootDevs; n++) {
            Device dev = rootDevList.getDevice(n);
            String devName = dev.getFriendlyName();
            Log.e("device name", devName);
        }
    }

}
;