
public class Coche {
	//Atributos
	private double miVelocidad; // Velocidad en pixels/segundo
	protected double miDireccionActual; // Dirección en la que estoy mirando en grados (de 0 a 360)
	protected double posX; // Posición en X (horizontal)
	protected double posY; // Posición en Y (vertical)
	private String piloto; // Nombre de piloto
	
	//Contructor vacio
	public Coche() {
		super();
		this.miVelocidad = 0;
		this.miDireccionActual = 0;
		this.posX = 0;
		this.posY = 0;
		this.piloto = "";
	}
	
	//Getters & Setters
	public double getMiVelocidad() {
		return miVelocidad;
	}

	public void setMiVelocidad(double miVelocidad) {
		this.miVelocidad = miVelocidad;
	}

	public double getMiDireccionActual() {
		return miDireccionActual;
	}

	public void setMiDireccionActual(double miDireccionActual) {
		if (miDireccionActual < 0.0) {
			miDireccionActual += 360.0;
        }
        if (miDireccionActual > 360.0) {
        	miDireccionActual -= 360.0;
        }
        this.miDireccionActual = miDireccionActual;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}
	
    public void setPosicion(double posX, double posY) {
        this.setPosX(posX);
        this.setPosY(posY);
    }
    

	public String getPiloto() {
		return piloto;
	}

	public void setPiloto(String piloto) {
		this.piloto = piloto;
	}

	//METODOS
    public void acelera(double aceleracion) {
        this.miVelocidad += aceleracion;
    }
    
    public void gira(double giro) {
        this.setMiDireccionActual(this.miDireccionActual + giro);
    }
    
    public void mueve(double tiempoDeMovimiento) {
        this.setPosX(this.posX + this.miVelocidad * Math.cos(this.miDireccionActual / 180.0 * 3.14) * tiempoDeMovimiento);
        this.setPosY(this.posY + this.miVelocidad * -Math.sin(this.miDireccionActual / 180.0 * 3.14) * tiempoDeMovimiento);
    }

	@Override
	public String toString() {
		return "Coche [miVelocidad=" + miVelocidad + ", miDireccionActual=" + miDireccionActual + ", posX=" + posX
				+ ", posY=" + posY + ", piloto=" + piloto + "]";
	}
	
	
	
}
