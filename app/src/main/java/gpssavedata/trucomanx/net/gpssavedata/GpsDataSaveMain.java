package gpssavedata.trucomanx.net.gpssavedata;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.Button;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import android.util.Log;
import android.widget.Toast;

import net.sourceforge.pdsplibja.pdsextras.*;

public class GpsDataSaveMain extends ActionBarActivity {
    private EditText TextOutputFileName;
    private TextView textCompleteFilename;
    private ToggleButton toggleInitProgram;

    private PdsSaveDataGPS FileOutputGPS;

    static final String TAG = "GPS_SIMPLE_SAMPLE";

    private LocationManager lm;

    private SeekBar seekbar1; //Your SeekBar
    private TextView textViewSeekBar;
    int value;        //The SeekBar value output

    private TextView textDataAccuracy;
    private TextView textDataAltitude;
    private TextView textDataLongitude;
    private TextView textDataLatitude;
    private TextView textDataTime;
    long timezero=-1;

    String Filename;

    LocationListener locationListener ;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_data_save_main);



        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        toggleInitProgram    = (ToggleButton)findViewById(R.id.toggleInitProgram);

        TextOutputFileName   = (EditText)    findViewById(R.id.TextOutputFileName);
        textCompleteFilename = (TextView)    findViewById(R.id.textCompleteFilename);

        textDataAccuracy  = (TextView)findViewById(R.id.textDataAccuracy);
        textDataLatitude  = (TextView)findViewById(R.id.textDataLatitude);
        textDataAltitude  = (TextView)findViewById(R.id.textDataAltitude);
        textDataLongitude = (TextView)findViewById(R.id.textDataLongitude);
        textDataTime      = (TextView)findViewById(R.id.textDataTime);

        seekbar1        = (SeekBar) findViewById(R.id.seekbar1);
        textViewSeekBar = (TextView)findViewById(R.id.textViewSeekBar);
        // Initialize the textview with '0'.
        textViewSeekBar.setText("Seconds: " +seekbar1.getProgress() + "/" + seekbar1.getMax());

        seekbar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewSeekBar.setText("Seconds: " + progress + "/" + seekBar.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });


        toggleInitProgram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (toggleInitProgram.isChecked()) {
                    Filename = TextOutputFileName.getText().toString();

                    FileOutputGPS = new PdsSaveDataGPS(getApplicationContext(), Filename);

                    textCompleteFilename.setText(FileOutputGPS.GetFileName());

                    value=seekbar1.getProgress();

                    // provider: The name of the provider with which you register
                    // minTime: The minimum time interval for notifications, in
                    // milliseconds.
                    // minDistance: The minimum distance interval for notifications, in
                    // meters.
                    // listener: An object whose onLocationChanged() method will be called
                    // for each location update.
                    //
                    // add location listener and request updates every 0 meters or 0 second,
                    // when it happens will be called onLocationChanged method
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, value*1000, 1, locationListener);


                    seekbar1.setEnabled(false);
                }
                else {
                    seekbar1.setEnabled(true);
                    FileOutputGPS.close();
                    // turn all off
                    Toast.makeText(getApplicationContext(), "Gps stopped", Toast.LENGTH_SHORT).show();
                    // turn off to save battery
                    lm.removeUpdates(locationListener);
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
        Toast.makeText(this, "app resumed", Toast.LENGTH_SHORT).show();


        super.onResume();
    }

    /**
     * Is where you deal with the user leaving your activity and usually save
     * the data, remember, Android architecture can kill your Activity if you
     * aren't using it.
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        // turn all off
        Toast.makeText(this, "app paused", Toast.LENGTH_SHORT).show();

        super.onPause();
    }

    /**
     * The final call, it's finish your application
     *
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        // save some information before exit
        Toast.makeText(this, "activity killed", Toast.LENGTH_SHORT).show();
        // turn off to save battery
        lm.removeUpdates(locationListener);
        super.finish();
    }

    /**
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
        // save some information before exit
        Toast.makeText(this, "app stoped", Toast.LENGTH_SHORT).show();
        super.onStop();
    }



    private final class MyLocationListener implements LocationListener {

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
            if(timezero<0)  timezero=time;

            String s = longitude + "\t" + latitude + "\t" + altitude + "\t" + ((time-timezero)/1000.0) + "\t" + accurancy+"\n";
            FileOutputGPS.Printf(1,s);
            textDataAccuracy.setText(accurancy + " ");
            textDataAltitude.setText(altitude + " ");
            textDataLatitude.setText(latitude + " ");
            textDataLongitude.setText(longitude + " ");
            textDataTime.setText(((time-timezero)/1000.0)+" ");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }

        /**
         * This is called if/when the GPS is disabled in settings.
         *
         * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
         */
        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider is disable", Toast.LENGTH_SHORT).show();
        }

        /**
         * This is called if/when the GPS is enabled in settings.
         *
         * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
         */
        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Provider is enable", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Status Changed: Out of Service", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.v(TAG, "Status Changed: Temporarily Unavailable");
                    Toast.makeText(getApplicationContext(), "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.AVAILABLE:
                    Log.v(TAG, "Status Changed: Available");
                    Toast.makeText(getApplicationContext(), "Status Changed: Available", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Status Changed: I dont know... throw an Exception", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Status Changed: I dont know... throw an Exception");
                    // FIXME throw an exception
            }
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
