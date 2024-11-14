### Client requirements

Necesidades de datos de empresa:

    -Queremos saber qué trayectos están más hasta arriba de gente
    -Aplicación para gestionar sistema de rutas
    -Saber conductor y vehículo asignados a una ruta
    -Historial de conductor y vehículo
    -Consultar paradas de una ruta y modificarlas
    -Historial de incidencias del vehículo
    -Ver ubicación en tiempo real de los vehículos
    -Consultar y modificar horarios de una ruta
    -Que los conductores puedan introducir eventos
    -Según el tipo de evento, deberían enviarse notificaciones a los clientes
    -Poder asignar vehículos a una ruta
    -Poder asignar conductores a una ruta y un horario

Necesidades de aplicación para clientes:

    -Calculador de costo de viajes
    -Queremos que la gente busque horarios en una app
    -Ver notificaciones de retrasos
    -Que se pueda modificar el perfil del usuario
    -Consultar rutas


#### Model

```mermaid
---
title: Transport system
---
classDiagram

    class PassengerManagement{
        <<Bounded context>>
        - PassengerService
        - PassengerRepository
        - PassengerFactory
    }

    class Passenger{
        <<Aggregate Root>>
        - String id
        - String name
        - String email
        - Integer phone
        - LocalDateTime registryDate
        - List~Trip~ trips
    }
    
    class Trip {
        <<Entity>>
        - String id
        - LocalDateTime startTime
        - LocalDateTime endTime
        - Stop startStop
        - Stop endStop
        - Double fare
    }
        
    class RouteManagement{
        <<Bounded context>>
        - RouteService
        - RouteRepository
        - RouteFactory
    }
    
    class Route{
        <<Aggregate Root>>
        - String id
        - String name
        - List~Stop~ stops
        - List~Schedule~ schedules
    }
    
    class Stop{
        <<Entity>>
        -String id
        -String name
        -Coordinates location
    }

    class Coordinates{
        <<Value Object>>
        - String latitude
        - String longitude
    }
    
    class Schedule {
        <<Value Object>>
        - String typeOfDay
        - LocalDateTime startTime
        - LocalDateTime endTime
        - Integer frequencyInMinutes
    }
    
    class TypeOfDay {
        <<Enumeration>>
        DAYOFWEEK
        FRIDAY
        SATURDAY
        SUNDAY
        HOLIDAY
        EXCEPTION
    }

    class VehicleManagement{
        <<Bounded context>>
        - VehicleService
        - VehicleRepository
        - VehicleFactory
    }
    
    class Vehicle{
        <<Aggregate Root>>
        - String id
        - String plateNumber
        - Integer capacity
        - String status
        - String type
        - Coordinates location
        - List~Event~ events
        - Double speed
        - String direction
        - List~VehicleHistory~ vehicleHistory
    }

    class Event{
        <<Value Object>>
        - String type
        - LocalDateTime time
        - String details
        - Maintenance maintenance
        - Notification notification
    }

    class VehicleHistory {
        <<Entity>>
        +String id
        +String RouteId
        +String driverId
        +LocalDateTime startTime
        +LocalDateTime endTime
    }

    class Driver{
        <<Entity>>
        - String id
        - String name
        - Integer contact
    }

    class NotificationManagement{
        <<Bounded context>>
        - NotificationService
        - NotificationRepository
        - NotificationFactory
    }

    class Notification{
        <<Aggregate Root>>
        - LocalDateTime timestamp
        - String Type
        - String title
        - String message
        - String severity
        - List~Action~ actions
    }

    class Action {
        <<Value Object>>
        - String type
        - String status
    }
    
    PassengerManagement --> Passenger
    Passenger --> Trip
    Trip --> Stop
    Stop --> Coordinates
    
    RouteManagement --> Route
    Route --> Stop
    Route --> Schedule
    
    VehicleManagement --> Vehicle
    Vehicle --> VehicleHistory
    VehicleHistory --> Route
    VehicleHistory --> Driver
    Vehicle --> Coordinates
    Vehicle --> Event
    Event --> Notification
    
    NotificationManagement --> Notification
    Notification --> Action

```

### Queue communication map

```mermaid
sequenceDiagram
    actor VehicleSender
    actor EventTrigger
    
    participant eventQueue
    participant monitoringReceiver
    participant vehicleReceiver
    participant routerReceiver
    participant notificationReceiver
    participant database
    participant passengers


    VehicleSender ->> +eventQueue: Vehicle arrives at stop
    VehicleSender ->> +eventQueue: Update vehicle position
    VehicleSender ->> +eventQueue: Update vehicle capacity
    
    eventQueue ->> +monitoringReceiver: Receive updated vehicle position
    eventQueue ->> +vehicleReceiver: Update capacity of vehicle
    
    EventTrigger ->> +eventQueue: Vehicle has accident
    eventQueue ->> notificationReceiver: Create new notification
    #-accidentes
    #-cuando bus llega a una parada
    #bus a punto de llegar a parada (alerta a usuario)
    #-bus actualiza qué tan ocupado está
```