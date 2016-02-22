FROM clojure
MAINTAINER Joel Gregory <joel@osden.io>
RUN ls -a
ADD ./buildoutput/catalogue.jar /srv/catalogue.jar
EXPOSE 8080
CMD ["java", "-jar", "/srv/catalogue.jar"]
