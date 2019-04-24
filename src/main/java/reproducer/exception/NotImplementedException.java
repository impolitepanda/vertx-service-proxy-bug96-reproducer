package reproducer.exception;

/**
 * Represents a Not Implemented HTTP response and contains a root cause and a message to return
 */
public class NotImplementedException extends MyException {
    public NotImplementedException(String message) {
        super(501, message);
    }
}