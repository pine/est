# est &nbsp;[![CircleCI](https://circleci.com/gh/pine/est.svg?style=shield&circle-token=5da684fe3eb45157e7b6069434a82bf37c95fa0f)](https://circleci.com/gh/pine/est)

## Requirements

- JDK 11 or later
- Redis

## Getting started

```
$ ./gradlew :app:bootJar
```

## Development

```
$ java -version
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.2+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.2+9, mixed mode)
```

## Deployment
[Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) is required.

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
