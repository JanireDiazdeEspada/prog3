import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class VentanaJuego extends JFrame {
	private JPanel panelPrincipal;
	private JPanel panelBotones;
	private JButton boton1;
	private JButton boton2;
	private JButton boton3;
	private JButton boton4;
	
	protected MiHilo hilo;
	
	
	private CocheJuego miCoche;
	
	//inicializar la ventana
	public VentanaJuego() {
		//Componentes de la ventana
		panelPrincipal = new JPanel();
		panelPrincipal.setBackground(Color.WHITE);
		panelPrincipal.setLayout(null);
		
		panelBotones = new JPanel(new GridLayout(1,4)); // 1 fila y 4 columnas
		boton1 = new JButton("Acelera");
		boton2 = new JButton("Frena");
		boton3 = new JButton("Gira izq.");
		boton4 = new JButton("Gira dcha.");
		//añadir los botones al panel de botones
		panelBotones.add(boton1);
		panelBotones.add(boton2);
		panelBotones.add(boton3);
		panelBotones.add(boton4);
		
		
		miCoche = new CocheJuego();
		
		//Acciones de los botones
		boton1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                if (VentanaJuego.this.miCoche.getMiVelocidad() == 0.0) {
                    VentanaJuego.this.miCoche.acelera(5.0);
                }
                else {
                    VentanaJuego.this.miCoche.acelera(5.0);
                }
                System.out.println("Velocidad de coche: " + VentanaJuego.this.miCoche.getMiVelocidad());
			}
		});
		
		boton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        VentanaJuego.this.miCoche.acelera(-5.0);
                System.out.println("Velocidad de coche: " + VentanaJuego.this.miCoche.getMiVelocidad());
			}
		});
		
		boton3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 VentanaJuego.this.miCoche.gira(10.0);
				 System.out.println("Direccion del coche: " + VentanaJuego.this.miCoche.getMiDireccionActual());
				
			}
		});
		
		boton4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaJuego.this.miCoche.gira(-10.0);
                System.out.println("Direccion del coche: " + VentanaJuego.this.miCoche.getMiDireccionActual());
		    }
				
		});
		
			
		//Caracteristicas de la ventana
		this.setTitle("Juego");
		this.setSize(600, 400);
		
		this.add(panelPrincipal, BorderLayout.CENTER);
		this.add(panelBotones, BorderLayout.SOUTH);
		
		
		this.setVisible(true); 
		this.setLocationRelativeTo(null); //aparece centrada
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public class MiHilo implements Runnable{
		boolean sigo = true;
		@Override
		public void run() {
	    while (this.sigo) {
	    	VentanaJuego.this.miCoche.mueve(0.5);
	    	if (VentanaJuego.this.miCoche.getPosX() < -50.0 || VentanaJuego.this.miCoche.getPosX() > VentanaJuego.this.panelPrincipal.getWidth() - 50) {
	    		System.out.println("Choca en X");
	    		double direccion = VentanaJuego.this.miCoche.getMiDireccionActual();
	    		direccion = 180.0 - direccion;
	    		if (direccion < 0.0) {
	    			direccion += 360.0;
	    			}
	    		VentanaJuego.this.miCoche.setMiDireccionActual(direccion);
	    		}
	    	if (VentanaJuego.this.miCoche.getPosY() < -50.0 || VentanaJuego.this.miCoche.getPosY() > VentanaJuego.this.panelPrincipal.getHeight() - 50) {
	    		System.out.println("Choca en Y");
	            double direccion = VentanaJuego.this.miCoche.getMiDireccionActual();
	            direccion = 360.0 - direccion;
	            VentanaJuego.this.miCoche.setMiDireccionActual(direccion);
	            }
	    	try {
	    		Thread.sleep(40L);
	    		}
	    	catch (Exception ex) {
	    		
	    	}
	    	}
	    }        
	}
	        
    public void creaCoche(int posX, final int posY) {
        (this.miCoche = new CocheJuego()).setPosicion(posX, posY);
        this.panelPrincipal.add(this.miCoche.getCocheLabel());
    }		

	public static void main(String[] args) {
		VentanaJuego v = new VentanaJuego();
		v.setVisible(true);
		v.creaCoche(150, 100);
		v.miCoche.setPiloto("Janire Diaz");
        v.setVisible(true);
        v.hilo = v.new MiHilo();
        final Thread nuevoHilo = new Thread(v.hilo);
        nuevoHilo.start();
        
        
      
        
	}
}
