package app.dao;

import app.auxiliary.Connector;
import app.model.FieldOfStudy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FieldDaoImpl implements FieldDao{
  private Connection connection;

  private PreparedStatement insertFieldByName;
  private PreparedStatement insertField;
  private PreparedStatement allFieldsOfStudy;


  public FieldDaoImpl() throws SQLException {
    connection = Connector.getConnection();
    allFieldsOfStudy = connection.prepareStatement("SELECT * FROM field_of_study");
    insertFieldByName = connection.prepareStatement("INSERT INTO field_of_study (field) VALUE (?)");
    insertField =
        connection.prepareStatement("INSERT INTO field_of_study (field, description) VALUE (?, ?)");
  }

  @Override
  public void insertFieldOfStudy(String fieldOfStudy) throws SQLException {
    insertFieldByName.setString(1, fieldOfStudy);
    insertFieldByName.executeUpdate();
  }

  @Override
  public void insertFieldOfStudy(String fieldOfStudy, String description) throws SQLException {
    insertField.setString(1, fieldOfStudy);
    insertField.setString(2, description);
    insertField.executeUpdate();
  }

  @Override
  public List<FieldOfStudy> getAllFieldsOfStudy() throws SQLException {
    ResultSet result = allFieldsOfStudy.executeQuery();
    List<FieldOfStudy> fields = new ArrayList<>();
    while (result.next()) {
      fields.add(new FieldOfStudy(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return fields;
  }

  @Override
  public FieldOfStudy getFieldByName(String name) throws SQLException {
    Statement query = connection.createStatement();
    ResultSet set = query.executeQuery("SELECT * FROM field_of_study WHERE field = '" + name + "'");
    if (set.next()) {
      return new FieldOfStudy(set.getInt(1), set.getString(2), set.getString(3));
    } else {
      return null;
    }
  }
}
