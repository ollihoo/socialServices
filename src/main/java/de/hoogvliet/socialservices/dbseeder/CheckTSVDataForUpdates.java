package de.hoogvliet.socialservices.dbseeder;

import de.hoogvliet.socialservices.socialservice.Location;
import de.hoogvliet.socialservices.socialservice.tsv.TSVParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @Log4j2 @RequiredArgsConstructor
public class CheckTSVDataForUpdates {
    private final TSVParser tsvParser;

    @PostConstruct
    public void parseTsvFile() {
        List<Location> locations = tsvParser.getAllEntriesFromTSV();
        log.info("Found and checked {} locations.", locations.size());
    }


}
