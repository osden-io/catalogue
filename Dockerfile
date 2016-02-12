FROM node
MAINTAINER Joel Gregory <joel@osden.io>
ADD . /home/osden/catalogue
WORKDIR /home/osden/catalogue
RUN npm install
CMD [ "npm", "start" ]