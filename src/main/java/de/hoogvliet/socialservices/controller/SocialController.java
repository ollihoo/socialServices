package de.hoogvliet.socialservices.controller;

import de.hoogvliet.socialservices.socialservice.*;
import de.hoogvliet.socialservices.socialservice.tsv.TSVParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;

@RestController@RequiredArgsConstructor @Slf4j
public class SocialController {
    private final SocialServices socialServices;
    private final TSVParser tsvParser;

    @GetMapping("/social")
    @ResponseBody @Validated
    public List<Location> getSocialServiceEntities(
            @RequestParam(value = "c") @Positive @Valid Integer categoryId,
            @RequestParam(value="ct") @Positive @Valid Integer cityId) {
        if (categoryId != null) {
            if (cityId != null) {
                return socialServices.getLocationsByCategoryAndCity(categoryId, cityId);
            }
            return socialServices.getLocationsByCategory(categoryId);
        }
        return Collections.emptyList();
    }

    @GetMapping("/social/online")
    @ResponseBody
    @Validated
    public List<Location> getSocialServiceEntities(
            @RequestParam(value = "c") @Positive @Valid Integer categoryId) {
        if (categoryId != null && categoryId > 0) {
            return socialServices.getLocationsBy2Categories(List.of(57, categoryId));
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/cities")
    @ResponseBody
    public List<City> getCities() {
        return socialServices.getCities();
    }

    @Operation(summary = "Trigger TSV update", description = "Triggers service to update from TSV", tags = "Config")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK - Updated DB from TSV")
    })
    @PostMapping("/tsv-update")
    public ResponseEntity<Void> triggerTSVUpdate() {
        tsvParser.getAllEntriesFromTSV();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(
                "Check your input parameters.",
                HttpStatus.BAD_REQUEST);
    }
}
