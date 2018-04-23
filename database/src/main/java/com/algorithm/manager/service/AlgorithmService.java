package com.algorithm.manager.service;

import com.algorithm.manager.model.Algorithm;
import com.algorithm.manager.model.Application;
import com.algorithm.manager.model.AreaOfUse;
import com.algorithm.manager.model.DesignParadigm;
import com.algorithm.manager.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface AlgorithmService {

  Algorithm persistAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception;

  List<Algorithm> getAllAlgorithms() throws Exception;

  List<Algorithm> searchAlgorithm(
      String text, String text1, DesignParadigm selectedItem, FieldOfStudy selectedItem1)
      throws Exception;

  void setAlgorithmName(Algorithm algorithm, String newName) throws Exception;

  void setAlgorithmComplexity(Algorithm rowValue, String newComplexity) throws Exception;

  void deleteAlgorithm(Algorithm algorithm) throws Exception;

  void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws Exception;

  void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws Exception;

  // -------------------------------------------------------

  List<DesignParadigm> getDesignParadigms() throws Exception;

  Optional<DesignParadigm> getDesignParadigmByName(String name) throws Exception;

  default boolean containsParadigm(DesignParadigm paradigm) throws Exception {
    return getDesignParadigmByName(paradigm.getName()).isPresent();
  }

  DesignParadigm createDesignParadigm(String paradigmName) throws Exception;

  DesignParadigm createDesignParadigm(String paradigmName, String description) throws Exception;

  // ------------------------------------------------------------

  List<FieldOfStudy> getAllFieldsOfStudy() throws Exception;

  Optional<FieldOfStudy> getFieldOfStudyByName(String name) throws Exception;

  default boolean containsField(FieldOfStudy fieldOfStudy) throws Exception {
    return getDesignParadigmByName(fieldOfStudy.getName()).isPresent();
  }

  FieldOfStudy createFieldOfStudy(String fieldName) throws Exception;

  FieldOfStudy createFieldOfStudy(String fieldName, String fieldDescription) throws Exception;

  // ------------------------------------------------------------

  List<AreaOfUse> getAllAreas() throws Exception;

  AreaOfUse createAreaOfUse(String area) throws Exception;

  AreaOfUse createAreaOfUse(String area, String description) throws Exception;

  void deleteAreaOfUse(AreaOfUse areaOfUse) throws Exception;

  Optional<AreaOfUse> getAreaOfUse(String name) throws Exception;

  // -----------------------------------------------------

  void createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  List<Application> getApplicationsAlgorithm(Algorithm algorithm) throws Exception;

  List<Application> getApplicationsByArea(AreaOfUse areaOfUse) throws Exception;

  void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;
}
