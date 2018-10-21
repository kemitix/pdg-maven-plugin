final String publicRepo = 'https://github.com/kemitix/'
final String mvn = "mvn --batch-mode --update-snapshots --errors"
final String repo = "kemitix/pdg-maven-plugin"

pipeline {
    agent any
    stages {
        stage('Build & Test') {
            steps {
                withMaven(maven: 'maven', jdk: 'JDK 1.8') {
                    sh "${mvn} clean test"
                }
            }
        }
        stage('Quality Checks (PMD & Checkstyle)') {
            steps {
                withMaven(maven: 'maven', jdk: 'JDK 1.8') {
                    sh "${mvn} checkstyle:checkstyle pmd:pmd"
                    pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '', unHealthy: ''
                    checkstyle defaultEncoding: 'UTF-8', healthy: '', pattern: '', shouldDetectModules: true, unHealthy: '', useDeltaValues: true, usePreviousBuildAsReference: true, useStableBuildAsReference: true
                }
            }
        }
        stage('Record Coverage (all)') {
            steps {
                jacoco exclusionPattern: '**/*{Test|IT|Main|Application|Immutable}.class'
                withMaven(maven: 'maven', jdk: 'JDK 1.8') {
                    // Codacy
                    sh "${mvn} jacoco:report com.gavinmogan:codacy-maven-plugin:coverage " +
                        "-DcoverageReportFile=target/site/jacoco/jacoco.xml " +
                        "-DprojectToken=`$JENKINS_HOME/codacy/token` " +
                        "-DapiToken=`$JENKINS_HOME/codacy/apitoken` " +
                        "-Dcommit=`git rev-parse HEAD`"
                }
            }
        }
        stage('Record Coverage (master)') {
            when { branch 'master' }
            steps {
                script {
                    currentBuild.result = 'SUCCESS'
                }
                step([$class: 'MasterCoverageAction', scmVars: [GIT_URL: env.GIT_URL]])
            }
        }
        stage('Coverage to Github (Pull Request)') {
            when { allOf {not { branch 'master' }; expression { return env.CHANGE_ID != null }} }
            steps {
                script {
                    currentBuild.result = 'SUCCESS'
                }
                step([$class: 'CompareCoverageAction', scmVars: [GIT_URL: env.GIT_URL]])
            }
        }
        stage('Verify') {
            steps {
                withMaven(maven: 'maven', jdk: 'JDK 1.8') {
                    sh "${mvn} --activate-profiles verify verify"
                }
            }
        }
        stage('SonarQube (published)') {
            when { expression { isPublished(publicRepo) } }
            steps {
                withSonarQubeEnv('sonarqube') {
                    withMaven(maven: 'maven', jdk: 'JDK 1.8') {
                        sh "${mvn} org.sonarsource.scanner.maven:sonar-maven-plugin:3.4.0.905:sonar -Dsonar.pullrequst.base=master -Dsonar.pullrequest.branch=${env.GIT_BRANCH} -Dsonar.pullrequest.provider=GitHub -Dsonar.pullrequest.key=${env.GIT_BRANCH} -Dsonar.pullrequest.repository=${repo}"
                    }
                }
            }
        }
        stage('Deploy (published release branch)') {
            when {
                expression {
                    (isReleaseBranch() &&
                            isPublished(publicRepo) &&
                            notSnapshot())
                }
            }
            steps {
                withMaven(maven: 'maven', jdk: 'JDK 1.8') {
                    sh "${mvn} --activate-profiles release deploy"
                }
            }
        }
        // Apache Maven Plugin Tools Version 3.5.2 only supports up to Java 10
    }
}

private boolean isReleaseBranch() {
    return branchStartsWith('release/')
}

private boolean branchStartsWith(final String branchName) {
    startsWith(env.GIT_BRANCH, branchName)
}

private boolean isPublished(final String repo) {
    startsWith(env.GIT_URL, repo)
}

private static boolean startsWith(final String value, final String match) {
    value != null && value.startsWith(match)
}

private boolean notSnapshot() {
    return !(readMavenPom(file: 'pom.xml').version).contains("SNAPSHOT")
}
