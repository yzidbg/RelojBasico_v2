import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;

public class Reloj extends JApplet implements Runnable {

    private int ancho = 300;
    private int alto = 300;
    private int centroX = ancho / 2;
    private int centroY = alto / 2;
    //variables de la hora
    private Calendar cal = Calendar.getInstance();
    private int hora = cal.get(Calendar.HOUR);
    private int minutos = cal.get(Calendar.MINUTE);
    private int segundos = cal.get(Calendar.SECOND);
    //radios de las agujas
    private int radioSegundos = 70;
    private int radioMinutos = 60;
    private int radioHoras = 50;
    //variables para la posición de las agujas
    private int segundosX;
    private int segundosY;
    private int minutosX;
    private int minutosY;
    private int horaX;
    private int horaY;
    //pinceles para las agujas y el círculo
    private BasicStroke strokeSegundos = new BasicStroke(1.3f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
    private BasicStroke strokeMinutos = new BasicStroke(2.5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
    private BasicStroke strokeHoras = new BasicStroke(5.5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
    private BasicStroke strokeCirculo = new BasicStroke(3.5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
    //doble buffer
    private Color colorNaranja = new Color(220,128,0);
    private BufferedImage bi;
    private Graphics2D gbi;

    @Override
    public void init() {
        this.setBounds(0, 0, ancho, alto);
        bi = (BufferedImage) createImage(getWidth(), getHeight());
        gbi = (Graphics2D) bi.getGraphics();
    }

    @Override
    public void start(){
        Thread hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //Antialias
        gbi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gbi.setColor(Color.white);
        gbi.fillRect(0, 0, 300, 300);
        pintaReloj(gbi);
        //pintar buffer
        g2.drawImage(bi, 0, 0, this);
    }

    public void run() {

        while (true) {
            try {
                Thread.sleep(100);
                updateReloj();
                repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void pintaReloj(Graphics2D gbi) {
        // dibujar círculo
        gbi.setColor(Color.lightGray);
        gbi.fillOval(centroX - 80, centroY - 80, 160, 160);
        gbi.setStroke(strokeCirculo);
        gbi.setColor(Color.darkGray);
        gbi.drawOval(centroX - 80, centroY - 80, 160, 160);
        //dibujar números en el reloj
        for (int i = 1; i < 12; i++) {
            gbi.drawString(String.valueOf(i), (int) (centroX + 70 * Math.cos(Math.toRadians(30 * i) - Math.PI / 2)-2),
                    (int) (centroY + 70 * Math.sin(Math.toRadians(30 * i) - Math.PI / 2)) + 4);

        }
         gbi.drawString("12", (int) (centroX + 70 * Math.cos(Math.toRadians(30 * 12) - Math.PI / 2)-7),
                    (int) (centroY + 70 * Math.sin(Math.toRadians(30 * 12) - Math.PI / 2)) + 4);

        //pintar agujas
        gbi.setColor(colorNaranja);
        gbi.setStroke(strokeSegundos);
        gbi.drawLine(centroX, centroY, centroX + segundosX, centroY + segundosY);
        gbi.setStroke(strokeMinutos);
        gbi.drawLine(centroX, centroY, centroX + minutosX, centroY + minutosY);
        gbi.setStroke(strokeHoras);
        gbi.drawLine(centroX, centroY, centroX + horaX, centroY + horaY);
        gbi.setColor(Color.darkGray);
        gbi.fillOval(centroX - 4, centroY - 4, 8, 8);
    }

    private void updateReloj() {
        //Actualizar variables del reloj
        cal = Calendar.getInstance();
        hora = cal.get(Calendar.HOUR);
        minutos = cal.get(Calendar.MINUTE);
        segundos = cal.get(Calendar.SECOND);
        //Actualizar posición de las agujas
        segundosX = (int) (radioSegundos * Math.cos(Math.toRadians(6 * segundos) - Math.PI / 2));
        segundosY = (int) (radioSegundos * Math.sin(Math.toRadians(6 * segundos) - Math.PI / 2));
        minutosX = (int) (radioMinutos * Math.cos(Math.toRadians(6 * minutos) - Math.PI / 2));
        minutosY = (int) (radioMinutos * Math.sin(Math.toRadians(6 * minutos) - Math.PI / 2));
        horaX = (int) (radioHoras * Math.cos(Math.toRadians(30 * hora) - Math.PI / 2));
        horaY = (int) (radioHoras * Math.sin(Math.toRadians(30 * hora) - Math.PI / 2));
    }
}
