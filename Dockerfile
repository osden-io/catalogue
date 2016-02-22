FROM clojure
MAINTAINER Joel Gregory <joel@osden.io>
ENV PORT 8080
ADD . /home/osden/catalogue
WORKDIR /home/osden/catalogue
EXPOSE 8080
CMD ["java", "-jar", "/home/osden/catalogue/target/uberjar/catalogue.jar"]
