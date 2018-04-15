package com.algorithm.manager.dao.spring_data;

import com.algorithm.manager.model.DesignParadigm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParadigmDaoImpl extends JpaRepository<DesignParadigm, Integer> {
  void persist(DesignParadigm paradigm) throws Exception;

  List<DesignParadigm> getAllParadigms() throws Exception;

  Optional<DesignParadigm> getParadigmById(int id);

  Optional<DesignParadigm> getParadigmByParadigm(String paradigm) throws Exception;

  void deleteDesignParadigm(DesignParadigm paradigm) throws Exception;

  @Modifying
  @Query(
    value =
        "UPDATE DesignParadigm paradigm SET paradigm.paradigm = :newName WHERE paradigm.id = :id"
  )
  void setParadigm(@Param("id") int id, @Param("newName") String newName) throws Exception;
}
