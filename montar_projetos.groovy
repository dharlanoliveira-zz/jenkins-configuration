import groovy.json.JsonSlurper

def file = new URL('https://raw.githubusercontent.com/dharlanoliveira/jenkins-configuration/master/projetos.json').openStream()

def jsonSlurper = new JsonSlurper()
def conf = jsonSlurper.parse(file)

conf.each {
    def obj = it;
    println "Montando projeto " + obj.id
    pipelineJob(obj.id) {
        displayName(obj.nome)
        description("Job do projeto " + obj.nome);

        compressBuildLog()

        logRotator {
            numToKeep 10
        }

        triggers {
            scm 'H/5 * * * *'
        }

        //label('swarm')

        definition {
            cpsScm {
                scm {
                    git(obj.url) { node ->
                        node / authorOrCommitter('true')
                        node / gitConfigName('Jenkins TCU')
                        node / gitConfigEmail('jekins@tcu.gov.br')

                        extensions {
                            wipeOutWorkspace()
                            cleanAfterCheckout()
                         }
                    }
                }
            }
        }
    }
}

