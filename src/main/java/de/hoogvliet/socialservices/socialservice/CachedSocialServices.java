package de.hoogvliet.socialservices.socialservice;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

@Service
public class CachedSocialServices {
    private final SocialServices socialServices;
    private static List<Location> locations;
    private static TreeSet<String> categories;

    private final Timer locationsTimer;
    private final Timer categoriesTimer;

    public CachedSocialServices(SocialServices socialServices, MeterRegistry meterRegistry) {
        this.socialServices = socialServices;
        locationsTimer = Timer.builder("CachedSocialServices.getAllEntries").register(meterRegistry);
        categoriesTimer = Timer.builder("CachedSocialServices.getCategories").register(meterRegistry);
    }

    public synchronized List<Location> getAllEntries() {
        locationsTimer.record(() -> {
            if (locations == null) {
                locations = socialServices.getAllEntries();
            }
        });
        return locations;
    }

    public synchronized TreeSet<String> getCategories() {
        categoriesTimer.record(() -> {
            if (categories == null) {
                getAllEntries();
                categories = socialServices.getCategories(locations);
            }
        });
        return categories;
    }
}
