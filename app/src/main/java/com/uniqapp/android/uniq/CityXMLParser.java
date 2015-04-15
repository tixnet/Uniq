package com.uniqapp.android.uniq;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz Gaździak on 2015-04-12.
 */
public class CityXMLParser{

    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readCity(parser);
        } finally {
            in.close();
        }
    }

    private List readCity(XmlPullParser parser) throws XmlPullParserException, IOException {
        List zones = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "city");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("Parser name", parser.getName());
            // Starts by looking for the entry tag
            if (name.equals("zone")) {
                zones.add(readZone(parser));
            } else {
                skip(parser);
            }
        }
        return zones;
    }

    public static class Zone {
        public final String zoneName;
        public final String coordinates;
        public final String color;

        protected Zone(String zoneName, String coordinates, String color) {
            this.zoneName = zoneName;
            this.coordinates = coordinates;
            this.color = color;
        }
    }

    private Zone readZone(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "zone");
        String zoneName = null;
        String coordinates = null;
        String color = null;
        while (parser.next()!= XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("Parser name 2", parser.getName());
            if (name.equals("zoneName")) {
                zoneName = readZoneName(parser);
                Log.d("Zone name", String.format("Zone name " +zoneName));
            }
            else if (name.equals("coordinates")){
                coordinates = readCoordinates(parser);
            }
            else if (name.equals("color")){
                color = readColor(parser);
            }
            else skip(parser);
        }

        return new Zone(zoneName, coordinates, color);
    }

    private String readZoneName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "zoneName");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "zoneName");
        return title;
    }

    private String readCoordinates(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        return title;
    }

    private String readColor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "color");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "color");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }




}
