package es.upm.dit.gsi;





import java.util.ArrayList;
import java.util.List;








import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class Web40Activity {
   

    public static class Binding extends Activity {
        
        Messenger mService = null;
        /** Flag indicating whether we have called bind on the service. */
        boolean mIsBound;
        
        private static CustomArrayAdapter mConversationArrayAdapter;
    	private ListView mConversationView;
    	private EditText mOutEditText;
    	private static Button mSend;
        
        
        /**
         * Handler of incoming messages from service.
         */
        class IncomingHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    
                    case MessengerService.MSG_HELLO:
                    	Toast.makeText(Binding.this, "Helloo!!",Toast.LENGTH_SHORT).show();
                    	break;
                    case MessengerService.MSG_DISPLAY:
                    	
                    	//Toast.makeText(Binding.this, (CharSequence) msg.obj,Toast.LENGTH_SHORT).show();
                    	reply((String) msg.obj);
                    	break;
                    default:
                        super.handleMessage(msg);
                }
            }
        }

        /**
         * Target we publish for clients to send messages to IncomingHandler.
         */
        final Messenger mMessenger = new Messenger(new IncomingHandler());

        /**
         * Class for interacting with the main interface of the service.
         */
        private ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className,
                    IBinder service) {
                // This is called when the connection with the service has been
                // established, giving us the service object we can use to
                // interact with the service.  We are communicating with our
                // service through an IDL interface, so get a client-side
                // representation of that from the raw service object.
                mService = new Messenger(service);
                

                // We want to monitor the service for as long as we are
                // connected to it.
                try {
                    Message msg = Message.obtain(null,
                            MessengerService.MSG_HELLOER);
                    msg.replyTo = mMessenger;
                    mService.send(msg);

                    // Give it some value as an example.
                    
                } catch (RemoteException e) {
                    // In this case the service has crashed before we could even
                    // do anything with it; we can count on soon being
                    // disconnected (and then reconnected if it can be restarted)
                    // so there is no need to do anything here.
                }

                // As part of the sample, tell the user what happened.
                Toast.makeText(Binding.this, "conectado a servicio remoto",
                        Toast.LENGTH_SHORT).show();
            }

            public void onServiceDisconnected(ComponentName className) {
                // This is called when the connection with the service has been
                // unexpectedly disconnected -- that is, its process crashed.
                mService = null;
              

                // As part of the sample, tell the user what happened.
                Toast.makeText(Binding.this, "Desconectado del servicio remoto",
                        Toast.LENGTH_SHORT).show();
            }
        };

        void doBindService() {
            // Establish a connection with the service.  We use an explicit
            // class name because there is no reason to be able to let other
            // applications replace our component.
            bindService(new Intent(Binding.this, 
                    MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
            
        }

        void doUnbindService() {
            if (mIsBound) {
                // If we have received the service, and hence registered with
                // it, then now is the time to unregister.
                

                // Detach our existing connection.
                unbindService(mConnection);
                mIsBound = false;
                
            }
        }

        /**
         * Standard initialization of this activity.  Set up the UI, then wait
         * for the user to poke it before doing anything.
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.main);
            doBindService();
            
            mConversationArrayAdapter = new CustomArrayAdapter(this, R.layout.bubble);	
            mConversationView = (ListView) this.findViewById(R.id.conversacion);
    		// Set the ListView adapter
    		mConversationView.setAdapter(mConversationArrayAdapter);
    		
    		mConversationView.setOnItemClickListener(new OnItemClickListener() {
  	          public void onItemClick(AdapterView<?> parent, View view,
  	              int position, long id) {
  	            
  	            // Do something when clicked
  	          }
  	        });
  		//Text
    		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
  	        mOutEditText.setInputType(EditorInfo.IME_ACTION_DONE);
  	        mOutEditText.setOnEditorActionListener(new OnEditorActionListener() {
  		        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { //not used
  		            if (actionId == EditorInfo.IME_ACTION_DONE) {
  		            	InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
  		            	in.hideSoftInputFromWindow(mOutEditText.getApplicationWindowToken(), 0); 
  	            		String message = mOutEditText.getText().toString();
  	            			//Do something when press send
  	            			sendMessage(message);
  	            			sillyReply();
  	            			mOutEditText.setText(null); 
  	            			
  		            }
  		            return false;
  		        }	
  	        });
  	    //Send button
	        mSend = (Button) findViewById(R.id.button_send);
	        mSend.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	
	            	InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            	in.hideSoftInputFromWindow(mOutEditText.getApplicationWindowToken(), 0); 
	            	String message = mOutEditText.getText().toString();
	            	sendMessage(message);
	            	
	            		mOutEditText.setText(""); 
	            			
	            }
	            
	        });
  	        sillyReply();
  	      
        }
        
        private void sillyReply(){
    		
    		mConversationArrayAdapter.add(new Mensajes("Hello theeeere!!", null, "bot"));
    		
    	}
        
        private void reply(String message){
        	mConversationArrayAdapter.add(new Mensajes(message, null, "bot"));
        }
        
        private void sendMessage(String message){
        	mConversationArrayAdapter.add(new Mensajes(message,"user",null));

        }
        @Override
        protected void onDestroy(){
        	doUnbindService();
        }
        
    


		public class CustomArrayAdapter extends ArrayAdapter<Mensajes> {
			
			private Context context;
			private ImageView userIcon;
			private ImageView botIcon;
			private TextView texto;
			private LinearLayout linear;
			private LinearLayout text;
			private List<Mensajes> conversacion = new ArrayList<Mensajes>();
		
			/**
			 * Construye un CustomArrayAdapter a partir de un contexto y de la direccion de un Layout.
			 * 
			 * @param context contexto en el que se crear el CustomArrayAdapter.
			 * @param textViewResourceId direccion del Layout que se desea introduccir.
			 */
			public CustomArrayAdapter(Context context, int textViewResourceId) {
				super(context, textViewResourceId);
				this.context = context;
			}    	
			
			/**
			 * Añade un mensaje a la lista de mensajes que se muestra por pantalla.
			 * 
			 * @param mensaje Mensaje que se desea añadir.
			 */
			public void add(Mensajes mensaje){
				conversacion.add(mensaje);
				this.notifyDataSetChanged();
		
		
			}
			public void clean(){
				conversacion.clear();
				this.notifyDataSetChanged();
		
			}
			
			public int getNumberMensaje(Mensajes mensaje){    		
				return conversacion.indexOf(mensaje);
				
			}
			
			/**
			 * Devuelve el numero de mensajes que contiene la lista.
			 * 
			 * @return int longitud de la lista.
			 */
			public int getCount() {
				return this.conversacion.size();
			}
		
			/**
			 * Obtiene un elemento Mensajes de la lista.
			 * 
			 * @param index posicion de la que desea obtener el mensaje
			 * @return Mensajes mensaje que a partir de index se deseaba obtener.
			 */
			public Mensajes getItem(int index) {
				return this.conversacion.get(index);
			}
		
			/**
			 * Obtiene un View de la lista, para posteriormente ser mostrado por pantalla.
			 * 
			 * @param position elemento del cual se desea obtener el View.
			 * @param convertView ekemento View de la lista(genérico).
			 * @param parent 
			 * @return View a partir de los paramentros devuelve View para ser mostrado por pantalla del elemento que se desea obtener.
			 */
			public View getView(int position, View convertView, ViewGroup parent) {
		
				View row = convertView;
				
				if(row==null){
					LayoutInflater inflater=getLayoutInflater();
					row=inflater.inflate(R.layout.bubble, parent, false);
				}
				// Get item
				Mensajes mensajes = getItem(position);
				
				// Get reference to ImageView
				//userIcon = (ImageView) row.findViewById(R.id.user_icon);
				
				// Get reference to TextView 
				texto = (TextView) row.findViewById(R.id.bubbleText);
				
				// Get reference to ImageView
				//botIcon = (ImageView) row.findViewById(R.id.bot_icon);
				
				//Get reference to LinearLayout
				linear = (LinearLayout) row.findViewById(R.id.linear_layout);
				text = (LinearLayout) row.findViewById(R.id.linearText);
				
				
				//Set menssage name
				texto.setText(mensajes.toString());
				texto.setTextColor(Color.BLACK);
				   		
					
				// Set icon usign the position
				if(mensajes.user() == 1){	
					//userIcon.setImageResource(R.drawable.user);
					//botIcon.setImageBitmap(null);
					linear.setBackgroundColor(Color.TRANSPARENT);
					linear.setGravity(Gravity.LEFT);
					text.setBackgroundResource(R.drawable.bocadillo_user2);
				}
				if(mensajes.bot() == 1){
					//botIcon.setImageResource(R.drawable.icon);
					//userIcon.setImageBitmap(null);
					linear.setBackgroundColor(Color.TRANSPARENT);
					linear.setGravity(Gravity.RIGHT);
					text.setBackgroundResource(R.drawable.bocadillo_bot2);
				}
				return row;  
			}
		
		   
		
		}



    }

}