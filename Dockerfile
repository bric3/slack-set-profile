FROM oracle/graalvm-ce:20.3.0-java11

RUN yum install -y -q xz \
  && curl -sL -o - https://github.com/upx/upx/releases/download/v3.96/upx-3.96-amd64_linux.tar.xz | tar xJ \
  && gu install native-image \
  && mkdir /src

VOLUME /src
WORKDIR /src

CMD ["bash"]

# Quickly run this dockerfile
# docker run -it --rm --mount=type=bind,source=$(pwd),target=/src  $(docker build -q -f Dockerfile .)