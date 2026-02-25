package com.post_hub.iam_service.config;

import com.post_hub.iam_service.security.filter.JwtRequestFilter;
import com.post_hub.iam_service.security.handler.AccessRestrictionHandler;
import com.post_hub.iam_service.service.UserService;
import com.post_hub.iam_service.service.model.IamServiceUserRole;
import jakarta.servlet.DispatcherType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtRequestFilter jwtRequestFilter;
	private final AccessRestrictionHandler accessRestrictionHandler;

	private final static String POST = "POST";
	private final static String GET = "GET";

	private final static AntPathRequestMatcher[] NOT_SECURED_URLS = new AntPathRequestMatcher[]{
			new AntPathRequestMatcher("/auth/login", POST),
			new AntPathRequestMatcher("/auth/register", POST),
			new AntPathRequestMatcher("/auth/refresh/token", GET),

			new AntPathRequestMatcher("/v3/api-docs/**"),
			new AntPathRequestMatcher("/swagger-ui/**"),
			new AntPathRequestMatcher("/swagger-ui.html/**"),
			new AntPathRequestMatcher("//webjars/**"),
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						// Allow Spring Boot error dispatching to reach BasicErrorController so invalid URLs become 404
						.dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
						.requestMatchers("/error").permitAll()

						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(NOT_SECURED_URLS).permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling(exceptions -> exceptions
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
						.accessDeniedHandler(accessRestrictionHandler)
				).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		config.setExposedHeaders(List.of("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userService);

		return daoAuthenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	private String[] adminAccessSecurityRoles() {
		return new String[]{
				IamServiceUserRole.SUPER_ADMIN.getRole(),
				IamServiceUserRole.ADMIN.getRole()
		};
	}

	private static AntPathRequestMatcher get(String pattern) {
		return new AntPathRequestMatcher(pattern, GET);
	}
}
