FROM clojure
MAINTAINER Joel Gregory <joel@osden.io>
ENV PORT 8080
ADD ./buildoutput/catalogue.jar /srv/catalogue.jar
RUN ls ./buildoutput
EXPOSE 8080
CMD ["java", "-jar", "/srv/catalogue.jar"]
