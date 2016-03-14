package de.otto.edison.hmac;

import de.otto.hmac.DefaultHmacConfiguration;
import de.otto.hmac.authentication.AuthenticationFilter;
import de.otto.hmac.authentication.AuthenticationService;
import de.otto.hmac.authorization.DefaultAuthorizationService;
import de.otto.hmac.authorization.RolesAuthorizationAspect;
import de.otto.hmac.repository.PropertyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class HmacConfiguration {

    @Value(value = "${edison.hmac.credentials-file}")
    private String authJson;

    @Autowired
    private HttpServletRequest request;

    @Bean
    public PropertyUserRepository userRepository() {
        return new PropertyUserRepository(authJson);
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService(userRepository());
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(authenticationService());
    }

    @Bean
    public DefaultHmacConfiguration defaultHmacConfiguration() {
        return new DefaultHmacConfiguration();
    }

    @Bean
    public DefaultAuthorizationService defaultAuthorizationService() {
        return new DefaultAuthorizationService(userRepository(), defaultHmacConfiguration());
    }

    @Bean
    public RolesAuthorizationAspect rolesAuthorizationAspect() {
        return new RolesAuthorizationAspect(defaultAuthorizationService(), request);
    }
}
