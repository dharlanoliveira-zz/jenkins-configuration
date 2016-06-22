println "To dentro ######################"

pipelineJob("spring-boot-sample") {
    displayName('Build de exemplo do spring boot sample')

    logRotator {
        numToKeep 10
    }

    triggers {
        scm 'H/5 * * * *'
    }

     definition {
        cpsScm {
            scm {
                git('https://github.com/dharlanoliveira/spring-boot-rest-example.git')
            }
        }
    }
}