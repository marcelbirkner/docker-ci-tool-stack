#
# Creates a docker container with SonarQube
#

FROM centos:6

MAINTAINER Marcel Birkner <marcel.birkner@codecentric.de>

USER root

# Update the system
RUN yum -y update; \
    yum clean all

##########################################################
# Install Java JDK other useful cmdline utilities
##########################################################
RUN yum -y install java-1.7.0-openjdk-devel \
    which \
		telnet \
		unzip \
		openssh-server \
		sudo \
		openssh-clients \
		iputils \
		iproute \
		httpd-tools \
		wget
RUN yum clean all

############################################
# Install PostgreSQL
############################################

# RUN yum -y localinstall http://yum.postgresql.org/9.3/redhat/rhel-6-x86_64/pgdg-centos93-9.3-1.noarch.rpm
# RUN yum -y install postgresql93-server \
#     postgresql93
# RUN /etc/init.d/postgresql-9.3 initdb
# RUN sed -i "s/peer/trust/g" /var/lib/pgsql/9.3/data/pg_hba.conf
# RUN sed -i "s/ident/md5/g" /var/lib/pgsql/9.3/data/pg_hba.conf
# RUN echo "host    all             all             0.0.0.0/0               md5" >> /var/lib/pgsql/9.3/data/pg_hba.conf

# RUN echo "listen_addresses='*'" >> /var/lib/pgsql/9.3/data/postgresql.conf
# RUN /etc/init.d/postgresql-9.3 start && \
#	psql -U postgres --command "CREATE USER sonar WITH SUPERUSER PASSWORD 'sonar';" && \
#	runuser -l postgres -c "createdb -O sonar sonar"

############################################
# Install SonarQube
############################################

RUN wget -O /tmp/sonarqube-5.1.zip http://dist.sonar.codehaus.org/sonarqube-5.1.zip
RUN unzip -d /opt /tmp/sonarqube-5.1.zip
RUN mv /opt/sonarqube-5.1 /opt/sonarqube
#RUN sed -i \
#        -e "s#^sonar.jdbc.url.*#sonar.jdbc.url: jdbc:postgresql://127.0.0.1:5432/sonar#" \
#        /opt/sonarqube/conf/sonar.properties

############################################
# Install Supervisor
############################################

RUN yum -y localinstall http://download.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
RUN yum -y install supervisor

# RUN echo "[program:postgresql]" >> /etc/supervisord.conf
# RUN echo "command=/etc/init.d/postgresql-9.3 start" >> /etc/supervisord.conf
# RUN echo "priority=10" >> /etc/supervisord.conf

RUN echo -e "\n\n[program:sonar]" >> /etc/supervisord.conf
RUN echo "command=/opt/sonarqube/bin/linux-x86-64/sonar.sh console" >> /etc/supervisord.conf
RUN echo "priority=20" >> /etc/supervisord.conf

############################################
# Start Sonar
############################################

CMD ["/usr/bin/supervisord", "-n"]

EXPOSE 9000 5432
