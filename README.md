##Proyeco de pruebas para postulacion a puesto de Desarrollador Backend##

Creado con spring boot version 3.4.1

Requerimientos:

Java version 17+

Maven


Descargar proyecto:

git clone https://github.com/sibieta/postulacion-bci.git

cd ./postulacion-bci

Compilar:

mvn clean package

Correr:

mvn spring-boot:run

Una vez corriendo ingresar con el navegador la url:
http://localhost:8080

Datos de autenticacion:

Usuario: demotest

Passwd: 12345678

Al autenticar redirecciona al swagger

http://localhost:8080/swagger-ui/index.html

Una vez ahi hay disponibles 2 endpoints

POST /user <- Para crear un nuevo usuario

GET /user/{id} <- Para consultar un usuario existente con el id

En el boton "try" y "EXECUTE" es posible probar los endpoints.

Ejemplo del cuerpo de mensjae JSON:

{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "hunter2",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "contrycode": "57"
        }
    ]
}


