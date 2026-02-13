package ggolist.refactor.global.exception.auth;

public class TokenMismatchException extends RuntimeException {
    public TokenMismatchException(String message) {
        super(message);
    }
}
