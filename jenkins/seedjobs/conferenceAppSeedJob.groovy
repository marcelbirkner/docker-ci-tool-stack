def gitUrl = 'https://github.com/codecentric/conference-app'

createCiJob("conference-app", gitUrl, "app/pom.xml")
createSonarJob("conference-app", gitUrl, "app/pom.xml")

createCiJob("conference-app-monitoring", gitUrl, "monitoring/pom.xml")
createSonarJob("conference-app-monitoring", gitUrl, "monitoring/pom.xml")

def createCiJob(def jobName, def gitUrl, def pomFile) {
  job("${jobName}-1-ci") {
    parameters {
      stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
      stringParam("REPOSITORY_URL", "http://nexus:8081/nexus/content/repositories/releases/", "Nexus Release Repository URL")
    }
    scm {
      git {
        remote {
          url(gitUrl)
        }
        createTag(false)
        clean()
      }
    }
    wrappers {
      colorizeOutput()
      preBuildCleanup()
    }
    triggers {
      scm('30/H * * * *')
      githubPush()
    }
    steps {
      maven {
          goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
          mavenInstallation('Maven 3.3.3')
          rootPOM( pomFile )
          mavenOpts('-Xms512m -Xmx1024m')
          providedGlobalSettings('MyGlobalSettings')
      }
      maven {
        goals('clean deploy')
        mavenInstallation('Maven 3.3.3')
        rootPOM(pomFile)
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
      }
    }
    publishers {
      chucknorris()
      archiveJunit('**/target/surefire-reports/*.xml')
      publishCloneWorkspace('**', '', 'Any', 'TAR', true, null)
      downstreamParameterized {
        trigger("${jobName}-2-sonar") {
          currentBuild()
        }
      }
    }
  }
}

def createSonarJob(def jobName, def gitUrl, def pomFile) {
  job("${jobName}-2-sonar") {
    parameters {
      stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
    }
    scm {
      cloneWorkspace("${jobName}-1-ci")
    }
    wrappers {
      colorizeOutput()
      preBuildCleanup()
    }
    steps {
      maven {
        goals('org.jacoco:jacoco-maven-plugin:0.7.4.201502262128:prepare-agent install -Psonar')
        mavenInstallation('Maven 3.3.3')
        rootPOM(pomFile)
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
      }
      maven {
        goals('sonar:sonar -Psonar')
        mavenInstallation('Maven 3.3.3')
        rootPOM(pomFile)
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
      }
    }
    publishers {
      chucknorris()
    }
  }
}

listView('Conference App') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/conference-app-.*/)
    }
    columns {
        status()
        buildButton()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
    }
}

def conferenceAppGitUrl="https://github.com/codecentric/conference-app"
createDockerJob("conference-app-build-container", "cd app && sudo /usr/bin/docker build -t conferenceapp .", conferenceAppGitUrl)
createDockerJob("conference-app-start-container", "sudo /usr/bin/docker run -d --name conferenceapp -p=48080:8080 conferenceapp", conferenceAppGitUrl)
createDockerJob("conference-app-stop-container", 'sudo /usr/bin/docker stop \$(sudo /usr/bin/docker ps -a -q --filter="name=conferenceapp") && sudo /usr/bin/docker rm \$(sudo /usr/bin/docker ps -a -q --filter="name=conferenceapp")', " ")

def createDockerJob(def jobName, def shellCommand, def gitRepository) {

  println "############################################################################################################"
  println "Creating Docker Job ${jobName} for gitRepository=${gitRepository}"
  println "############################################################################################################"

  job(jobName) {
    logRotator {
        numToKeep(10)
    }
    if( "${gitRepository}".size() > 0 ) {
      if( "${jobName}".contains("conference-app") ) {
        scm {
          cloneWorkspace("conference-app-1-ci")
        }
      } else {
        scm {
          git {
            remote {
              url(gitRepository)
            }
            createTag(false)
            clean()
          }
        }
      }
    }
    steps {
      steps {
        shell(shellCommand)
      }
    }
    publishers {
      chucknorris()
    }
  }
}


listView('Conference App Docker') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/docker-conference-app-.*/)
    }
    columns {
        status()
        buildButton()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
    }
}
