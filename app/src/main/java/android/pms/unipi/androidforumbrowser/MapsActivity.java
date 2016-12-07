package android.pms.unipi.androidforumbrowser;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.pms.unipi.androidforumbrowser.MainActivity.MAPS_ACTIVITY;
import static android.pms.unipi.androidforumbrowser.MainActivity.mSharedPrefs;
import static android.pms.unipi.androidforumbrowser.MainActivity.serverUrl;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
    public static GoogleMap mMap;
    double longitude;
    double latitude;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        url = serverUrl + "locations.php";
        //String timeStampString = mTimeStamp.toString();
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        try
        {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
        }
        catch(SecurityException e) {
            e.printStackTrace();}

        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //mMap.addMarker(new MarkerOptions().position(loc).title(mSharedPrefs.getString("Username",null)));
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            }
        };
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.send_location:
                Long mTimeStamp= System.currentTimeMillis()/1000;;
                new JsonTaskPost().execute(url,
                        mSharedPrefs.getString("Username",null),
                        mTimeStamp.toString(),
                        MAPS_ACTIVITY,
                        String.valueOf(longitude),
                        String.valueOf(latitude));
                return true;
            case R.id.search_users:
                String url = serverUrl + "get_user_locations.php";
                new JsonTask().execute(url,MAPS_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void addMapPin(String input,String username,GoogleMap map)
    {
        String[] test = input.split("\\s+");
        String marker;
        double longi,lati;

        //3 values,username - longitude - latitude
        //if username is active username pin in not created
        for (int i = 0; i < test.length/3; i++)
        {
            if(!test[i*2+i].toString().matches(username))
            {
                marker = test[i*2+i];//username
                longi = Double.parseDouble(test[(i*2)+1+i]);
                lati= Double.parseDouble(test[(i*2)+2+i]);
                LatLng loc = new LatLng(lati, longi);
                map.addMarker(new MarkerOptions().position(loc).title(marker));
            }
        }
    }

}
