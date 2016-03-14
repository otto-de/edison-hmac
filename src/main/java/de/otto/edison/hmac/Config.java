package de.otto.edison.hmac;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix="edison.hmac")
public class Config {

    private List<Credentials> users = new ArrayList<>();

    public Config(List<Credentials> users) {
        this.users = users;
    }

    public List<Credentials> getUsers() {
        return users;
    }

    public void setUsers(List<Credentials> users) {
        this.users = users;
    }

    private static class Credentials {
        private String name;
        private String roles;
        private String key;

        public Credentials(String name, String roles, String key) {
            this.name = name;
            this.roles = roles;
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public String getRoles() {
            return roles;
        }

        public String getKey() {
            return key;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}