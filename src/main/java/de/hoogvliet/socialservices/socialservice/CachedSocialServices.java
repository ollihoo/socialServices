package de.hoogvliet.socialservices.socialservice;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CachedSocialServices {
    private final SocialServices socialServices;
    private static List<Location> locations;
    private static List<Category> categories;

    private final Timer locationsTimer;
    private final Timer categoriesTimer;

    public CachedSocialServices(
            SocialServices socialServices,
            MeterRegistry meterRegistry) {
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

    public synchronized List<Category> getCategories() {
        categoriesTimer.record(() -> {
            if (categories == null) {
                getAllEntries();
                categories = socialServices.getAllCategories();
            }
        });
        return categories;
    }
}
