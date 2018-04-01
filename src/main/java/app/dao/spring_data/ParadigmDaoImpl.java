package app.dao.spring_data;

import app.dao.interf.ParadigmDao;
import app.model.DesignParadigm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParadigmDaoImpl extends ParadigmDao, JpaRepository<DesignParadigm, Integer> {
  @Override
  void create(DesignParadigm paradigm) throws Exception;

  @Override
  List<DesignParadigm> getAll() throws Exception;

  @Override
  Optional<DesignParadigm> findById(int id);

  @Override
  Optional<DesignParadigm> findByParadigm(String paradigm) throws Exception;

  @Override
  void deleteDesignParadigmById(int id) throws Exception;

  @Override
  @Modifying
  @Query(
    value =
        "UPDATE DesignParadigm paradigm SET paradigm.paradigm = :newName WHERE paradigm.id = :id"
  )
  void updateDesignParadigm(@Param("newName") String newName, @Param("id") int id) throws Exception;
}
