# est &nbsp;[![CircleCI](https://circleci.com/gh/pine/est.svg?style=shield&circle-token=5da684fe3eb45157e7b6069434a82bf37c95fa0f)](https://circleci.com/gh/pine/est)

## Getting started

```
$ ./gradlew clean :app:thorntail-package
$ java -jar app/build/libs/app-thorntail.jar
```

## Deployment

```bash
$ heroku config:set "JAVA_OPTS=-XX:+UseStringDeduplication"
$ heroku config:set "LANG=en_US.UTF-8"
$ heroku config:set "TZ=Asia/Tokyo"
```

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe)
