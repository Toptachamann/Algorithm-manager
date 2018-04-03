package app.dao.interf;

import app.model.DesignParadigm;

import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  void persist(DesignParadigm paradigm) throws Exception;

  List<DesignParadigm> getAllParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id) throws Exception;

  Optional<DesignParadigm> getParadigmByParadigm(String paradigm) throws Exception;

  default boolean containsParadigm(String name) throws Exception {
    return getParadigmByParadigm(name).isPresent();
  }

  default boolean containsParadigm(DesignParadigm paradigm) throws Exception {
    return getParadigmById(paradigm.getId()).isPresent();
  }

  void delete(DesignParadigm paradigm) throws Exception;

  void setParadigm(DesignParadigm paradigm, String newName) throws Exception;
}
