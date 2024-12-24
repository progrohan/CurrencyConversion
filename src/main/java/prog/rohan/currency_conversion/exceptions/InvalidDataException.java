package prog.rohan.currency_conversion.exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message){
        super(message);
    }
}
