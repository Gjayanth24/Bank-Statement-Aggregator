package com.BSA.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Configures the security filter chain, specifying which endpoints are public
	 * and which require authentication.
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// Disables CSRF protection, which is useful for stateless API designs.
		http.csrf(csrf -> csrf.disable())
				// Configures authorization rules.
				.authorizeHttpRequests(authorize -> authorize
						// Publicly accessible endpoints (no authentication required).
						.requestMatchers("/*","/users/**", "/companies/**", "/branches/**", "/statements/**").permitAll()
						// All other requests must be authenticated.
						.anyRequest().authenticated());

		// Build the security filter chain object.
		return http.build();
	}

	/**
	 * Configures the password encoder using BCrypt for securely hashing passwords.
	 */
	@Bean
	PasswordEncoder passwordEncoder() { // it is a method
		// BCrypt is a widely used and secure algorithm for password hashing.
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures a custom HTTP firewall to allow certain URL encodings. Useful if
	 * your app needs to handle encoded characters like slashes or percent symbols
	 * in URLs.
	 */
	@Bean
	HttpFirewall allowUrlEncodedSlashHttpFirewall() { // it is a method
		// Instantiating the strict firewall which blocks certain characters by default.
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		// Allow encoded slashes (e.g., %2F) in URLs.
		firewall.setAllowUrlEncodedSlash(true);
		// Allow encoded double slashes (e.g., %2F%2F).
		firewall.setAllowUrlEncodedDoubleSlash(true);
		// Allow encoded percent symbols (e.g., %25).
		firewall.setAllowUrlEncodedPercent(true);
		return firewall;
	}
}
