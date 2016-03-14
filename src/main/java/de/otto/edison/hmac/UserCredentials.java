package de.otto.edison.hmac;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.List;
import java.util.Set;

@Immutable
public class UserCredentials {

    private final String name;
    private final String password;
    private final Set<String> roles;

    private UserCredentials(final String name,
                            final String password,
                            final Set<String> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCredentials that = (UserCredentials) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return !(roles != null ? !roles.equals(that.roles) : that.roles != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
