FROM java:8-jre
MAINTAINER Yeh-Yung <yyc1217@gmail.com>

RUN apt-get update
RUN apt-get upgrade -y

# install wkhtmltopdf
RUN apt-get install -y libfontenc1 libxfont1 xfonts-75dpi xfonts-base xfonts-encodings xfonts-utils xz-utils
RUN wget http://download.gna.org/wkhtmltopdf/0.12/0.12.3/wkhtmltox-0.12.3_linux-generic-amd64.tar.xz
RUN tar -xf wkhtmltox-0.12.3_linux-generic-amd64.tar.xz
RUN cp wkhtmltox/bin/wkhtmltopdf /usr/local/bin/wkhtmltopdf

# This is for tranditional chinese font rendering, ignore this line if you don't need this.
RUN apt-get install -y ttf-wqy-microhei

EXPOSE 8080
ADD build/libs/url2pdf.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
