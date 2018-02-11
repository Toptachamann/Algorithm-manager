package app.auxiliary;

public class QueryException extends Exception {
  public QueryException() {}

  public QueryException(String message) {
    super(message);
  }

  public QueryException(String message, Throwable cause) {
    super(message, cause);
  }
}
