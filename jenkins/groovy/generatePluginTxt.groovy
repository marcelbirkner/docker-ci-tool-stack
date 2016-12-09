/**
 * This script prints a sorted list of all installed plugins incl. the version.
 * The output can be used to update the plugins.txt
 *
 * 1. Update all Plugins via Jenkins UI or upgradeAllPlugins.groovy script
 * 2. Run this script using the Script Console (via http://<jenkins-url:port>/script)
 * 3. Replace plugins.txt with output from step 2
 */

import jenkins.model.Jenkins;

pm = Jenkins.instance.pluginManager

def pluginList = []
pm.plugins.each { plugin ->
  pluginList.add("${plugin.shortName}:${plugin.version}")
}

Collections.sort(pluginList)
pluginList.each {
  println it
}
