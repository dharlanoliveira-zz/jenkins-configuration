import groovy.json.JsonSlurper

def conf = carregarConfiguracaoArquivo('https://raw.githubusercontent.com/dharlanoliveira/jenkins-configuration/master/projetos.json');

conf.each {
    def obj = it;
    println "Montando projeto " + obj.id
    criarPasta(obj.folder)
    criarProjetoPipeline(obj);
}

def carregarConfiguracaoArquivo(String caminho){
    def file = new URL(caminho).openStream()

    def jsonSlurper = new JsonSlurper()
    return jsonSlurper.parse(file)
}

def criarPasta (String pasta) {
    if(pasta.isEmpty()){
        return;
    }
    String[] folders = pasta.split("/")
    String caminhoPasta = "";
    for(int i = 0 ; i < folders.length; ++i){
        caminhoPasta = caminhoPasta.concat(folders[i] + "/")
        println "Criando pasta " + caminhoPasta
        folder(caminhoPasta.substring(0,caminhoPasta.length() - 1));
    }
}

def criarProjetoPipeline (obj){
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
