package exceptions;

public class InsufficientFuelException extends Exception {
   public InsufficientFuelException(String error){
      super(error);
   }
}
