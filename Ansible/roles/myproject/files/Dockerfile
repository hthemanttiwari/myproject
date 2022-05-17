FROM debian:stretch

WORKDIR /opt/

ADD https://downloads.apache.org/tomcat/tomcat-8/v8.5.78/bin/apache-tomcat-8.5.78.tar.gz /opt/apache-tomcat-8.5.78.tar.gz \
    && https://download.java.net/java/ga/jdk11/openjdk-11_linux-x64_bin.tar.gz /opt/openjdk-11_linux-x64_bin.tar.gz
    https://download.java.net/java/ga/jdk11/openjdk-11_linux-x64_bin.tar.gz

RUN tar -xvzf apache-tomcat-8.5.78.tar.gz \
    && rm -rf /opt/apache-tomcat-8.5.78.tar.gz \
    && tar -xvzf openjdk-11_linux-x64_bin.tar.gz \
    && rm -rf /opt/openjdk-11_linux-x64_bin.tar.gz

ENV JAVA_HOME=/opt/jdk-11
ENV PATH $JAVA_HOME/bin:$PATH

COPY ./*.war /opt/apache-tomcat-8.5.78/webapps/myproject.war

EXPOSE 8080

CMD ["/opt/apache-tomcat-8.5.78/bin/catalina.sh" , "run"]
