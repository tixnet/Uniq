package com.uniqapp.android.uniq;

import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

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
        public final List<LatLng> coordinates;
        public final String color;

        protected Zone(String zoneName, List<LatLng> coordinates, String color) {
            this.zoneName = zoneName;
            this.coordinates = coordinates;
            this.color = color;
        }
    }

    private Zone readZone(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "zone");
        String zoneName = null;
        List<LatLng> coordinates = null;
        String color = null;
        while (parser.next()!= XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
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
        String zoneName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "zoneName");
        return zoneName;
    }

    private List<LatLng> readCoordinates(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String coordinates = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        String[] coordinatesArray = coordinates.split(";");

        List<LatLng> coordinatesLatLng = new ArrayList<>();
        String[] latLngs;
        double latitude;
        double longitude;
        for (String latLng : coordinatesArray) {
            latLngs = latLng.split(",");
            latitude = Double.parseDouble(latLngs[1]);
            longitude = Double.parseDouble(latLngs[0]);
            LatLng coordinatesPoint = new LatLng(latitude,longitude);
            coordinatesLatLng.add(coordinatesPoint);
        }
        
        return coordinatesLatLng;
    }

    private String readColor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "color");
        String color = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "color");
        return color;
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
