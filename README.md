# est
[![CircleCI](https://circleci.com/gh/pine/est.svg?style=shield&circle-token=5da684fe3eb45157e7b6069434a82bf37c95fa0f)](https://circleci.com/gh/pine/est)
[![codecov](https://codecov.io/gh/pine/est/branch/master/graph/badge.svg)](https://codecov.io/gh/pine/est)

:mailbox: The cute email notification system V2 for Slack.

![](images/resized.jpg)<br>
<sup><sup>&copy; milla74/123RF.COM</sup></sup>
<br>
<br>

## Requirements

- JDK 11 or later
- Redis

## Getting started

```
$ ./gradlew :app:bootRun
```

# Development
##  JDK
JDK 11 is required.

Please install Java by Homebrew if you use macOS.

```
$ brew tap adoptopenjdk/openjdk
$ brew cask install adoptopenjdk11

$ java -version
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.2+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.2+9, mixed mode)
```

## Deployment
[Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) is required.

```bash
# Create a new Heroku app
$ heroku apps:create your-app

# Setup environment variables
$ heroku config:set "JAVA_OPTS=-XX:+UseStringDeduplication"
$ heroku config:set "SPRING_PROFILES_ACTIVE=prod"
$ heroku config:set "TZ=Asia/Tokyo"

# Setup Redis
$ heroku addons:create heroku-redis:hobby-dev

# Deploy JAR file
$ ./gradlew :app:bootJar
$ heroku plugins:install java
$ heroku deploy:jar --jar app/build/libs/app.jar --jdk 11
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe)
