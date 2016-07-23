// Example from https://jenkinsci.github.io/job-dsl-plugin/

def giturl = 'https://github.com/quidryan/aws-sdk-test.git'
for(i in 0..10) {
  job("Job-DSL-Tutorial-1-Test-${i}") {
    scm {
      git {
        remote {
          url(giturl)
        }
        extensions {
          cleanAfterCheckout()
        }
      }
    }
    triggers {
        scm('15/H * * * *')
        githubPush()
    }
    steps {
      maven {
        goals('clean')
        goals('verify')
        goals("test -Dtest.suite=${i}")
        mavenOpts('-Xms256m')
        mavenOpts('-Xmx512m')
        properties(skipTests: true)
        mavenInstallation('Maven 3.3.3')
      }
    }
  }
}

listView('Seed Jobs') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/.*seed-job.*/)
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

listView('Job DSL Tutorial') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/.*Tutorial.*/)
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
