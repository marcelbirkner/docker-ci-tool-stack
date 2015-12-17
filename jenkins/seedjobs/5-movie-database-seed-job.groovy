def giturl = 'https://github.com/tobiasflohre/movie-database'
job("movie-database-1-ci") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
    stringParam("REPOSITORY_URL", "http://\${DOCKERCITOOLSTACK_NEXUS_1_PORT_8081_TCP_ADDR}:\${DOCKERCITOOLSTACK_NEXUS_1_PORT_8081_TCP_PORT}/nexus/content/repositories/releases/", "Nexus Release Repository URL")
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
    scm('10/H * * * *')
    githubPush()
  }
  steps {
    maven {
        goals('clean versions:set -DnewVersion=DEV-\${BUILD_NUMBER}')
        mavenInstallation('Maven 3.3.3')
        rootPOM('pom.xml')
        mavenOpts('-Xms512m -Xmx1024m')
        providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('clean deploy -DaltDeploymentRepository=nexus-release-repository::default::\${REPOSITORY_URL}')
      mavenInstallation('Maven 3.3.3')
      rootPOM('pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
    archiveJunit('**/target/surefire-reports/*.xml')
    publishCloneWorkspace('**', '', 'Any', 'TAR', true, null)
    downstreamParameterized {
      trigger('movie-database-2-sonar') {
        currentBuild()
      }
    }
  }
}
job("movie-database-2-sonar") {
  parameters {
    stringParam("BRANCH", "master", "Define TAG or BRANCH to build from")
  }
  scm {
    cloneWorkspace("movie-database-1-ci")
  }
  wrappers {
    colorizeOutput()
    preBuildCleanup()
  }
  steps {
    maven {
      goals('org.jacoco:jacoco-maven-plugin:0.7.4.201502262128:prepare-agent install -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
    maven {
      goals('sonar:sonar -Psonar')
      mavenInstallation('Maven 3.3.3')
      rootPOM('pom.xml')
      mavenOpts('-Xms512m -Xmx1024m')
      providedGlobalSettings('MyGlobalSettings')
    }
  }
  publishers {
    chucknorris()
  }
}
listView('Movie Database') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/movie-database-.*/)
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
