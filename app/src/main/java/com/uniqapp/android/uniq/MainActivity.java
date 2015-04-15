package com.uniqapp.android.uniq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.melnykov.fab.FloatingActionButton;
import com.uniqapp.android.uniq.CityXMLParser.Zone;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private OpenXmlTask openXmlTask;
    private MapFragment mapFragment;
    private List<Zone> cityZones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = getActionBarToolbar();
        String city = "krakow";
        openXmlTask = new OpenXmlTask();
        openXmlTask.execute(city);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getView().setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), ZoneCheckerService.class));
                moveTaskToBack(true);
            }
        });
        if (openXmlTask.getStatus() == AsyncTask.Status.FINISHED) {
            Log.d("Test Watkow", "TEST");
            mapFragment.getMapAsync(this);
        }
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

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng krakow = new LatLng(50.06465, 19.94498);


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(krakow, 13));

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(50.06465, 19.94498)).radius(1000).strokeWidth(1).fillColor(R.color.amber_A200);

        Circle circle = map.addCircle(circleOptions);
        // In meters
    }

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
            this.dialog.setMessage("Czekaj");
            this.dialog.show();
        }

        @Override
        protected List<Zone> doInBackground(String... passing) {
            try {
                zones = loadXml("krakow");
            }
            catch (IOException e) {
                Log.d(TAG, getResources().getString(R.string.io_error));
            }
            catch (XmlPullParserException e) {
                Log.d(TAG, getResources().getString(R.string.xml_error));

            }
            finally {
                return zones;
            }
        }
        @Override
        protected void onPostExecute(List<Zone> zones) {
            dialog.dismiss();
            updateCityZones(zones);
         //   mapFragment.getView().setVisibility(View.VISIBLE);
            //*setContentView(R.layout.activity_main);*/
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

}
