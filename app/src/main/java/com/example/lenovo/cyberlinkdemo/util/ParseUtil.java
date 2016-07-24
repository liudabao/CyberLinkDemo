package com.example.lenovo.cyberlinkdemo.util;

import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liumin on 2016/7/24.
 */
public class ParseUtil {

    public static String parseXml(String data){

        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();
            InputStream inputStream=new ByteArrayInputStream(data.getBytes());
            parser.setInput(inputStream, "utf-8");
            int eventType=parser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:

                        String nodeName=parser.getName();

                        if(nodeName.equals("friendlyName")){
                            Log.e("xml node",nodeName);
                            Log.e("xml text", parser.nextText());
                        }
                        break;
                    default:
                        break;
                }

                eventType=parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
