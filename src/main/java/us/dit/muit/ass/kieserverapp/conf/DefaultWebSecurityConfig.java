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
 * ES: Versión nueva de seguridad, sustituye la configuración por defecto que se
 * genera con el arquetipo maven utilizando convenciones de seguridad más
 * actuales ahora está basada en beans, más información en:
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * Esto va bien cuando se utiliza el spring boot starter 2.6.15 y el kie server
 * 7.74.1.Final fecha de la revisión 22/11/2024
 * 
 * EN: New security version, replaces the default configuration that is
 * generated with the maven archetype using more current security conventions
 * now it's based on beans, more information at:
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * This works well when using spring boot starter 2.6.15 and kie server
 * 7.74.1.Final revision date 22/11/2024
 * 
 * ES: TO DO: Utilizar la autenticación basada en oauth o en SAML (SSO) contra
 * un
 * servidor de autenticación externo REF:
 * https://is.docs.wso2.com/en/latest/sdks/spring-boot/ para hacerlo con el
 * identity server de WSO2 usando oauth
 * 
 * EN: TO DO: Use oauth or SAML (SSO) based authentication against an
 * external authentication server REF:
 * https://is.docs.wso2.com/en/latest/sdks/spring-boot/ to do it with the
 * WSO2 identity server using oauth
 * 
 * @author Isabel Román Martínez
 */
@Configuration("kieServerSecurity")
@EnableWebSecurity
public class DefaultWebSecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/**
		 * ES: configuraicón el constructor de un objeto
		 * org.springframework.security.config.annotation.web.builders.HttpSecurity
		 * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeHttpRequests(org.springframework.security.config.Customizer)
		 * 
		 * EN: configuration of the constructor of an object
		 * org.springframework.security.config.annotation.web.builders.HttpSecurity
		 * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeHttpRequests(org.springframework.security.config.Customizer)
		 */
		/**
		 * ES: TODO
		 * https://stackoverflow.com/questions/53395200/h2-console-is-not-showing-in-browser
		 * Añadir esta configuración, la última línea del http, para que la
		 * consola h2 se vea
		 * 
		 * EN: TODO
		 * https://stackoverflow.com/questions/53395200/h2-console-is-not-showing-in-browser
		 * Add this configuration, the last line of the http, so that the
		 * h2 console can be seen
		 */
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.antMatchers("/**").authenticated()
				.antMatchers("/*", "/img/*", "/js/**", "/css/**").permitAll())
				.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/access-denied.html"))
				.csrf((csrf) -> csrf.disable()).httpBasic(withDefaults()).cors(withDefaults()).formLogin(withDefaults())
				.headers().frameOptions().disable();
		return http.build();
	}

	/**
	 * ES: Configuración de la autenticación con autenticación en memoria y
	 * encriptada
	 * Muy débil no sirve para producción aspectos de gestión de tareas humanas
	 * https://github.com/dmarrazzo/rh-bpm-notes/blob/master/human_tasks.md
	 * 
	 * EN: Authentication configuration with in-memory and encrypted authentication
	 * Very weak, not suitable for production, human task management aspects
	 * https://github.com/dmarrazzo/rh-bpm-notes/blob/master/human_tasks.md
	 **/

	@Bean
	UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {

		// ES: codificación de las password en https://bcrypt-generator.com/, uso nombre
		// como password
		// EN: passwords encoding at https://bcrypt-generator.com/, use name as password
		/**
		 * ES: Los usuario controladores (kieserver, controller y admin) permiten la
		 * conexión en modo development, para que el kie server sea controlado por
		 * Business Central, tiene que tener el rol kie-server este usuario tiene que
		 * estar configurado en el BC De este modo el kie server embebido en esta
		 * aplicación tendrá estos usuarios como controladores externos
		 * 
		 * EN: The controller users (kieserver, controller and admin) allow the
		 * connection in development mode, so that the kie server is controlled by
		 * Business Central, it must have the kie-server role this user must be
		 * configured in BC. This way the kie server embedded in this
		 * application will have these users as external controllers
		 */
		UserDetails controller = User.withUsername("controllerUser").password(encoder.encode("controllerUser"))
				.roles("kie-server").roles("user").build();
		UserDetails kieserver = User.withUsername("kieserver").password(encoder.encode("kieserver1!"))
				.roles("kie-server").roles("user").build();
		UserDetails admin = User.withUsername("admin").password(encoder.encode("admin")).roles("kie-server").build();

		// ES: A continuación se configuran otros usuarios, algunos, como wbadmin o
		// katy,
		// coinciden con usuarios por defecto de BC
		// EN: The following configures other users, some, like wbadmin or katy,
		// match with BC default users
		// ES: TODO: En esta configuración hay exceso de credenciales, sería necesaria
		// una limpieza
		// EN: TODO: In this configuration there are excess credentials, a cleanup would
		// be necessary

		UserDetails practitioner = User.withUsername("practitioner").password(encoder.encode("practitioner"))
				.roles("practitioner").build();

		UserDetails wbadmin = User.withUsername("wbadmin").password(encoder.encode("wbadmin")).roles("kie-server")
				.roles("practitioner").roles("rest-all").roles("user").roles("admin").build();

		UserDetails user = User.withUsername("user").password(encoder.encode("user")).roles("HR").build();

		UserDetails medico = User.withUsername("medico").password(encoder.encode("medico")).roles("practitioner")
				.roles("user").roles("admin").build();
		UserDetails katy = User.withUsername("katy").password(encoder.encode("katy")).roles("practitioner")
				.roles("user").roles("admin").build();

		return new InMemoryUserDetailsManager(katy, admin, wbadmin, user, kieserver, practitioner, medico, controller);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
		// ES: addAllowedOriginPattern lo añado yo y quito la linea de arriba
		// EN: addAllowedOriginPattern I add it and remove the line above
		corsConfiguration.addAllowedOriginPattern("*");

		corsConfiguration.setAllowCredentials(true);

		// ES: cambio la configuración de métodos a add....
		// EN: I change the methods configuration to add....
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