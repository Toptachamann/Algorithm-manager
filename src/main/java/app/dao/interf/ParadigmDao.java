package app.dao.interf;

import app.model.DesignParadigm;

import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  DesignParadigm createParadigm(String paradigm) throws Exception;

  DesignParadigm createParadigm(String paradigm, String description) throws Exception;

  List<DesignParadigm> getAllDesignParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id) throws Exception;

  Optional<DesignParadigm> getParadigmByName(String name) throws Exception;

  default boolean containsParadigm(String name) throws Exception {
    return getParadigmByName(name).isPresent();
  }

  default boolean containsParadigm(int id) throws Exception {
    return getParadigmById(id).isPresent();
  }

  void deleteParadigm(int id) throws Exception;

  void updateDesignParadigm(String newName, int id) throws Exception;
}
