package de.hoogvliet.socialservices;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ActuatorInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
       String buildVersion = (System.getenv("BUILD.VERSION") != null)?
               System.getenv("BUILD.VERSION"): "SNAPSHOT";
       builder.withDetail("build.version", buildVersion);
    }
}
