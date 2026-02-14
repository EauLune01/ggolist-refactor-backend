package ggolist.refactor.auth.service;

public interface RedisMailConstants {
    String AUTH_PREFIX = "auth:email:";
    String VERIFIED_PREFIX = "verified:email:";
    long AUTH_TTL_MINUTE = 5;

    String REQUEST_COUNT_PREFIX = "email:count:";
    int MAX_REQUEST_COUNT = 5;
    long BLOCK_24_HOURS = 24;
}
