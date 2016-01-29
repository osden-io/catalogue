FROM strongloop/node
ADD . /home/osden/app
WORKDIR /home/osden/app
RUN sudo npm install
CMD [ "slc", "run", "." ]