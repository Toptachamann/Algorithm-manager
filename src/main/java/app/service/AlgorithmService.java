package app.service;

import app.model.Algorithm;
import app.model.Application;
import app.model.AreaOfUse;
import app.model.DesignParadigm;
import app.model.FieldOfStudy;

import java.util.List;
import java.util.Optional;

public interface AlgorithmService {
  List<Algorithm> getAllAlgorithms() throws Exception;

  List<DesignParadigm> getDesignParadigms() throws Exception;

  List<FieldOfStudy> getAllFieldsOfStudy() throws Exception;

  DesignParadigm createDesignParadigm(String paradigmName) throws Exception;

  DesignParadigm createDesignParadigm(String paradigmName, String description) throws Exception;

  Optional<FieldOfStudy> getFieldByName(String name) throws Exception;

  Optional<DesignParadigm> getParadigmByName(String name) throws Exception;

  FieldOfStudy createFieldOfStudy(String fieldName) throws Exception;

  FieldOfStudy createFieldOfStudy(String fieldName, String fieldDescription) throws Exception;

  Algorithm createAlgorithm(
      String name, String complexity, DesignParadigm designParadigm, FieldOfStudy fieldOfStudy)
      throws Exception;

  List<Algorithm> searchAlgorithm(
      String text, String text1, DesignParadigm selectedItem, FieldOfStudy selectedItem1)
      throws Exception;

  void updateAlgorithmName(Algorithm algorithm, String newName) throws Exception;

  void updateAlgorithmComplexity(Algorithm rowValue, String newComplexity) throws Exception;

  DesignParadigm setParadigmName(DesignParadigm oldValue, String paradigm) throws Exception;

  FieldOfStudy setFieldName(FieldOfStudy newValue, String field) throws Exception;

  void setDesignParadigm(Algorithm algorithm, DesignParadigm paradigm) throws Exception;

  void setFieldOfStudy(Algorithm algorithm, FieldOfStudy field) throws Exception;

  void deleteAlgorithm(Algorithm algorithm) throws Exception;

  List<AreaOfUse> getAllAreas() throws Exception;

  AreaOfUse createAreaOfUse(String area) throws Exception;

  AreaOfUse createAreaOfUse(String area, String description) throws Exception;

  void deleteAreaOfUse(AreaOfUse areaOfUse) throws Exception;

  Optional<AreaOfUse> getAreaOfUse(String name) throws Exception;

  void createApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;

  List<Application> getApplicationsAlgorithm(Algorithm algorithm) throws Exception;

  List<Application> getApplicationsByArea(AreaOfUse areaOfUse) throws Exception;

  void deleteApplication(Algorithm algorithm, AreaOfUse areaOfUse) throws Exception;
}
