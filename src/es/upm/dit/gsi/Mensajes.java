package es.upm.dit.gsi;

public class Mensajes{
	public String imageUser;
	public String imageBot;
	public String mensaje;
	public String estado;

	/**
	 * Constructor de la Clase Mensajes, crea un elemento mensaje sin nada.
	 */
	public Mensajes(){
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor de la Clase Mensajes, crea un elemento mensaje especificando sus caracteristicas.
	 * 
	 * @param texto mensaje que se desea poner
	 * @param imagenUser si es "user" pone la imagen del usuario en caso contrario nada.
	 * @param imagenBot si es "bot" pone la imagen del bot en caso contrario nada.
	 */
	public Mensajes(String texto, String imagenUser, String imagenBot){
			this.mensaje = texto;
			this.imageUser = imagenUser;
			this.imageBot= imagenBot;
			this.estado = "";
	}
	
	public Mensajes(String texto, String imagenUser, String imagenBot, String estado){
		this.mensaje = texto;
		this.imageUser = imagenUser;
		this.imageBot= imagenBot;
		this.estado = estado;
	}

	/**
	 * Metodo toString de la clase Mensaje.
	 * 
	 * @return String cadena que devuelve el texto que contiene el mensaje.
	 */
	@Override
	public String toString(){
			return this.mensaje;
	}
	
	/**
	 * Conversor para imagen usuario.
	 * 
	 * @return int 
	 */
	public int user(){
		try{
			if(imageUser.equals("user")){
				return 1;
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * Conversor para imagen bot.
	 * 
	 * @return int 
	 */
	public int bot(){
		try{
			if(imageBot.equals("bot")){
				return 1;
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * Obtener el estado para poner una imagen u otra
	 * 
	 * @return int 0 estado generico, 1 estado feliz, 2 estado malhumorado, 3 normal
	 */
	public int estado(){
		if(estado.equals("happy")){
			return 1;
		}
		if(estado.equals("sad")){
			return 2;
		}
		if(estado.equals("blinky")){
			return 3;
		}
		return 0;
	}
}