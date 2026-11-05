package service;

import modelo.RegistroTemperatura;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LectorCSV {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate parsearFecha(String texto) {
        return LocalDate.parse(texto.trim(), FORMATO);
    }

    public static List<RegistroTemperatura> leer(Path ruta) throws IOException {
        try (Stream<String> lineas = Files.lines(ruta)) {
            return lineas
                    .skip(1)                                        
                    .filter(linea -> !linea.isBlank())            
                    .map(linea -> linea.split(","))                 
                    .filter(c -> c.length == 3)                    
                    .map(c -> new RegistroTemperatura(              
                            c[0].trim(),
                            parsearFecha(c[1]),
                            Double.parseDouble(c[2].trim())))
                                       .collect(Collectors.toList());                  
        }
    }
}