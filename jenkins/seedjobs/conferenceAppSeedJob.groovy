def giturl = 'https://github.com/codecentric/conference-app'
job("conference-app-1-ci") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
    stringParam("REPOSITORY_URL", "http://nexus:8081/nexus/content/repositories/releases/", "Nexus Release Repository URL")
  }
  scm {
    git {
      remote {
        url(giturl)
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
        rootPOM('app/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean deploy')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
    archiveJunit('**/target/surefire-reports/*.xml')
    publishCloneWorkspace('**', '', 'Any', 'TAR', true, null)
    downstreamParameterized {
      trigger('conference-app-2-sonar') {
        currentBuild()
      }
    }
  }
}
job("conference-app-2-sonar") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
  scm {
    cloneWorkspace("conference-app-1-ci")
  }
  wrappers {
    colorizeOutput()
    preBuildCleanup()
  }
  steps {
    maven {
      goals('org.jacoco:jacoco-maven-plugin:0.7.4.201502262128:prepare-agent install -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('sonar:sonar -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('app/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
  }
}
job("conference-app-monitoring-1-ci") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
    stringParam("REPOSITORY_URL", "http://nexus:8081/nexus/content/repositories/releases/", "Nexus Release Repository URL")
  }
  scm {
    git {
      remote {
        url(giturl)
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
        rootPOM('monitoring/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean deploy -DaltDeploymentRepository=nexus-release-repository::default::$REPOSITORY_URL')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
    publishCloneWorkspace('**', '', 'Any', 'TAR', true, null)
    downstreamParameterized {
      trigger('conference-app-monitoring-2-sonar') {
        currentBuild()
      }
    }
  }
}
job("conference-app-monitoring-2-sonar") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
  scm {
    cloneWorkspace("conference-app-monitoring-1-ci")
  }
  wrappers {
    colorizeOutput()
    preBuildCleanup()
  }
  steps {
    maven {
        goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
        mavenInstallation('Maven 3.3.3')
        rootPOM('monitoring/pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('org.jacoco:jacoco-maven-plugin:0.7.4.201502262128:prepare-agent install -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('sonar:sonar -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('monitoring/pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
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
