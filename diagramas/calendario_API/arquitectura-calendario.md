# Diagrama de Arquitectura - API Calendario

```mermaid
flowchart TD

    %% =========================
    %% MÓDULO DOMINIO
    %% =========================
    subgraph DomainLayer["Módulo: dominio"]
        direction LR

        Entities["Calendario / Tipo"]
    end


    %% =========================
    %% MÓDULO CORE
    %% =========================
    subgraph CoreLayer["Módulo: core"]
        direction LR

        ServiceInterfaces["ICalendarioServicio / ITipoServicio"]

        RepositoryInterfaces["ICalendarioRepositorio / ITipoRepositorio"]

        IntegrationInterfaces["IFestivosServicioExterno"]

        CoreModels["FestivoExterno"]

        CoreExceptions["SolicitudInvalidaExcepcion / ServicioExternoExcepcion<br/>RecursoNoEncontradoExcepcion / ConflictoExcepcion"]
    end


    %% =========================
    %% MÓDULO APLICACIÓN
    %% =========================
    subgraph ApplicationLayer["Módulo: aplicación"]
        direction TB

        CalendarService["CalendarioServicio / TipoServicio"]
    end


    %% =========================
    %% MÓDULO INFRAESTRUCTURA
    %% =========================
    subgraph InfrastructureLayer["Módulo: infraestructura"]
        direction TB

        RepositoryImpl["CalendarioRepositorio / TipoRepositorio"]

        JpaRepositories["ICalendarioRepositorioJpa / ITipoRepositorioJpa"]

        JpaEntities["CalendarioEntidad / TipoEntidad"]

        Mappers["CalendarioMapeador / TipoMapeador"]

        IntegrationExternal["FestivosServicioExterno"]

        IntegrationDTO["FestivoDto"]

        HttpService["HttpServicio"]

        DB[("Base de Datos PostgreSQL")]

        FestivosAPI["API Festivos<br/>Node.js + Express + MongoDB"]
    end


    %% =========================
    %% MÓDULO PRESENTACIÓN
    %% =========================
    subgraph PresentationLayer["Módulo: presentación"]
        direction TB

        App["ApiApplication<br/>@SpringBootApplication"]

        Controller["CalendarioControlador / TipoControlador"]

        Swagger["SwaggerConfig"]

        Handler["ExcepcionesGlobalesHandler"]

        ErrorDTO["ErrorRespuesta"]
    end


    %% =========================
    %% APLICACIÓN -> CORE / DOMINIO
    %% =========================
    CalendarService -.->|"Implementa"| ServiceInterfaces

    CalendarService -->|"Inyecta"| RepositoryInterfaces

    CalendarService -->|"Inyecta"| IntegrationInterfaces

    CalendarService -->|"Maneja"| Entities

    CalendarService -->|"Usa"| CoreModels

    CalendarService -->|"Lanza excepciones de negocio"| CoreExceptions

    IntegrationInterfaces -->|"Retorna"| CoreModels


    %% =========================
    %% INFRAESTRUCTURA -> CORE
    %% =========================
    RepositoryImpl -.->|"Implementa"| RepositoryInterfaces

    RepositoryImpl -->|"Inyecta"| JpaRepositories

    RepositoryImpl -->|"Usa"| Mappers


    %% =========================
    %% MAPEO DE PERSISTENCIA
    %% =========================
    Mappers -->|"Transforma"| Entities

    Mappers -->|"Transforma"| JpaEntities


    %% =========================
    %% INTEGRACIÓN API FESTIVOS
    %% =========================
    IntegrationExternal -.->|"Implementa"| IntegrationInterfaces

    IntegrationExternal -->|"Usa"| HttpService

    IntegrationExternal -->|"Mapea respuesta"| IntegrationDTO

    IntegrationDTO -->|"Transforma FestivoDto → FestivoExterno"| CoreModels

    HttpService -->|"HTTP GET"| FestivosAPI

    HttpService -->|"Lanza ServicioExternoExcepcion"| CoreExceptions


    %% =========================
    %% JPA -> POSTGRESQL
    %% =========================
    JpaRepositories -->|"Spring Data JPA / SQL"| DB

    JpaEntities -->|"Mapeo ORM @Entity"| DB


    %% =========================
    %% PRESENTACIÓN
    %% =========================
    Controller -->|"Inyecta"| ServiceInterfaces

    Controller -->|"Usa"| Entities

    Handler -->|"Devuelve"| ErrorDTO

    Handler -->|"Captura (400 / 404 / 409 / 502)"| CoreExceptions

    Swagger -.->|"Documenta"| Controller

    App -.->|"Inicializa"| Controller


    %% =========================
    %% ORDEN VISUAL
    %% =========================
    PresentationLayer ~~~ ApplicationLayer

    ApplicationLayer ~~~ CoreLayer

    CoreLayer ~~~ DomainLayer

    ApplicationLayer ~~~ InfrastructureLayer


    %% =========================
    %% ESTILO GENERAL
    %% =========================
    classDef default fill:#FFFFFF,stroke:#334155,stroke-width:1.5px,color:#0F172A


    %% =========================
    %% ESTILOS DE MÓDULOS
    %% =========================
    style PresentationLayer fill:#FFF4E5,stroke:#D97706,stroke-width:2px,color:#0F172A

    style ApplicationLayer fill:#ECFDF3,stroke:#059669,stroke-width:2px,color:#0F172A

    style CoreLayer fill:#EAF4FF,stroke:#2563EB,stroke-width:2px,color:#0F172A

    style DomainLayer fill:#FEFCE8,stroke:#CA8A04,stroke-width:2px,color:#0F172A

    style InfrastructureLayer fill:#F5F0FF,stroke:#7C3AED,stroke-width:2px,color:#0F172A


    %% =========================
    %% PRESENTACIÓN
    %% =========================
    style App fill:#FFFFFF,stroke:#D97706,stroke-width:1.5px,color:#0F172A
    style Controller fill:#FFFFFF,stroke:#D97706,stroke-width:1.5px,color:#0F172A
    style Swagger fill:#FFFFFF,stroke:#D97706,stroke-width:1.5px,color:#0F172A
    style Handler fill:#FFFFFF,stroke:#D97706,stroke-width:1.5px,color:#0F172A
    style ErrorDTO fill:#FFFFFF,stroke:#D97706,stroke-width:1.5px,color:#0F172A


    %% =========================
    %% APLICACIÓN
    %% =========================
    style CalendarService fill:#FFFFFF,stroke:#059669,stroke-width:1.5px,color:#0F172A


    %% =========================
    %% CORE
    %% =========================
    style ServiceInterfaces fill:#FFFFFF,stroke:#2563EB,stroke-width:1.5px,color:#0F172A
    style RepositoryInterfaces fill:#FFFFFF,stroke:#2563EB,stroke-width:1.5px,color:#0F172A
    style IntegrationInterfaces fill:#FFFFFF,stroke:#2563EB,stroke-width:1.5px,color:#0F172A
    style CoreModels fill:#FFFFFF,stroke:#2563EB,stroke-width:1.5px,color:#0F172A
    style CoreExceptions fill:#FFFFFF,stroke:#2563EB,stroke-width:1.5px,color:#0F172A


    %% =========================
    %% DOMINIO
    %% =========================
    style Entities fill:#FFFFFF,stroke:#CA8A04,stroke-width:1.5px,color:#0F172A


    %% =========================
    %% INFRAESTRUCTURA
    %% =========================
    style RepositoryImpl fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A
    style JpaRepositories fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A
    style JpaEntities fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A
    style Mappers fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A
    style IntegrationExternal fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A
    style IntegrationDTO fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A
    style HttpService fill:#FFFFFF,stroke:#7C3AED,stroke-width:1.5px,color:#0F172A

    style DB fill:#FFFFFF,stroke:#DC2626,stroke-width:1.5px,color:#0F172A

    style FestivosAPI fill:#FFFFFF,stroke:#607D8B,stroke-width:1.5px,color:#0F172A


    %% =========================
    %% CONEXIONES
    %% =========================
    linkStyle default stroke:#475569,stroke-width:1.5px
```