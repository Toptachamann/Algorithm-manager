package app.dao.interf;

import app.model.DesignParadigm;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  DesignParadigm createParadigm(String paradigm) throws Exception;

  DesignParadigm createParadigm(String paradigm, String description) throws Exception;

  List<DesignParadigm> getAllDesignParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id) throws Exception;

  Optional<DesignParadigm> getParadigmByName(String name) throws Exception;

  void updateDesignParadigm(String newName, int id) throws Exception;
}
