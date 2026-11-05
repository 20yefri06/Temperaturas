package vista;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class PanelBarras extends JPanel {

    private static final Color[] PALETA = {
            new Color(0x2E86AB), new Color(0xF18F01), new Color(0x06A77D),
            new Color(0xC73E1D), new Color(0x6A4C93), new Color(0x3B8EA5),
            new Color(0xE07A5F), new Color(0x81B29A)
    };
    private static final Color GRIS_TEXTO = new Color(0x6B7785);
    private static final Color GRIS_LINEA = new Color(0xE3E7EB);

    private Map<String, Double> datos = new TreeMap<>();

    public PanelBarras() {
        setBackground(Color.WHITE);
    }

    public void setDatos(Map<String, Double> datos) {
        this.datos = datos;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final int w = getWidth();
        final int h = getHeight();

        if (datos.isEmpty()) {
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            g2.setColor(GRIS_TEXTO);
            g2.drawString("Selecciona un rango de fechas y presiona \"Graficar promedios\"", 30, 40);
            return;
        }

        final int izq = 60;
        final int der = 30;
        final int sup = 50;
        final int inf = 45;
        final int areaW = w - izq - der;
        final int areaH = h - sup - inf;

        final double maxValor = datos.values().stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(1);
        final double techo = Math.max(5, Math.ceil(maxValor / 5) * 5);

      
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2.setColor(new Color(0x1F2D3D));
        g2.drawString("Promedio de temperatura por ciudad (°C)", izq, 28);

        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        IntStream.rangeClosed(0, 5).forEach(i -> {
            int y = h - inf - (int) (areaH * i / 5.0);
            g2.setColor(GRIS_LINEA);
            g2.drawLine(izq, y, w - der, y);
            g2.setColor(GRIS_TEXTO);
            g2.drawString(String.format("%.0f", techo * i / 5), izq - 32, y + 4);
        });

     
        final List<Map.Entry<String, Double>> entradas = new ArrayList<>(datos.entrySet());
        final int n = entradas.size();
        final int slot = areaW / n;
        final int anchoBarra = Math.min((int) (slot * 0.6), 90);
        final FontMetrics fm = g2.getFontMetrics();

        IntStream.range(0, n).forEach(i -> {
            Map.Entry<String, Double> e = entradas.get(i);
            Color color = PALETA[i % PALETA.length];

            int alto = (int) (e.getValue() / techo * areaH);
            int x = izq + i * slot + (slot - anchoBarra) / 2;
            int y = h - inf - alto;

            g2.setPaint(new GradientPaint(x, y, color.brighter(), x, y + alto, color.darker()));
            g2.fill(new RoundRectangle2D.Double(x, y, anchoBarra, alto, 12, 12));

            String valor = String.format("%.2f °C", e.getValue());
            g2.setColor(new Color(0x1F2D3D));
            g2.drawString(valor, x + (anchoBarra - fm.stringWidth(valor)) / 2, y - 8);

            g2.setColor(GRIS_TEXTO);
            g2.drawString(e.getKey(), x + (anchoBarra - fm.stringWidth(e.getKey())) / 2, h - inf + 20);
        });

        g2.setColor(new Color(0x9AA5B1));
        g2.drawLine(izq, h - inf, w - der, h - inf);
    }
}