package exceptions;

public class InvalidOperationException extends Exception {
    public InvalidOperationException(String error){
        super(error);
    }
}
