FROM gradle:jdk8

ENV TZ=America/New_York
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install -y curl webp
RUN apt-get install -y build-essential

RUN curl -sfLO https://download.imagemagick.org/ImageMagick/download/ImageMagick.tar.gz \
  && tar -xzf ImageMagick.tar.gz \
  && cd ImageMagick* \
  && ./configure --prefix /usr \
  && make install \
  && ldconfig /usr/lib \
  && cd .. \
  && rm -rf  ImageMagick*

