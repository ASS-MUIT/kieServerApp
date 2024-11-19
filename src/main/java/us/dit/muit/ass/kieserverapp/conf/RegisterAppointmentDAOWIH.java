/**
 * 
 */
package us.dit.muit.ass.kieserverapp.conf;

import java.util.List;

import org.springframework.stereotype.Component;

import us.dit.muit.ass.wih.AppointmentDAOWorkItemHandler;

/**
 * 
 */
@Component("AppointmentDAODefinitions")
public class RegisterAppointmentDAOWIH extends AppointmentDAOWorkItemHandler {

	public RegisterAppointmentDAOWIH(String appointmentURL, List<String> reqAttributes) {
		super(appointmentURL, reqAttributes);
		// TODO Auto-generated constructor stub
	}

}
