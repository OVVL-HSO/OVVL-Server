package com.tam.api.controller;


import com.tam.api.MachineLearningApi;
import com.tam.model.ThreatDataSetResource;
import com.tam.repositories.MLRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@EnableAutoConfiguration
@RestController
public class MLApiController implements MachineLearningApi {


    private MLRepository mlRepository;

    @Autowired
    public MLApiController(MLRepository mlRepository) {
        this.mlRepository = mlRepository;
    }

    @Override
    public ResponseEntity<List<ThreatDataSetResource>> getMLData() {
        List<ThreatDataSetResource> threatDataSetResources = mlRepository.findAll();
        return new ResponseEntity<>(threatDataSetResources, HttpStatus.OK);
    }

}
