# est &nbsp;[![CircleCI](https://circleci.com/gh/pine/est.svg?style=shield&circle-token=5da684fe3eb45157e7b6069434a82bf37c95fa0f)](https://circleci.com/gh/pine/est)

## Getting started

```
$ ./gradlew :app:bootJar
```

## Deployment

```bash
# Setup environment variables
$ heroku config:set "JAVA_OPTS=-XX:+UseStringDeduplication"
$ heroku config:set "SPRING_PROFILES_ACTIVE=prod"
$ heroku config:set "TZ=Asia/Tokyo"

# Setup Redis
$ heroku addons:create heroku-redis:hobby-dev
$ heroku config | fgrep REDIS_URL
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe)
