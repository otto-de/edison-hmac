package de.otto.edison.hmac;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class AuthorizationService extends PropertySourcesPlaceholderConfigurer {

    private Environment environment;
    private static final Logger LOG = getLogger(AuthorizationService.class);

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
        super.setEnvironment(environment);
    }

    public void authorize(String username, Set<String> expectedRoles) {
        ImmutableList<String> userRoles = readUserRolesFromPropertyFile(username);

        if (intersection(expectedRoles, userRoles).isEmpty()) {
            LOG.info("[%s] is not in one of these groups: %s.", username, expectedRoles);
            throw new AuthorizationException(username);
        }
    }

    private Set<String> intersection(final Collection<String> allowedForRoles, final Collection<String> rolesForUser) {
        final HashSet<String> set = new HashSet<>(allowedForRoles);
        set.retainAll(rolesForUser);
        return set;
    }

    private ImmutableList<String> readUserRolesFromPropertyFile(final String username) {
        final String vaultPropertyKeys = environment.getProperty("edison.hmac." + username + ".roles");
        if (StringUtils.isEmpty(vaultPropertyKeys)) {
            return ImmutableList.of();
        }
        List<String> userRoles = Arrays.asList(vaultPropertyKeys.split(","));
        return ImmutableList.copyOf(userRoles);
    }
}
