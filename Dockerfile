FROM strongloop/node
MAINTAINER Joel Gregory <joel@osden.io>
ADD . /home/osden/app
WORKDIR /home/osden/app
RUN sudo npm install
CMD [ "slc", "run", "." ]