package de.hoogvliet.socialservices.osm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component @Slf4j
public class OsmMapper {

    public void write(List<? extends OsmCity> osmData, Resource cacheFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = cacheFile.getFile();
        mapper.writeValue(file, osmData);
    }

    public List<OsmLocation> getLocations(Resource resource) throws IOException {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(resource.getFile(), new TypeReference<>() {});
    }

    public List<OsmCity> getCities(Resource resource) throws IOException {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(resource.getFile(), new TypeReference<>() {});
    }
}
