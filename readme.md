# 🚀 Basic Kie Server
## ⚡ Capabilities
Spring Boot application that simply starts a Kie server.
It doesn't load any knowledge base (containers) at startup; this will need to be done later.

Its mission will be to integrate cross-cutting elements that are common to many processes.
These elements should be integrated from **Maven** repositories.

## 🛠️ Development Mode
In managed mode, a Kie server uses the **"controller" REST API** from BC to register itself with the controller.
In turn, the controller (BC) uses the **Kie server's REST API** to manage it (load containers and manage processes, cases, tasks, etc...). Therefore, it's necessary to configure users with permissions for both interfaces; these will be the REST API clients.

This application is configured so that if it is started in development mode the Kie server will be configured in managed mode. Thus, the `application-dev.properties` file is configured so that the server is managed from Business Central (BC).
In the product documentation [Red Hat Process Automation Manager](https://docs.redhat.com/en/documentation/red_hat_process_automation_manager/7.13/html/managing_red_hat_process_automation_manager_and_kie_server_settings/kie-server-configure-central-proc_execution-server#kie-server-configure-central-proc_execution-server) you can find detailed information on how to configure a Kie server and BC in managed mode.

To start the application so that the server works in managed mode by the controller, you should execute:
```bash
.\launch-dev.bat clean install
```

### 🏢 Business Central Configuration
📄 `standalone.xml` file: add the properties `org.kie.server.user` and `org.kie.server.pwd` with the data of a Kie server user that has the kie-server role, which grants credentials to access the server's REST API.

🖥️ GUI: add a user that has the `rest-all` role, which grants credentials to access BC's API. This user must also be configured on the Kie server.

### ⚙️ Kie Server Configuration
📄 `application-dev.properties` file: add the properties `org.kie.server.controller.pwd/user` with the BC user with `rest-all` credentials and `kieserver.controllers` with the BC REST API URL `http://localhost:8080/business-central/rest/controller`

📄 `DefaultWebSecurityConfig` file: in development mode, when no identity provider is used, this is the file where server users are configured. Therefore, here it will be necessary to add a user that has the `kie-server` role, which will grant credentials to access the server API; this user must also be configured in BC.

### ▶️ Execution
1. 🚀 Start Business Central

2. 🔧 When executing `.\launch-dev.bat clean install`, a Kie server connected to Business Central will start.
   You can see the server in the execution servers view with the name **kieserverapp-dev@localhost:8090**

3. 📦 From that moment, containers defined in Business Central can be deployed on this server.

4. 🔄 Subsequently, the processes deployed in the container can be executed from Business Central.
   Remember to select the server in the process view, in the upper right corner (by default sampleServer is used).

### 🔧 Additional Server Configurations in Development Mode
👥 To be able to manage human tasks from BC, the configuration file adds `system.properties.org.jbpm.ht.admin.group=admin`

📚 In this [GitHub repository](https://github.com/dmarrazzo/rh-bpm-notes/blob/master/human_tasks.md) you can find interesting information for human task management.

🗄️ Server has also been configured so that the H2 console is available at `/h2-console`, to facilitate debugging.

### ⚠️ Additional Considerations
☕ Tests have been performed using `jdk-11.0.4` and `jdk-11.0.8`, both work well. So the environment variable `JAVA_HOME` should store the access path to the appropriate JDK. For example:
```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-11.0.4'
```

🔧 If work item handler artifacts are used, care must be taken that they are compiled with a compatible JDK version.