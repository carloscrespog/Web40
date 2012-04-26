package es.upm.dit.gsi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;



/**
 * 
 * 
 */
public class MessengerService extends Service {
    
    NotificationManager mNM;
    
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    
    int mValue = 0;
    
   
    

    static final int MSG_HELLOER = 1;
    static final int MSG_HELLO = 2;

    Messenger mActivity=null;
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                
                case MSG_HELLOER:
                	Messenger mActivity=msg.replyTo;
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
        
       
       
        showNotification();
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
    private void showNotification() {
        
        Notification notification = new Notification(R.drawable.icon, "Servicio de",
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MessengerService.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "Notificación!",
                       "Servicio iniciado", contentIntent);

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(1, notification);
    }
}