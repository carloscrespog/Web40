package es.upm.dit.gsi;


import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Socketer extends Service {

	private final IBinder mBinder = new LocalBinder();
	private Context context;
	private NotificationManager mNM;

	@Override
	public IBinder onBind(Intent intent) {
		

		return mBinder;
	}
	
	public class LocalBinder extends Binder {
	      Socketer getService() {
	          return Socketer.this;
	      }
	 }
	@Override
	public void onCreate(){
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		this.context=getApplicationContext();
		Toast.makeText(this.context,"Service connected too!",Toast.LENGTH_SHORT).show();

		SocketIO socket;
        
		try {
			socket = new SocketIO("http://192.168.1.110:1337");
		
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
            		Log.v("SocketIO","Server said:" + json.toString(2));

                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
            	Log.v("SocketIO","Server said: " + data);
            	Context context = getApplicationContext();
    					Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
    							.show();
    				
            }

            @Override
            public void onError(SocketIOException socketIOException) {
            	Log.v("SocketIO","an Error occured:" + socketIOException.getStackTrace());
            	for(StackTraceElement ste : socketIOException.getStackTrace()){
            		Log.v("SocketIO",ste.toString());
            	}
                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {
            	Log.v("SocketIO","Connection terminated.");
            }

            @Override
            public void onConnect() {
            	
            	Log.v("SocketIO","Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
            	
            	
            	Log.v("SocketIO","Server triggered event '" + event + "'");
            	
            	

            }
           
            private void runception(){
            	//SocketThread st=new SocketThread(context);
        		final Thread t=new LooperThread(context);
        		t.start();
            }
			
        });

        // This line is cached until the connection is establisched.
        socket.send("Hello Server!");
    
    } catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		Log.v("SocketIO","Error");
	}

	}
	private void notifier(String text){
		String titleText="Notificación!";
		String contentText=text;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(R.drawable.icon, titleText, when);
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this, Socketer.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, titleText, contentText, contentIntent);
		mNM.notify(1, notification);

	}

}
class SocketThread implements Runnable{

	Context context;
	public SocketThread(Context context){
		this.context=context;
	}
	public void run(){

		Toast.makeText(this.context,"Hello!",Toast.LENGTH_SHORT).show();
		}

}
class LooperThread extends Thread {
    public Handler mHandler;
    static Context sContext;
	public LooperThread(Context context){
		this.sContext=context;
	}
    public void run() {
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
        		Toast.makeText(sContext,"Hello!",Toast.LENGTH_SHORT).show();
        		
            }
        };

        Looper.loop();
    }
}