import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JLabelCoche extends JLabel{
    private double miGiro;
    
    public JLabelCoche() {
        this.miGiro = 1.57;
        try {
            this.setIcon(new ImageIcon(JLabelCoche.class.getResource("coche.png").toURI().toURL()));
        }
        catch (Exception e) {
            System.err.println("Error en carga de recurso: coche.png no encontrado");
            e.printStackTrace();
        }
        this.setSize(100, 100);
    }
    
    public void setGiro(final double gradosGiro) {
        this.miGiro = gradosGiro / 180.0 * 3.14;
        this.miGiro = -this.miGiro;
        this.miGiro += 1.57;
    }
    @Override
    protected void paintComponent(Graphics g) {
        // Obtener la imagen del ImageIcon
        Image img = ((ImageIcon)this.getIcon()).getImage();

        // Castear el objeto Graphics a Graphics2D
        Graphics2D g2 = (Graphics2D) g;

        // Establecer configuraciones de renderizado
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Rotar el gráfico
        g2.rotate(this.miGiro, 50.0, 50.0);

        // Dibujar la imagen
        g2.drawImage(img, 0, 0, 100, 100, null);
    }
    
	
}
