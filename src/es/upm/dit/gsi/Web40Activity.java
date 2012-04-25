package es.upm.dit.gsi;



import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class Web40Activity extends Activity {
	
	
	private Intent intent;
	private ServiceConnection conn;
	private Socketer mSocketer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        intent = new Intent(getBaseContext(), Socketer.class);
        startService(intent);
        setContentView(R.layout.main);
        conn = new ServiceConnection() {
        	@Override
        	public void onServiceConnected(ComponentName className, IBinder service) {
        		mSocketer = ((Socketer.LocalBinder)service).getService();
				Toast.makeText(getBaseContext(),"Connected to service"+mSocketer.toString(),Toast.LENGTH_SHORT).show();

        		
        		 
        	}
        	@Override
        	public void onServiceDisconnected(ComponentName className) {
        		mSocketer = null;
        		stopService(intent);
        		finish();
        	}
        };

        bindService(intent, conn,  BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause() {
        
    }
    @Override
    protected void onStop() {
       
    }
    @Override
    protected void onDestroy() {
    	mSocketer = null;
		stopService(intent);
        super.onDestroy();
        // The activity is about to be destroyed.
    }
}