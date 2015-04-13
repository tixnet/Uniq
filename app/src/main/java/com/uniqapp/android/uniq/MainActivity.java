package com.uniqapp.android.uniq;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.uniqapp.android.uniq.CityXMLParser.Zone;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String city = "Krakow";
        Toolbar toolbar = getActionBarToolbar();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), ZoneCheckerService.class));
                moveTaskToBack(true);
            }
        });



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

        map.addMarker(new MarkerOptions()
                .title("Kraków")
                .snippet("Test")
                .position(krakow));

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(50.06465, 19.94498)).radius(1000).strokeWidth(1).fillColor(R.color.amber_A200);

        Circle circle = map.addCircle(circleOptions);
        // In meters
    }

    private class OpenXmlTask extends AsyncTask<String,Void, String> {

        @Override
        protected String doInBackground(String... string) {
            try {
                //TODO
                loadXml("test");
                return "test";
            }
            catch (IOException e) {
                Log.d(TAG, getResources().getString(R.string.io_error));
                return getResources().getString(R.string.io_error);
            }
            catch (XmlPullParserException e) {
                Log.d(TAG, getResources().getString(R.string.xml_error));
                return getResources().getString(R.string.xml_error);
            }
        }
        @Override
        protected void onPostExecute(String result) {
            setContentView(R.layout.activity_main);
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
            stream = new FileInputStream("Krakow.xml");
            zones = cityXMLParser.parse(stream);
        }
        catch (IOException e) {
            Log.d(TAG, getResources().getString(R.string.io_error));
        }
        catch (XmlPullParserException e) {
            Log.d(TAG, getResources().getString(R.string.xml_error));
        }
        finally {
            if (stream != null) stream.close();
        }
        return zones;
    }

}
