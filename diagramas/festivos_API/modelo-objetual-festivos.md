# Modelo Objetual - API Festivos

```mermaid
classDiagram
direction TB

class FestivoService {
    +crearFestivo(festivo: Festivo) Festivo
    +obtenerFestivo(id: int) Festivo
    +actualizarFestivo(id: int, festivo: Festivo) Festivo
    +eliminarFestivo(id: int) boolean
    +esFestivo(fecha: Date) boolean
    +listarFestivosPorAnio(anio: int) array~Festivo~
}

class CalculoFechasService {
    +calcularFecha(festivo: Festivo, tipo: Tipo, anio: int) Date
}

class Tipo {
    +int id
    +string tipo
    +string modoCalculo
}

class Festivo {
    +int dia
    +int mes
    +string nombre
    +int diasPascua
}

FestivoService ..> Tipo : usa
FestivoService ..> Festivo : gestiona
FestivoService ..> CalculoFechasService : usa

CalculoFechasService ..> Tipo : usa regla
CalculoFechasService ..> Festivo : calcula

Tipo "1" *-- "0..*" Festivo : festivos
```