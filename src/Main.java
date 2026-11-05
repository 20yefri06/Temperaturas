import controlador.TemperaturaController;
import modelo.RegistroTemperatura;
import service.LectorCSV;
import vista.VentanaTemperaturas;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Path ruta = Path.of(args.length > 0 ? args[0] : "Temperaturas.csv");
        try {
            List<RegistroTemperatura> datos = LectorCSV.leer(ruta);
            TemperaturaController controlador = new TemperaturaController(datos);
            SwingUtilities.invokeLater(() -> new VentanaTemperaturas(controlador).setVisible(true));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se pudo leer el archivo: " + ruta);
        }
    }
}