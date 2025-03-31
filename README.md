# API Franquicias - Aplicación Reactiva con Spring Boot, WebFlux y MongoDB

Esta aplicación es una API para manejar una lista de franquicias, donde cada franquicia tiene un nombre y una lista de sucursales. Cada sucursal tiene un nombre y una lista de productos, y cada producto se define por su nombre y cantidad de stock. La aplicación está desarrollada con Spring Boot, WebFlux y MongoDB en modo reactivo, utilizando un enfoque basado en Router/Handler para aprovechar al máximo la programación funcional y no bloqueante.

## Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Instalación y Ejecución Local](#instalación-y-ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Endpoints Principales](#endpoints-principales)
- [Notas Adicionales](#notas-adicionales)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

## Características

- **Programación Reactiva:** Utiliza Spring WebFlux y el driver reactivo de MongoDB para operaciones no bloqueantes.
- **Arquitectura Hexagonal:** Separación clara entre dominio, aplicación y adaptadores (handlers/routers para la exposición de endpoints).
- **Operaciones CRUD:** Permite registrar, actualizar, consultar y eliminar franquicias, sucursales y productos.
- **Endpoints Compuestos:** Incluye endpoints para asociar sucursales y productos a franquicias existentes.
- **Dockerizado:** La aplicación se empaqueta en una imagen Docker ligera basada en JDK 17 Alpine.

## Arquitectura

El proyecto sigue una arquitectura hexagonal (también conocida como "Ports & Adapters") con la siguiente estructura:
  
- **Dominio:**  
  - Modelos: `Franquicia`, `Sucursal`, `Producto`.  
  - Excepciones y validaciones.
  
- **Aplicación:**  
  - Servicios que implementan la lógica de negocio y orquestan operaciones sobre los repositorios.
  
- **Infraestructura:**  
  - Adaptadores de salida: Repositorios reactivos para MongoDB.
  - Adaptadores de entrada: Endpoints expuestos mediante Router/Handler (ServerRequest y ServerResponse).

## Requisitos

- Java 17
- Maven 3.9+
- MongoDB (puedes usar una instancia local o remota)
- Docker (opcional, para empaquetar y desplegar la aplicación)

## Instalación y Ejecución Local

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuusuario/api-franquicias.git
cd api_franquicias
```

###2. Configurar MongoDB
- Si usas MongoDB localmente, asegúrate de que esté corriendo en mongodb://localhost:27017. Puedes usar Docker para levantar una instancia:
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

###3. Construir y ejecutar la aplicación
```bash
mvn clean install
mvn spring-boot:run
```
##Ejecución con Docker
- Para construir y ejecutar la aplicación en un contenedor Docker:
```bash
docker build -t api-franquicias .
docker run -p 8080:8080 --name api-franquicias api-franquicias
```
##Endpoints Principales
###Franquicias
- POST /api/franquicias - Registrar una franquicia

- PUT /api/franquicias/{franquiciaId} - Actualizar nombre de una franquicia

- GET /api/franquicias/{franquiciaId}/info - Obtener información completa de una franquicia

- GET /api/franquicias/{franquiciaId}/mas-stock - Obtener el producto con más stock en una sucursal de la franquicia

###Sucursales
- POST /api/franquicias/{franquiciaId}/sucursales - Agregar una nueva sucursal a una franquicia

- PUT /api/franquicias/sucursales/{sucursalId} - Actualizar el nombre de una sucursal

- PUT /api/franquicias/{franquiciaId}/sucursales/asociar/{sucursalId} - Asociar una sucursal existente a una franquicia

- GET /api/franquicias/sucursales - Obtener todas las sucursales

- GET /api/franquicias/sucursales/{sucursalId} - Obtener una sucursal por ID

###Productos
- POST /api/franquicias/sucursales/{sucursalId}/productos - Agregar un producto a una sucursal

- POST /api/franquicias/productos - Registrar un nuevo producto

- PUT /api/franquicias/sucursales/{sucursalId}/productos/asociar/{productoId} - Asociar un producto a una sucursal

- GET /api/franquicias/productos - Obtener todos los productos

- GET /api/franquicias/productos/{productoId} - Obtener un producto por ID

- DELETE /api/franquicias/sucursales/{sucursalId}/productos/{productoId} - Eliminar un producto de una sucursal

- PUT /api/franquicias/productos/{productoId}/stock - Actualizar stock de un producto

- PUT /api/franquicias/productos/{productoId}/nombre - Actualizar nombre de un producto

##Notas Adicionales
- La aplicación está diseñada para ser reactiva y escalar de manera eficiente.

- Se recomienda el uso de herramientas como Postman o cURL para probar los endpoint

##Contribuciones

##Licencias
