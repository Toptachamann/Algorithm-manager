package app.dao.interf;

import app.model.DesignParadigm;

import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  void create(DesignParadigm paradigm) throws Exception;

  List<DesignParadigm> getAll() throws Exception;

  Optional<DesignParadigm> findById(int id) throws Exception;

  Optional<DesignParadigm> findByParadigm(String paradigm) throws Exception;

  default boolean containsParadigm(String name) throws Exception {
    return findByParadigm(name).isPresent();
  }

  default boolean containsParadigm(int id) throws Exception {
    return findById(id).isPresent();
  }

  void deleteDesignParadigmById(int id) throws Exception;

  void updateDesignParadigm(String newName, int id) throws Exception;
}
