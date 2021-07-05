FROM gradle:7-jdk11

WORKDIR /server
COPY . /server

RUN ["gradle", "build"]
# CMD ["sh", "entrypoint.sh"]
CMD ["gradle", "bootRun"]