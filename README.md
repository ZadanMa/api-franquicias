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
