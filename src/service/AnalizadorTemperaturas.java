package service;

import modelo.RegistroTemperatura;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AnalizadorTemperaturas {

    public static Map<String, Double> promedioPorCiudad(List<RegistroTemperatura> datos,
                                                        LocalDate inicio,
                                                        LocalDate fin) {
        return datos.stream()
                .filter(r -> !r.fecha().isBefore(inicio) && !r.fecha().isAfter(fin))
                .collect(Collectors.groupingBy(
                        RegistroTemperatura::ciudad,
                        TreeMap::new,
                        Collectors.averagingDouble(RegistroTemperatura::temperatura)));
    }
}