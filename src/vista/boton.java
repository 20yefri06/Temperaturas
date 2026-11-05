package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class boton extends JButton {

    private static final Color BASE = new Color(0x2E86AB);
    private static final Color HOVER = new Color(0x3AA0C9);
    private static final Color PRESIONADO = new Color(0x1F6A8A);

    private boolean encima = false;
    private boolean presionado = false;

    public boton(String texto) {
        super(texto);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(9, 24, 11, 24));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                encima = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encima = false;
                presionado = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                presionado = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                presionado = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = presionado ? PRESIONADO : (encima ? HOVER : BASE);
        int ancho = getWidth();
        int alto = getHeight();

  
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fill(new RoundRectangle2D.Double(1, 3, ancho - 2, alto - 3, 22, 22));

     
        g2.setPaint(new GradientPaint(0, 0, color.brighter(), 0, alto, color));
        g2.fill(new RoundRectangle2D.Double(0, 0, ancho - 1, alto - 3, 22, 22));

        g2.dispose();
        super.paintComponent(g);
    }
}