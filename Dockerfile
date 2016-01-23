FROM java:8-jre
MAINTAINER Yeh-Yung <yyc1217@gmail.com>

RUN apt-get update
RUN apt-get upgrade -y

# install wkhtmltopdf
RUN apt-get install -y libfontenc1 libxfont1 xfonts-75dpi xfonts-base xfonts-encodings xfonts-utils gdebi
RUN wget https://bitbucket.org/wkhtmltopdf/wkhtmltopdf/downloads/wkhtmltox-0.13.0-alpha-7b36694_linux-jessie-amd64.deb
RUN gdebi --n wkhtmltox-0.13.0-alpha-7b36694_linux-jessie-amd64.deb


EXPOSE 8080
ADD build/libs/url2pdf-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
