
# Drone Delivery API

## Descripción
Este proyecto es una API para gestionar una flota de drones de entrega. Permite registrar drones, cargar paquetes, registrar entregas, y consultar el historial de entregas. Está desarrollado usando **Spring Boot con WebFlux**, utilizando una arquitectura reactiva y conectándose a una base de datos PostgreSQL.

---

## **Instrucciones para construir y ejecutar el proyecto en Docker**

### **Requisitos previos**
- Tener instalado:
  - Docker
  - Docker Compose

### **Pasos para ejecutar**
1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-repositorio/drone-delivery-api.git
   cd drone-delivery-api
   ```

2. Construye las imágenes y levanta los contenedores:
   ```bash
   docker-compose up --build
   ```

3. La aplicación estará disponible en:
   - API: `http://localhost:8080`
   - Documentación Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## **Modelo de datos y lógica de negocio**

### **Modelo de datos**

1. **Drone**
   - **id** (UUID): Identificador único del dron.
   - **serialNumber** (String): Número de serie del dron.
   - **model** (String): Modelo del dron.
   - **maxWeight** (Double): Peso máximo que el dron puede cargar.
   - **currentWeight** (Double): Peso actual de la carga en el dron.
   - **status** (String): Estado del dron (`AVAILABLE`, `LOADING`, `DELIVERING`, `UNAVAILABLE`).

2. **Delivery**
   - **id** (UUID): Identificador único de la entrega.
   - **droneId** (UUID): Identificador del dron asociado.
   - **weight** (Double): Peso del paquete entregado.
   - **description** (String): Descripción del paquete.
   - **deliveryTimestamp** (DateTime): Fecha y hora de la entrega.

### **Lógica de negocio**
- **Carga de paquetes**: Valida que el peso no exceda el límite permitido para el dron. Si el dron está cargando, se cambia su estado a `LOADING`.
- **Registro de entregas**: Actualiza el peso actual del dron después de cada entrega. Si no tiene más paquetes, su estado cambia a `AVAILABLE`.
- **Eliminación de drones**: Solo es posible eliminar drones que no tienen entregas activas.

---

## **Ejemplos de cómo probar la API**

### **1. Registrar un dron**
**Endpoint:**
```http
POST /api/drones
```

**Body:**
```json
{
  "serialNumber": "DRN-001",
  "model": "PhantomX",
  "maxWeight": 15.5,
  "currentWeight": 0.0,
  "status": "AVAILABLE"
}
```

### **2. Obtener todos los drones**
**Endpoint:**
```http
GET /api/drones
```

**Response:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "serialNumber": "DRN-001",
    "model": "PhantomX",
    "maxWeight": 15.5,
    "currentWeight": 0.0,
    "status": "AVAILABLE"
  }
]
```

### **3. Cargar un dron**
**Endpoint:**
```http
POST /api/drones/{id}/load
```

**Body:**
```json
{
  "weight": 5.0
}
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "serialNumber": "DRN-001",
  "model": "PhantomX",
  "maxWeight": 15.5,
  "currentWeight": 5.0,
  "status": "LOADING"
}
```

---

## **Pasos para ejecutar las pruebas unitarias y de integración**

### **Requisitos previos**
- Tener instalado Maven.

### **Ejecutar todas las pruebas**
Usa el siguiente comando para ejecutar las pruebas unitarias y de integración:
```bash
mvn clean test
```

### **Ver cobertura de pruebas**
Si deseas generar un informe de cobertura de pruebas, asegúrate de tener configurado un plugin como JaCoCo. Usa:
```bash
mvn clean verify
```

El informe estará disponible en:
```
target/site/jacoco/index.html
```

---

## **Herramientas utilizadas**
- **Spring Boot (WebFlux):** Para crear una API reactiva.
- **PostgreSQL:** Base de datos relacional.
- **Liquibase:** Versionamiento de esquemas de base de datos.
- **Swagger/OpenAPI:** Documentación interactiva de la API.
- **Docker/Docker Compose:** Contenerización de la aplicación.
- **JUnit/Mockito:** Pruebas unitarias y de integración.

---

Este archivo está listo para ser usado como documentación oficial de tu proyecto.
