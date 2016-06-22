println "To dentro ######################"

pipelineJob("spring-boot-sample") {
    displayName('Pipeline do Spring Boot Sample')

    compressBuildLog()

    logRotator {
        numToKeep 10
    }

    triggers {
        scm 'H/5 * * * *'
    }

    label('slave')

     definition {
        cpsScm {
            scm {
                git('https://github.com/dharlanoliveira/spring-boot-rest-example.git')
            }
        }
    }
}