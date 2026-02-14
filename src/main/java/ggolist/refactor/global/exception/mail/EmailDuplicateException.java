package ggolist.refactor.global.exception.mail;

public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException(String message) {
        super(message);
    }
}
