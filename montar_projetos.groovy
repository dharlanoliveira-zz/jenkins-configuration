import groovy.json.JsonSlurper

def file = new URL('https://raw.githubusercontent.com/dharlanoliveira/jenkins-configuration/master/projetos.json').openStream()

def jsonSlurper = new JsonSlurper()
def conf = jsonSlurper.parse(file)

conf.each {
    println "Montando projeto " + it.id
    pipelineJob(it.id) {
        displayName(it.nome)

        compressBuildLog()

        logRotator {
            numToKeep 10
        }

        triggers {
            scm 'H/5 * * * *'
        }

        label('swarm')

         definition {
            cpsScm {
                scm {
                    git(it.url) {
                        wipeOutWorkspace()
                        cleanAfterCheckout()
                    }
                }
            }
        }
    }
}

