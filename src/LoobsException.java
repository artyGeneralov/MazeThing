// Location Out Of Bounds Exception
public class LoobsException extends Exception {

    // Default constructor
    public LoobsException() {
        super("Location Out Of Bounds");
    }

    // Constructor with a custom message
    public LoobsException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public LoobsException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public LoobsException(Throwable cause) {
        super("Location Out Of Bounds", cause);
    }
}
