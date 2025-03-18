## Proyecto de Pruebas para Postulación a Puesto de Desarrollador Backend

**Creado con:** Spring Boot versión 3.4.1

### Requerimientos
* **Java:** Versión 17 o superior
* **Maven**

### Descargar el Proyecto
```bash
git clone https://github.com/sibieta/springboot-api.git
cd ./springboot-api
```

### Compilar y Ejecutar
```bash
mvn clean package
mvn spring-boot:run
```

### Acceder a la Aplicación
Una vez en ejecución, ingresa la siguiente URL en tu navegador:

http://localhost:8080

Datos de Autenticación:

* **Usuario:** demotest

* **Contraseña:** 12345678

### Swagger UI
Al autenticarte, serás redirigido a la interfaz de Swagger:

http://localhost:8080/swagger-ui/index.html

### Endpoints Disponibles

* **POST /user:** Crea un nuevo usuario.

* **Estos metodos de lectura requieren pasar el header HTTP Authorization: Bearer TOKEN donde este se obtiene en la respuesta al crear un nuevo usuario.

* **GET /user/{id}:** Consulta un usuario existente por su ID.

* **GET /user:** Obtiene un listado de usuarios ya creados

### Ejemplo de Cuerpo de Mensaje JSON
```json
{
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "password": "hunter2##H",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

## Arquitectura del Sistema

![Diagrama de la Arquitectura](diagrama.jpg "Diagrama de la Arquitectura")

Este diagrama ilustra los componentes y sus interacciones en la aplicación
