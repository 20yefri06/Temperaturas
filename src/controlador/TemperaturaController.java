package controlador;

import modelo.RegistroTemperatura;
import service.AnalizadorTemperaturas;
import service.ExtractorExtremos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TemperaturaController {

    private final List<RegistroTemperatura> datos;

    public TemperaturaController(List<RegistroTemperatura> datos) {
        this.datos = datos;
    }

    public Map<String, Double> promedios(LocalDate inicio, LocalDate fin) {
        return AnalizadorTemperaturas.promedioPorCiudad(datos, inicio, fin);
    }

    public List<RegistroTemperatura> masCalurosas(LocalDate fecha) {
        return ExtractorExtremos.masCalurosas(datos, fecha);
    }

    public List<RegistroTemperatura> menosCalurosas(LocalDate fecha) {
        return ExtractorExtremos.menosCalurosas(datos, fecha);
    }
}