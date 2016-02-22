FROM clojure
MAINTAINER Joel Gregory <joel@osden.io>
ADD ./buildoutput/uberjar/catalogue-0.0.1-SNAPSHOT-standalone.jar /srv/catalogue.jar
EXPOSE 8080
CMD ["java", "-jar", "/srv/catalogue.jar"]
