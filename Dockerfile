FROM clojure
MAINTAINER Joel Gregory <joel@osden.io>
ENV PORT 8080
RUN ls .
ADD ./buildoutput/catalogue.jar /srv/catalogue.jar
EXPOSE 8080
CMD ["java", "-jar", "/srv/catalogue.jar"]
