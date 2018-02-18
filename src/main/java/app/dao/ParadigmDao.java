package app.dao;

import app.model.DesignParadigm;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ParadigmDao {
  DesignParadigm createParadigm(String paradigm) throws SQLException;

  DesignParadigm createParadigm(String paradigm, String description) throws SQLException;

  List<DesignParadigm> getAllDesignParadigms() throws SQLException;

  Optional<DesignParadigm> getParadigmById(int id) throws SQLException;

  Optional<DesignParadigm> getParadigmByName(String name) throws SQLException;

  void updateDesignParadigm(String newName, int id) throws SQLException;
}
