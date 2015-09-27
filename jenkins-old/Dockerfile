#
# Creates a docker container with Nexus Artifact Repository
#

FROM centos:6

MAINTAINER Marcel Birkner <marcel.birkner@codecentric.de>

USER root
# Update the system
# RUN yum -y update;

##########################################################
# Install Java JDK, SSH and other useful cmdline utilities
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
    wget \
    tar; \
    yum clean all
ENV JAVA_HOME /usr/lib/jvm/jre

############################################
# Install Jenkins
############################################
RUN wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat/jenkins.repo
RUN rpm --import http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key
RUN yum -y install jenkins

RUN cp /etc/sysconfig/jenkins /etc/sysconfig/jenkins.save
RUN sed -i 's/^JENKINS_ARGS=.*/JENKINS_ARGS="--prefix=\/jenkins"/' /etc/sysconfig/jenkins

############################################
# Install Git
############################################

RUN yum -y install git

############################################
# Configure Jenkins
############################################
# Jenkins settings
ADD config.xml /var/lib/jenkins/config.xml

# Jenkins Settings, i.e. Maven, Groovy, ...
ADD hudson.tasks.Maven.xml /var/lib/jenkins/hudson.tasks.Maven.xml
ADD hudson.plugins.groovy.Groovy.xml /var/lib/jenkins/hudson.plugins.groovy.Groovy.xml
ADD maven-global-settings-files.xml /var/lib/jenkins/maven-global-settings-files.xml

# SSH Keys & Credentials
ADD credentials.xml /var/lib/jenkins/credentials.xml
ADD ssh-keys/cd-demo /var/lib/jenkins/.ssh/id_rsa
ADD ssh-keys/cd-demo.pub /var/lib/jenkins/.ssh/id_rsa.pub

# Default Jenkins Jobs
ADD jobs/jersey-rest-server.xml /var/lib/jenkins/jobs/jersey-rest-server/config.xml
ADD jobs/1-github-seed-job.xml /var/lib/jenkins/jobs/1-github-seed-job/config.xml
ADD jobs/2-job-dsl-seed-job.xml /var/lib/jenkins/jobs/2-job-dsl-seed-job/config.xml

# Plugins
RUN mkdir -p /var/lib/jenkins/plugins
RUN wget -O /var/lib/jenkins/plugins/git.hpi http://updates.jenkins-ci.org/download/plugins/git/2.4.0/git.hpi
RUN wget -O /var/lib/jenkins/plugins/git-client.hpi http://updates.jenkins-ci.org/download/plugins/git-client/1.19.0/git-client.hpi
RUN wget -O /var/lib/jenkins/plugins/scm-api.hpi http://updates.jenkins-ci.org/download/plugins/scm-api/0.2/scm-api.hpi

# MB: custom plugins
RUN wget -O /var/lib/jenkins/plugins/rebuild.hpi http://updates.jenkins-ci.org/download/plugins/rebuild/1.25/rebuild.hpi
RUN wget -O /var/lib/jenkins/plugins/jobConfigHistory.hpi http://updates.jenkins-ci.org/download/plugins/jobConfigHistory/2.12/jobConfigHistory.hpi
RUN wget -O /var/lib/jenkins/plugins/plugin-usage-plugin.hpi http://updates.jenkins-ci.org/download/plugins/plugin-usage-plugin/0.3/plugin-usage-plugin.hpi
RUN wget -O /var/lib/jenkins/plugins/credentials.hpi http://updates.jenkins-ci.org/download/plugins/credentials/1.23/credentials.hpi
RUN wget -O /var/lib/jenkins/plugins/groovy.hpi http://updates.jenkins-ci.org/download/plugins/groovy/1.27/groovy.hpi
RUN wget -O /var/lib/jenkins/plugins/job-dsl.hpi http://updates.jenkins-ci.org/download/plugins/job-dsl/1.38/job-dsl.hpi
RUN wget -O /var/lib/jenkins/plugins/github-api.hpi http://updates.jenkins-ci.org/download/plugins/github-api/1.69/github-api.hpi
RUN wget -O /var/lib/jenkins/plugins/github.hpi http://updates.jenkins-ci.org/download/plugins/github/1.13.3/github.hpi

RUN chown -R jenkins:jenkins /var/lib/jenkins

############################################
# Install Maven & Groovy
############################################

RUN mkdir -p /usr/share/apache-maven-3.3.3/
RUN wget -O /usr/share/apache-maven.tar.gz http://mirror.netcologne.de/apache.org/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
RUN /bin/tar xvfz /usr/share/apache-maven.tar.gz -C /usr/share/
ADD settings.xml /usr/share/apache-maven-3.3.3/conf/
RUN chown -R jenkins:jenkins /usr/share/apache-maven-3.3.3/

RUN mkdir -p /usr/share/groovy-2.4.3/
RUN wget -O /usr/share/groovy-binary.zip http://dl.bintray.com/groovy/maven/groovy-binary-2.4.3.zip
RUN /usr/bin/unzip /usr/share/groovy-binary.zip -d /usr/share/
RUN chown -R jenkins:jenkins /usr/share/groovy-2.4.3/

############################################
# Install Supervisor
############################################

RUN wget -O /tmp/epel-release-6-8.noarch.rpm http://download.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
RUN rpm -ivh /tmp/epel-release-6-8.noarch.rpm
RUN yum -y install supervisor
RUN echo "[program:jenkins]" >> /etc/supervisord.conf
RUN echo "command=/etc/init.d/jenkins start" >> /etc/supervisord.conf

############################################
# Start Jenkins
############################################

CMD ["/usr/bin/supervisord", "-n"]

EXPOSE 8080
