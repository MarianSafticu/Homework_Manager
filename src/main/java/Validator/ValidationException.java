package Validator;

public class ValidationException extends RuntimeException{

    public ValidationException(String err){
        super(err);
    }

}
