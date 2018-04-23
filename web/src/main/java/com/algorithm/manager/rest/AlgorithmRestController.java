package com.algorithm.manager.rest;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;
import com.algorithm.manager.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AlgorithmRestController {
  @Autowired private AlgorithmService algorithmService;

  public AlgorithmRestController(AlgorithmService algorithmService) {
    this.algorithmService = algorithmService;
  }

  @RequestMapping(value = "/api/algorithms", method = RequestMethod.GET)
  public ResponseEntity<List<Algorithm>> getAllAlgorithms() {
    try {
      List<Algorithm> algorithms = algorithmService.getAllAlgorithms();
      return new ResponseEntity<>(algorithms, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/api/paradigmByName", method = RequestMethod.GET)
  public ResponseEntity<DesignParadigm> getDesignParadigmByName(@Param("name") String name) {
    try {
      Optional<DesignParadigm> optParadigm = algorithmService.getDesignParadigmByName(name);
      return optParadigm
          .map(designParadigm -> new ResponseEntity<>(designParadigm, HttpStatus.FOUND))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "api/fieldByName", method = RequestMethod.GET)
  public ResponseEntity<FieldOfStudy> getFieldOfStudyByName(@Param("name") String name) {
    try {
      Optional<FieldOfStudy> optField = algorithmService.getFieldOfStudyByName(name);
      return optField
          .map(fieldOfStudy -> new ResponseEntity<>(fieldOfStudy, HttpStatus.FOUND))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
