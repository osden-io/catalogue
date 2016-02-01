FROM strongloop/node
MAINTAINER Joel Gregory <joel@osden.io>
ADD . /home/osden/catalogue
WORKDIR /home/osden/catalogue
RUN sudo npm install
CMD [ "slc", "run", "." ]