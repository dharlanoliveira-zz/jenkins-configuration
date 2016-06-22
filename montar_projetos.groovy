import groovy.json.JsonSlurper

println "#######################################"
println new File(".").getAbsolutePath()

def jsonSlurper = new JsonSlurper()
def conf = jsonSlurper.parseText(new File('./projetos.json').getText('UTF-8'))

conf.each {
    println "Montando projeto " + it.id
    pipelineJob(it.id) {
        displayName(it.descricao)

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

