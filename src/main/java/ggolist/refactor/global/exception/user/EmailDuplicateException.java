package ggolist.refactor.global.exception.user;

public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException(String message) {
        super(message);
    }
}
