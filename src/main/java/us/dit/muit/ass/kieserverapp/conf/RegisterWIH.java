/**
 * 
 */
package us.dit.muit.ass.kieserverapp.conf;

import org.kie.api.runtime.process.WorkItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import us.dit.muit.ass.wih.AppointmentDAOWorkItemHandler;

/**
 * Registro el WIH con el nombre AppointmentDAODefinitions
 * El nombre (campo name) con el que se registra el componente debe coincidir con el que se haya puesto en el wid del kjar. Los wid están en la carpeta global de la base de conocimiento (kjar)
 *[
 *       [
 *           "name" : "AppointmentDAODefinitions",
 *           "displayName" : "AppointmentDAODefinitions",
 *           "category" : "wih",
 *   ...      
 *     ]
 *]
 */

@Configuration
public class RegisterWIH {

    /**
     * Register the external Work Item Handler as a Spring Bean.
     */
    @Bean(name = "AppointmentDAODefinitions")
    WorkItemHandler appointmentDAOWorkItemHandler() {
        return new AppointmentDAOWorkItemHandler();
    }
}
