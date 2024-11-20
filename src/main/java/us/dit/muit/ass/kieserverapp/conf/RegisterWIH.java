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
 */

@Configuration
public class RegisterWIH {

    /**
     * Register the external Work Item Handler as a Spring Bean.
     */
    @Bean(name = "AppointmentDAODefinitions")
    WorkItemHandler appointmentDAOWorkItemHandler() {
        return new AppointmentDAOWorkItemHandler(null, null);
    }
}