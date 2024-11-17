# Servidor Kie básico
=============================
## Capacidades
Aplicación spring boot que simplemente arranca un servidor kie.
No carga ningún contenedor en el arranque, tendrá que hacerse a posteriori.
Su misión será integrar elementos transversales, que sean comunes a muchos procesos.
Estos elementos deberán integrarse desde repositorios maven.


## Modo desarrollo
### Configuración de Business Central
standalone.xml: añadir las propiedades org.kie.server.user y org.kie.server.pwd con los datos de un usuario del servidor kie que tenga el rol kie-server, que concede credenciales para acceder a la API del servidor REST
GUI: añadir un usuario que tenga el rol rest-all, que concede credenciales para acceder a la API de BC
### Configuración del servidor
application-dev.properties: añadir las propiedades org.kie.server.controller.pwd/user con el usuario de BC con credenciales rest-all y kieserver.controllers con la url de la api rest de bc http://localhost:8080/business-central/rest/controller
DefaultWebSecurityConfig: añadir un usuario que tenga el rol kie-server, que concederá credenciales para acceder a la API del servidor
### Ejecución
Arrancar Business Central
Al ejecutar .\launch-dev.bat clean install se arrancará un servidor kie conectado business central.
Podrá verse el servidor en la vista de servidores de ejecución con el nombre kieserverapp-dev@localhost:8090
A partir de ese momento se pueden desplegar en este servidor contenedores definidos en business central
Posteriormente se pueden ejecutar los procesos desplegados en el contenedor desde business central.
Para ello recordar seleccionar el servidor en la vista de procesos, en la esquina superior derecha (por defecto se usa sampleServer)