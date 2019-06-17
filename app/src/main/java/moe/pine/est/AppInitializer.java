package moe.pine.est;

import moe.pine.heroku.addons.HerokuRedis;

public class AppInitializer {
    public static void run() {
        final HerokuRedis redis = HerokuRedis.get();
        if (redis != null) {
            System.setProperty("spring.redis.host", redis.getHost());
            System.setProperty("spring.redis.password", redis.getPassword());
            System.setProperty("spring.redis.port", Integer.toString(redis.getPort()));
        }
    }
}
