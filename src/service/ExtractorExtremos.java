package service;

import modelo.RegistroTemperatura;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ExtractorExtremos {

    private static TreeMap<Double, List<RegistroTemperatura>> agruparPorTemperatura(
            List<RegistroTemperatura> datos, LocalDate fecha) {
        return datos.stream()
                .filter(r -> r.fecha().equals(fecha))
                .collect(Collectors.groupingBy(
                        RegistroTemperatura::temperatura,
                        TreeMap::new,
                        Collectors.toList()));
    }

    public static List<RegistroTemperatura> masCalurosas(List<RegistroTemperatura> datos,
                                                         LocalDate fecha) {
        return Optional.ofNullable(agruparPorTemperatura(datos, fecha).lastEntry())
                .map(Map.Entry::getValue)
                .orElse(List.of());
    }

    public static List<RegistroTemperatura> menosCalurosas(List<RegistroTemperatura> datos,
                                                           LocalDate fecha) {
        return Optional.ofNullable(agruparPorTemperatura(datos, fecha).firstEntry())
                .map(Map.Entry::getValue)
                .orElse(List.of());
    }
}