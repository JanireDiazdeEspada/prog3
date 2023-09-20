
public class CocheJuego extends Coche{
	private JLabelCoche cocheLabel;
	
	public CocheJuego() {
        cocheLabel = new JLabelCoche();
    }
	
    public void setPosX(double posX) {
        super.setPosX(posX);
        cocheLabel.setLocation((int) posX, (int) getPosY());
    }

    public void setPosY(double posY) {
        super.setPosY(posY);
        cocheLabel.setLocation((int) getPosX(), (int) posY);
    }

    public JLabelCoche getCocheLabel() {
        return cocheLabel;
    }
  
    
    @Override
    public void setMiDireccionActual(double direccion) {
    	super.setMiDireccionActual(direccion);
        this.cocheLabel.setGiro(this.miDireccionActual);
        }
}


