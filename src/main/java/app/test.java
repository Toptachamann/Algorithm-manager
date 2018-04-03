package app;

import app.dao.hibernate.ParadigmDaoImpl;
import app.model.DesignParadigm;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.List;
import java.util.stream.Collectors;

public class test {
  public static void main(String[] args) throws ClassNotFoundException {
    System.out.println(HibernatePersistenceProvider.class);
    Class.forName("org.hibernate.jpa.HibernatePersistenceProvider");
    ParadigmDaoImpl dao = new ParadigmDaoImpl();
    List<DesignParadigm> paradigms = dao.getAllParadigms();
    System.out.println(
        paradigms.stream().map(DesignParadigm::getParadigm).collect(Collectors.joining(", ")));
  }
}
