package vista;

import controlador.TemperaturaController;
import modelo.RegistroTemperatura;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerDateModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VentanaTemperaturas extends JFrame {

    private static final Color AZUL_OSCURO = new Color(0x1F3A5F);
    private static final Color AZUL = new Color(0x2E86AB);
    private static final Color ROJO = new Color(0xC73E1D);
    private static final Color FONDO = new Color(0xF4F6F8);
    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FUENTE_NEGRITA = new Font("Segoe UI", Font.BOLD, 13);

    private final TemperaturaController controlador;

    public VentanaTemperaturas(TemperaturaController controlador) {
        this.controlador = controlador;

        setTitle("Temperaturas por ciudad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(FUENTE_NEGRITA);
        pestanas.addTab("Gráfica", crearPestanaGrafica());
        pestanas.addTab("Extremos por fecha", crearPestanaExtremos());

        add(crearEncabezado(), BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

   
    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Temperaturas por ciudad");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel subtitulo = new JLabel("Promedios por rango de fechas y extremos por día");
        subtitulo.setForeground(new Color(0xBFD7EA));
        subtitulo.setFont(FUENTE);

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        encabezado.add(textos, BorderLayout.WEST);
        return encabezado;
    }

  
    private JPanel crearPestanaGrafica() {
        JSpinner spInicio = selectorFecha(LocalDate.of(2024, 1, 1));
        JSpinner spFin = selectorFecha(LocalDate.of(2024, 1, 10));
        JButton btnGraficar = crearBoton("Graficar promedios");
        PanelBarras grafica = new PanelBarras();
        grafica.setBorder(BorderFactory.createLineBorder(new Color(0xD9DEE3)));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        controles.setBackground(FONDO);
        controles.add(crearEtiqueta("Desde:"));
        controles.add(spInicio);
        controles.add(crearEtiqueta("Hasta:"));
        controles.add(spFin);
        controles.add(btnGraficar);

        btnGraficar.addActionListener(ev -> {
            LocalDate inicio = aLocalDate(spInicio);
            LocalDate fin = aLocalDate(spFin);
            if (inicio.isAfter(fin)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de inicio no puede ser posterior a la fecha de fin.");
                return;
            }
            grafica.setDatos(controlador.promedios(inicio, fin));
        });

        JPanel contenedorGrafica = new JPanel(new BorderLayout());
        contenedorGrafica.setBackground(FONDO);
        contenedorGrafica.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        contenedorGrafica.add(grafica, BorderLayout.CENTER);

        JPanel pestana = new JPanel(new BorderLayout());
        pestana.setBackground(FONDO);
        pestana.add(controles, BorderLayout.NORTH);
        pestana.add(contenedorGrafica, BorderLayout.CENTER);
        return pestana;
    }

   
    private JPanel crearPestanaExtremos() {
        JSpinner spDia = selectorFecha(LocalDate.of(2024, 1, 1));
        JButton btnConsultar = crearBoton("Consultar");
        JLabel lblCalurosa = new JLabel("—");
        JLabel lblFresca = new JLabel("—");

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        controles.setBackground(FONDO);
        controles.add(crearEtiqueta("Fecha:"));
        controles.add(spDia);
        controles.add(btnConsultar);

        JPanel tarjetas = new JPanel(new GridLayout(1, 2, 16, 0));
        tarjetas.setBackground(FONDO);
        tarjetas.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        tarjetas.add(crearTarjeta("Más calurosa(s)", ROJO, lblCalurosa));
        tarjetas.add(crearTarjeta("Menos calurosa(s)", AZUL, lblFresca));

        btnConsultar.addActionListener(ev -> {
            LocalDate fecha = aLocalDate(spDia);
            lblCalurosa.setText(formatearValor(controlador.masCalurosas(fecha)));
            lblFresca.setText(formatearValor(controlador.menosCalurosas(fecha)));
        });

        JPanel norte = new JPanel(new BorderLayout());
        norte.setBackground(FONDO);
        norte.add(controles, BorderLayout.NORTH);
        norte.add(tarjetas, BorderLayout.CENTER);

        JPanel pestana = new JPanel(new BorderLayout());
        pestana.setBackground(FONDO);
        pestana.add(norte, BorderLayout.NORTH);
        return pestana;
    }

       private static JButton crearBoton(String texto) {
        return new boton(texto);
    }

    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_NEGRITA);
        return etiqueta;
    }

    private static JPanel crearTarjeta(String titulo, Color acento, JLabel valor) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(0x6B7785));

        valor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valor.setForeground(new Color(0x1F2D3D));

        JPanel tarjeta = new JPanel(new GridLayout(2, 1, 0, 4));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, acento),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        tarjeta.add(lblTitulo);
        tarjeta.add(valor);
        return tarjeta;
    }

    private static String formatearValor(List<RegistroTemperatura> registros) {
        return registros.stream()
                .findFirst()
                .map(r -> registros.stream()
                        .map(RegistroTemperatura::ciudad)
                        .collect(Collectors.joining(", "))
                        + String.format(" (%.1f °C)", r.temperatura()))
                .orElse("No hay datos para esa fecha");
    }

   
    private static JSpinner selectorFecha(LocalDate inicial) {
        Date fecha = Date.from(inicial.atStartOfDay(ZoneId.systemDefault()).toInstant());
        JSpinner spinner = new JSpinner(
                new SpinnerDateModel(fecha, null, null, Calendar.DAY_OF_MONTH));
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setFont(FUENTE);
        spinner.setPreferredSize(new Dimension(115, 32));
        return spinner;
    }

    private static LocalDate aLocalDate(JSpinner spinner) {
        return ((Date) spinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
    }
}