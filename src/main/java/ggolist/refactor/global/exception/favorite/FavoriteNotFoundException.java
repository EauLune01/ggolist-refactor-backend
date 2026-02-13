package ggolist.refactor.global.exception.favorite;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
