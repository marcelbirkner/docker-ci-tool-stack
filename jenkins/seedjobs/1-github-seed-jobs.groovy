def githubApi = new URL("https://api.github.com/users/marcelbirkner/repos")
def projects = new groovy.json.JsonSlurper().parse(githubApi.newReader())

projects.each {
  def jobName=it.name
  def githubName=it.full_name
  def gitUrl=it.ssh_url
  println "Creating Job ${jobName} for ${gitUrl}"

  job("GitHub-${jobName}") {
    logRotator(-1, 10)
    scm {
        github(githubName, 'master')
    }
    triggers {
        githubPush()
    }
  }
}

listView('MB GitHub Jobs') {
    description('')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex(/GitHub-.*/)
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
