package es.upm.dit.gsi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 
 * 
 */
public class MessengerService extends Service {
    
    NotificationManager mNM;
    
    
    int mValue = 0;
    
   
    

    static final int MSG_HELLOER = 1;
    static final int MSG_HELLO = 2;
    static final int MSG_DISPLAY = 3;

    Messenger mActivity=null;
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                
                case MSG_HELLOER:
                	mActivity=msg.replyTo;
                	Message msgi = Message.obtain(null,
                            MessengerService.MSG_HELLO);
                    
					try {
						mActivity.send(msgi);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	break;
                
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Messenger incoming for service
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
    	
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        showNotification("Aplicación","Servicio creado");
        
        Toast.makeText(getBaseContext(),"Service connected too!",Toast.LENGTH_SHORT).show();

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
            	
            	Message msgi = Message.obtain(null,
                        MessengerService.MSG_DISPLAY,data);
                
				try {
					mActivity.send(msgi);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				showNotification("SocketIO","Has recibido un mensaje");
				
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
           
            
			
        });

        // This line is cached until the connection is establisched.
        socket.send("Hello Server!");
    
    } catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		Log.v("SocketIO","Error");
	}
    }
    
    
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(1);

        // Tell the user we stopped.
        Toast.makeText(this, "Servicio remoto parado", Toast.LENGTH_SHORT).show();
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String titleText, String contentText) {
        
        Notification notification = new Notification(R.drawable.icon, titleText,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MessengerService.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, titleText,contentText, contentIntent);

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(1, notification);
    }
}