FROM jenkins

COPY plugins.txt /var/jenkins_home/plugins.txt
RUN /usr/local/bin/plugins.sh /var/jenkins_home/plugins.txt

# Adding default Jenkins Jobs
COPY jobs/1-github-seed-job.xml /usr/share/jenkins/ref/jobs/1-github-seed-job/config.xml
COPY jobs/2-job-dsl-seed-job.xml /usr/share/jenkins/ref/jobs/2-job-dsl-seed-job/config.xml
COPY jobs/3-conference-app-seed-job.xml /usr/share/jenkins/ref/jobs/3-conference-app-seed-job/config.xml
COPY jobs/4-selenium2-maven-test.xml /usr/share/jenkins/ref/jobs/4-selenium2-maven-test/config.xml
COPY jobs/6-conference-app-ci.xml /usr/share/jenkins/ref/jobs/conference-app-1-ci/config.xml
COPY jobs/6-conference-app-sonar-analysis.xml /usr/share/jenkins/ref/jobs/conference-app-2-sonar-analysis/config.xml
COPY jobs/7-conference-app-monitoring-ci.xml /usr/share/jenkins/ref/jobs/conference-app-monitoring-1-ci/config.xml
COPY jobs/7-conference-app-monitoring-sonar-analysis.xml /usr/share/jenkins/ref/jobs/conference-app-monitoring-2-sonar-analysis/config.xml

############################################
# Configure Jenkins
############################################
# Jenkins settings
COPY config/config.xml /usr/share/jenkins/ref/config.xml

# Jenkins Settings, i.e. Maven, Groovy, ...
COPY config/hudson.tasks.Maven.xml /usr/share/jenkins/ref/hudson.tasks.Maven.xml
COPY config/hudson.plugins.groovy.Groovy.xml /usr/share/jenkins/ref/hudson.plugins.groovy.Groovy.xml
COPY config/maven-global-settings-files.xml /usr/share/jenkins/ref/maven-global-settings-files.xml

# SSH Keys & Credentials
COPY config/credentials.xml /usr/share/jenkins/ref/credentials.xml
COPY config/ssh-keys/cd-demo /usr/share/jenkins/ref/.ssh/id_rsa
COPY config/ssh-keys/cd-demo.pub /usr/share/jenkins/ref/.ssh/id_rsa.pub
