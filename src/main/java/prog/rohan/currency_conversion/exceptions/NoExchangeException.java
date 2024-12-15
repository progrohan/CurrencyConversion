package prog.rohan.currency_conversion.exceptions;

public class NoExchangeException extends RuntimeException{
    public NoExchangeException(String message){
        super(message);
    }
}
