package com.tam.api.controller;

import com.tam.api.SoftwareApi;
import com.tam.model.CPEResource;
import com.tam.model.CVEResource;
import com.tam.services.meta.CPEService;
import com.tam.services.meta.CVEService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
@RestController
public class SoftwareApiController implements SoftwareApi {

    private CPEService cpeService;
    private CVEService cveService;

    @Autowired
    public SoftwareApiController(CPEService cpeService,
                                 CVEService cveService) {
        this.cpeService = cpeService;
        this.cveService = cveService;
    }

    @Override
    public ResponseEntity<List<CPEResource>> findCPEs(@ApiParam(value = "cpe query") @Valid @RequestParam(value = "query") String query) {
        List<CPEResource> cpeItems = cpeService.findIemsMatchingProductQuery(query);
        return new ResponseEntity<>(cpeItems, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CVEResource>> findVulnerabilitiesForAListOfCPEs(@ApiParam(value = "cpe for threat lookup") @Valid @RequestParam(value = "cpes") List<String> cpes) {
        List<CVEResource> cveItems = new ArrayList<>();
        List<CVEResource> cves = cveService.loadAllCVES();
        cpes.forEach(cpe -> cveItems.addAll(cveService.findCVEDataAffectingCPE(cpe, cves)));
        return new ResponseEntity<>(cveItems, HttpStatus.OK);
    }
}
