package app.auxiliary;

public class LogicException extends Exception {
  public LogicException() {
    super();
  }

  public LogicException(String message) {
    super(message);
  }

  public LogicException(String message, Throwable cause) {
    super(message, cause);
  }
}
