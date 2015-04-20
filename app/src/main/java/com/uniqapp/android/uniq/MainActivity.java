package com.uniqapp.android.uniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View.OnClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
//import com.melnykov.fab.FloatingActionButton;
import com.uniqapp.android.uniq.CityXMLParser.Zone;
import com.getbase.floatingactionbutton.FloatingActionButton;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private OpenXmlTask openXmlTask;
    private MapFragment mapFragment;
    private List<Zone> cityZones;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = getActionBarToolbar();
        String city = "krakow";

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
/*

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), MiniAppService.class));
                moveTaskToBack(true);
            }
        });
*/



        map = mapFragment.getMap();
        openXmlTask = new OpenXmlTask();
        openXmlTask.execute(city);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    public void onMapReady(GoogleMap map) {
        LatLng krakow = new LatLng(50.06465, 19.94498);


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(krakow, 13));

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(50.06465, 19.94498)).radius(1000).strokeWidth(1).fillColor(R.color.amber_A200);


        // In meters
    }
*/
    private class OpenXmlTask extends AsyncTask<String, Void, List<Zone>> {

        private Activity activity;
        List<Zone> zones = new ArrayList<Zone>();
        private ProgressDialog dialog;

        private OpenXmlTask() {
           // this.activity = activity;
            this.dialog = new ProgressDialog(MainActivity.this);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage(getResources().getString(R.string.loading));
            this.dialog.show();
        }

        @Override
        protected List<Zone> doInBackground(String... passing) {
            try {
                zones = loadXml("krakow");
            }
            catch (IOException e) {
                Log.d(TAG, getResources().getString(R.string.io_error));
                e.getStackTrace();

            }
            catch (XmlPullParserException e) {
                Log.d(TAG, getResources().getString(R.string.xml_error));
                e.getStackTrace();

            }
            finally {
                return zones;
            }
        }
        @Override
        protected void onPostExecute(List<Zone> zones) {
            dialog.dismiss();
            updateCityZones(zones);
//            String coordinatesXML = zones.get(0).coordinates;
//            String[] splitCoordinatesXML = coordinatesXML.split("\\s*;\\s*");


            PolygonOptions polygonOptions = new PolygonOptions();
            for (LatLng coordinates : cityZones.get(0).coordinates) {
                polygonOptions.add(coordinates);
            }
            Polygon polygon = map.addPolygon(polygonOptions.strokeWidth(0).fillColor(getResources().getColor(R.color.zone1)));
            PolygonOptions polygonOptions2 = new PolygonOptions();
            for (LatLng coordinates : cityZones.get(1).coordinates) {
                polygonOptions2.add(coordinates);
            }
            Polygon polygon2 = map.addPolygon(polygonOptions2.strokeWidth(0).fillColor(getResources().getColor(R.color.zone2)));
            PolygonOptions polygonOptions3 = new PolygonOptions();
            for (LatLng coordinates : cityZones.get(2).coordinates) {
                polygonOptions3.add(coordinates);
            }
            Polygon polygon3 = map.addPolygon(polygonOptions3.strokeWidth(0).fillColor(getResources().getColor(R.color.zone3)));

            LatLng krakow = new LatLng(50.06465, 19.94498);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(krakow, 13));
        }
    }


    private List loadXml(String city) throws XmlPullParserException, IOException {
        InputStream stream = null;
        CityXMLParser cityXMLParser = new CityXMLParser();
        List<Zone> zones = null;
        String zoneName = null;
        String coordinates = null;
        String color = null;

        try {
            stream = getResources().openRawResource(R.raw.krakow);
            zones = cityXMLParser.parse(stream);
        }
        finally {
            if (stream != null) stream.close();
        }
        return zones;
    }

    private void updateCityZones(List<Zone> zones) {
        cityZones = new ArrayList<Zone>(zones);
    }

/*    private void stringToLatLong(List<Zone> zones) {
        String[] coordinates;
        String[] latLng;
//        List<LatLng> ;
        double latitude;
        double longitude;

        for (Zone zone : zones ) {
            coordinates = zone.coordinates.split("\\;");
            for(String strLatLng: coordinates) {
                latLng = strLatLng.split(",");
                latitude = Double.parseDouble(latLng[0]);
                latitude = Double.parseDouble(latLng[1]);
            }
        }
    }*/

}
