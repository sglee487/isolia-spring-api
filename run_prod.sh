# after change yml profile active to prod:
gradle clean build
java -jar build/libs/isolia_api-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=prod
