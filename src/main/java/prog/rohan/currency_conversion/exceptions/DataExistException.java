package prog.rohan.currency_conversion.exceptions;

public class DataExistException extends RuntimeException{
    public DataExistException(String message){
        super(message);
    }
}
