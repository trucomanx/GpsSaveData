package gpssavedata.trucomanx.net.gpssavedata;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.Button;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import android.util.Log;
import android.widget.Toast;

import net.sourceforge.pdsplibja.pdsextras.*;

public class GpsDataSaveMain extends ActionBarActivity implements LocationListener {
    private EditText TextOutputFileName;
    private TextView textCompleteFilename;
    private ToggleButton toggleInitProgram;

    private PdsSaveDataGPS FileOutputGPS;

    static final String TAG = "GPS_SIMPLE_SAMPLE";

    private LocationManager lm;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    String Filename;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_data_save_main);



        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        toggleInitProgram    = (ToggleButton)findViewById(R.id.toggleInitProgram);

        TextOutputFileName   = (EditText)    findViewById(R.id.TextOutputFileName);
        textCompleteFilename = (TextView)    findViewById(R.id.textCompleteFilename);


        toggleInitProgram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                if( toggleInitProgram.isChecked()) {
                    Filename = TextOutputFileName.getText().toString();
                    FileOutputGPS = new PdsSaveDataGPS(getApplicationContext(), Filename);
                    textCompleteFilename.setText(FileOutputGPS.GetFileName());



                }
                else {
                    FileOutputGPS.close();
                }
            }
        });



    }

    /**
     * onResume is is always called after onStart, even if the app hasn't been
     * paused
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // turn all on
        Log.d(TAG, "Simple_GPS_ExampleActivity::onResume");
        Toast.makeText(this, "app resumed", Toast.LENGTH_SHORT).show();

        // provider: The name of the provider with which you register
        // minTime: The minimum time interval for notifications, in
        // milliseconds.
        // minDistance: The minimum distance interval for notifications, in
        // meters.
        // listener: An object whose onLocationChanged() method will be called
        // for each location update.

        // add location listener and request updates every 0 meters or 0 second,
        // when it happens will be called onLocationChanged method
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);
        super.onResume();
    }

    /**
     * This method is called when the location has changed. The
     * onLocationChanged method will be called as soon as the device locks up
     * with the satellites and then on the intervals you specified in your
     * request updates call.
     *
     * @see android.location.LocationListener#onLocationChanged(android.location.Location)
     */
    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double altitude = location.getAltitude();
        float accurancy = location.getAccuracy();
        long time = location.getTime();

        String s = longitude + "\t" + latitude + "\t" + altitude + "\t" + accurancy + "\t" + time + "\n";
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * This is called if/when the GPS is disabled in settings.
     *
     * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
     */
    @Override
    public void onProviderDisabled(String provider) {
        //
    }

    /**
     * This is called if/when the GPS is enabled in settings.
     *
     * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
     */
    @Override
    public void onProviderEnabled(String provider) {
        //
    }


    /**
     * This is called when the GPS status alters
     *
     * @see android.location.LocationListener#onStatusChanged(java.lang.String,int, android.os.Bundle)
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Simple_GPS_ExampleActivity::onStatusChanged");
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.v(TAG, "Status Changed: Out of Service");
                Toast.makeText(this, "Status Changed: Out of Service", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v(TAG, "Status Changed: Temporarily Unavailable");
                Toast.makeText(this, "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.AVAILABLE:
                Log.v(TAG, "Status Changed: Available");
                Toast.makeText(this, "Status Changed: Available", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Status Changed: I dont know... throw an Exception", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Status Changed: I dont know... throw an Exception");
                // FIXME throw an exception
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gps_data_save_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
