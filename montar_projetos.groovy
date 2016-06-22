job('DSL-Tutorial-1-Test') {
    scm {
        git('https://github.com/dharlanoliveira/spring-boot-rest-example.git')
    }
    triggers {
        scm('H/15 * * * *')
    }
    steps {
        maven('-e clean install')
    }
}