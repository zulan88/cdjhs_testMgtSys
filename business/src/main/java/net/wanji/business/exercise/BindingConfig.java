package net.wanji.business.exercise;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jenny
 * @create: 2024-07-31 3:29 下午
 */
@Configuration
@ConfigurationProperties(prefix = "binding")
public class BindingConfig {
    private boolean enabled = false;

    private Map<String, String> relationship = new HashMap<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getRelationship() {
        return relationship;
    }

    public void setRelationship(Map<String, String> relationship) {
        this.relationship = relationship;
    }
}
