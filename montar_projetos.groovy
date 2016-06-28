import groovy.json.JsonSlurper

def file = new URL('https://raw.githubusercontent.com/dharlanoliveira/jenkins-configuration/master/projetos.json').openStream()

def jsonSlurper = new JsonSlurper()
def conf = jsonSlurper.parse(file)

conf.each {
    def obj = it;
    println "Montando projeto " + obj.id
    criarPasta(obj.folder)
    pipelineJob(obj.folder + "/" + obj.id) {
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
                        node / wipeOutWorkspace('true')
                        node / clean('true')
                    }
                }
            }
        }
    }
}

void criarPasta (String pasta) {
    if(it.isEmpty()){
        return;
    }
    String[] folders = pasta.split("/")
    String caminhoPasta = "";
    for(int i = 0 ; i < folders.length; ++i){
        caminhoPasta.concat(folders[i] + "/")
        println "Criando pasta " + caminhoPasta
        folder(caminhoPasta);
    }
}

