package us.dit.muit.ass.kieserverapp.conf;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Versión nueva de seguridad, sustituye la ocnfiguración por defecto que se
 * genera con el arquetipo maven utilizando convenciones de seguridad más
 * actuales ahora está basada en beans, más información en:
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * Esto va bien cuando se utiliza el spring boot starter 2.6.15 y el kie server
 * 7.74.1.Final fecha de la revisión 6/10/2023
 * 
 * TO DO: Utilizar la autenticación basada en oauth o en SAML (SSO) contra un
 * servidor de autenticación externo REF:
 * https://is.docs.wso2.com/en/latest/sdks/spring-boot/ para hacerlo con el
 * identity server de WSO2 usando oauth
 */
@Configuration("kieServerSecurity")
@EnableWebSecurity
public class DefaultWebSecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/**
		 * Configuro el constructor de un objeto
		 * org.springframework.security.config.annotation.web.builders.HttpSecurity
		 * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeHttpRequests(org.springframework.security.config.Customizer)
		 */
		/**
		 * TODO
		 * https://stackoverflow.com/questions/53395200/h2-console-is-not-showing-in-browser
		 * Tengo que añadir esta configuración, la última línea del http, para que la consola h2 se vea
		 */
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.antMatchers("/**").authenticated().antMatchers("/*", "/img/*","/js/**", "/css/**")
				.permitAll())
				.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/access-denied.html"))
				.csrf((csrf) -> csrf.disable()).httpBasic(withDefaults()).cors(withDefaults())
				.formLogin(withDefaults())
				.headers().frameOptions().disable();
		return http.build();
	}

	/**
	 * Configuración de la autenticación con autenticación en memoria y encriptada
	 * Muy débil no sirve para producción
	 **/

	@Bean
	UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {

		// codifico las password en https://bcrypt-generator.com/, uso nombre como
		// password


		//Este usuario se va a utilizar para el acceso al servidor
		UserDetails consentimientos = User.withUsername("consentimientos").password(encoder.encode("consentimientos")).roles("kie-server").build();
	
        UserDetails practitioner = User.withUsername("practitioner").password(encoder.encode("practitioner")).roles("practitioner").build();		

        UserDetails wbadmin = User.withUsername("wbadmin").password(encoder.encode("wbadmin")).roles("kie-server").roles("practitioner").roles("rest-all").build();

        UserDetails user = User.withUsername("user").password(encoder.encode("user")).roles("HR").build();

        UserDetails admin = User.withUsername("admin").password(encoder.encode("admin")).roles("kie-server").build();

        UserDetails medico = User.withUsername("medico").password(encoder.encode("medico")).roles("practitioner").build();
        /**
		 * El usuario controlador (kieserver y controller) permite la conexión en modo development, para que el kie server sea controlado por BC, tiene que tener el rol kie-server
		 * este usuario tiene que estar configurado en el BC
		 */
        UserDetails controller = User.withUsername("controllerUser").password(encoder.encode("controllerUser")).roles("kie-server").build();
		UserDetails kieserver = User.withUsername("kieserver").password(encoder.encode("kieserver1!")).roles("kie-server").build();

		return new InMemoryUserDetailsManager(admin,wbadmin, user, kieserver, consentimientos, practitioner, medico, controller);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration corsConfiguration = new CorsConfiguration();
	        //corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
			//addAllowedOriginPattern lo añado yo y quito la linea de arriba
			corsConfiguration.addAllowedOriginPattern("*");
		
	        corsConfiguration.setAllowCredentials(true);
	        
			//cambio la configuración de métodos a add....
	        corsConfiguration.addAllowedMethod(HttpMethod.GET);
	        corsConfiguration.addAllowedMethod(HttpMethod.HEAD);
	        corsConfiguration.addAllowedMethod(HttpMethod.POST);
	        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
	        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
	        corsConfiguration.applyPermitDefaultValues();
	        source.registerCorsConfiguration("/**", corsConfiguration);
	        return source;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	

}