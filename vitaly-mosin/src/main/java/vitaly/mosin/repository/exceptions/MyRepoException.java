package vitaly.mosin.repository.exceptions;

public class MyRepoException extends Exception {
    private ExceptionResponseCode code;
    private String message;


    public MyRepoException(ExceptionResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }


    public ExceptionResponseCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

