#
# Creates a docker container with Nexus Artifact Repository
#

FROM centos:7

MAINTAINER Marcel Birkner <marcel.birkner@codecentric.de>

ENV SONATYPE_WORK /sonatype-work
ENV NEXUS_VERSION 2.14.1-01
ENV NEXUS_HOME /opt/sonatype/nexus/

RUN INSTALL_PKGS="java-1.8.0-openjdk.x86_64" && \
    yum -y --setopt=tsflags=nodocs install $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all  && \
    localedef -f UTF-8 -i en_US en_US.UTF-8

# Install packages necessary
RUN yum -y install unzip && yum clean all

ADD https://download.sonatype.com/nexus/oss/nexus-${NEXUS_VERSION}-bundle.zip nexus-${NEXUS_VERSION}-bundle.zip

# Extract Nexus
RUN set -x \
	&& unzip nexus-${NEXUS_VERSION}-bundle.zip \
  && mkdir -p ${NEXUS_HOME} \
	&& cp -r nexus-${NEXUS_VERSION}/* ${NEXUS_HOME} \
	&& rm -rf nexus-${NEXUS_VERSION}-bundle.tar.gz \
  && rm -rf nexus-${NEXUS_VERSION}

RUN groupadd -r nexus -g 3001 && \
    useradd -u 3001 -r -g nexus -m -d ${SONATYPE_WORK} -s /bin/bash -c "Nexus Run User" nexus

VOLUME ${SONATYPE_WORK}

EXPOSE 8081

WORKDIR /opt/sonatype/nexus

RUN INSTALL_PKGS="createrepo" && \
    yum -y --setopt=tsflags=nodocs install $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all

ENV CONTEXT_PATH /
ENV MAX_HEAP 768m
ENV MIN_HEAP 256m
ENV JAVA_OPTS -server -XX:MaxPermSize=192m -Djava.net.preferIPv4Stack=true
ENV LAUNCHER_CONF ./conf/jetty.xml ./conf/jetty-requestlog.xml
CMD java \
  -Dnexus-work=${SONATYPE_WORK} -Dnexus-webapp-context-path=${CONTEXT_PATH} \
  -Xms${MIN_HEAP} -Xmx${MAX_HEAP} \
  -cp 'conf/:lib/*' \
  ${JAVA_OPTS} \
  org.sonatype.nexus.bootstrap.Launcher ${LAUNCHER_CONF}
