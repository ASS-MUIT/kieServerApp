# Servidor Kie básico
## Capacidades
Aplicación spring boot que simplemente arranca un servidor kie.
No carga ningún contenedor en el arranque, tendrá que hacerse a posteriori.

Su misión será integrar elementos transversales, que sean comunes a muchos procesos.
Estos elementos deberán integrarse desde repositorios maven.


## Modo desarrollo
En el modo gestionado un servidor kie utiliza la API REST "controller" de BC para registrarse en el controlador.
A su vez, el controlador (BC) utiliza la API REST del servidor kie para gestionarlo (cargar contenedores y gestionar procesos, casos, tareas, etc...). Por ello es necesario configurar usuarios con permisos para ambas interfaces, estos serán los clientes de las APIs REST.

Esta aplicación está configurada para que en modo desarrollo el servidor kie se configure en modo gestionado. De manera que el fichero ``application-dev.properties`` está configurado para que el servidor se gestione desde business central (BC). 
En la documentación del producto [Process Automation Manager de RedHat](https://docs.redhat.com/en/documentation/red_hat_process_automation_manager/7.13/html/managing_red_hat_process_automation_manager_and_kie_server_settings/kie-server-configure-central-proc_execution-server#kie-server-configure-central-proc_execution-server) puede encontrar información detallada de cómo configurar un servidor kie y BC en modo gestionado.

Para arrancar la aplicación de manera que el servidor trabaje en modo gestionado por el controlador deberá ejecutar:
`` .\launch-dev.bat clean install ``

### Configuración de Business Central
Fichero ``standalone.xml``: añadir las propiedades org.kie.server.user y org.kie.server.pwd con los datos de un usuario del servidor kie que tenga el rol kie-server, que concede credenciales para acceder a la API del servidor REST

GUI: añadir un usuario que tenga el rol ``rest-all``, que concede credenciales para acceder a la API de BC. Este usuario tiene que estar configurado también en el servidor kie.
### Configuración del servidor
Fichero ``application-dev.properties``: añadir las propiedades ``org.kie.server.controller.pwd/user`` con el usuario de BC con credenciales ``rest-all`` y ``kieserver.controllers`` con la url de la api rest de BC ``http://localhost:8080/business-central/rest/controller``

Fichero ``DefaultWebSecurityConfig``: en modo desarrollo, cuando no se utiliza ningún proveedor de identidad, este es el fichero en el que se configuran los usuarios del servidor. Por ello aquí será necesario añadir un usuario que tenga el rol ``kie-server``, que concederá credenciales para acceder a la API del servidor, este usuario tiene que estar también configurado en BC.
### Ejecución
Arrancar Business Central

Al ejecutar `` .\launch-dev.bat clean install `` se arrancará un servidor kie conectado business central.
Podrá verse el servidor en la vista de servidores de ejecución con el nombre kieserverapp-dev@localhost:8090

A partir de ese momento se pueden desplegar en este servidor contenedores definidos en business central
Posteriormente se pueden ejecutar los procesos desplegados en el contenedor desde business central.
Para ello recordar seleccionar el servidor en la vista de procesos, en la esquina superior derecha (por defecto se usa sampleServer)
### Configuraciones adicionales del servidor en modo desarrollo
Para poder gestionar las tareas humanas desde BC en el fichero de configuración se añade `` system.properties.org.jbpm.ht.admin.group=admin ``

En este [repositorio de github](https://github.com/dmarrazzo/rh-bpm-notes/blob/master/human_tasks.md) puede encontrar información interesante para la gestión de las tareas humanas

También se ha configurado para que la consola h2 esté disponible en ``/h2-console``, para así facilitar la depuración.

### Consideraciones adicionales
Las pruebas están realizadas usando ``jdk-11.0.4`` y ``jdk-11.0.8`` ambos van bien. De modo que la variable de entorno ``JAVA_HOME`` debería guardar la ruta de acceso al jdk adecuado. P.e.
`` $env:JAVA_HOME='C:\Program Files\Java\jdk-11.0.4' ``

Si se usan artefactos de work item handlers hay que tener precaución de que estén compilados con versión compatible del jdk
