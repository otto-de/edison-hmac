package de.otto.edison.hmac;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@Component
@Aspect
public class HmacAuthorizationAspect {

    private AuthenticationService authenticationService;
    private AuthorizationService authorizationService;

    @Resource
    @Required
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Before("@annotation(allowedForRoles)")
    public void assertAuthorized(JoinPoint jp, AllowedForRoles allowedForRoles) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = authenticationService.validateHmacSignature(request);
        final Set<String> roles = new HashSet<>(asList(allowedForRoles.value()));
        authorizationService.authorize(username, roles);
    }

}
