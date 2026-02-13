package ggolist.refactor.global.auth.jwt;

public final class JwtUtils {

    private JwtUtils() {
    }

    public static String stripBearerPrefix(String token) {
        if (token == null) {
            return null;
        }

        if (token.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return token.substring(JwtConstants.TOKEN_PREFIX.length());
        }

        return token;
    }
}

