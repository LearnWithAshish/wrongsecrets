 node {
    withCredentials([string(credentialsId: 'ARMORCODE_TOKEN', variable: 'c42d7e10-23a5-45f1-b36b-76708a89b900')]){
        sh '''
        i=0
        end=30
        while [ $i -lt $end ]
        do
            i=$(( $i + 1 ))
            curl --location --request POST 'https://qa.armorcode.ai/client/buildvalidation' --header 'Content-Type: application/json' --header "Authorization: Bearer c42d7e10-23a5-45f1-b36b-76708a89b900"  -d '{ "env": "Production", "product": "34651", "subProduct": "47709", "buildNumber": "'"$BUILD_NUMBER"'", "jobName":"'"$JOB_NAME"'","current":"'$i'" , "end":"'$end'"}' > result.json
            status=$(jq -r '.status' result.json)
            if [ $status = "HOLD" ]
            then
                echo "Armorcode SLA failed."
                sleep 10
                echo "Sleeping 10 seconds before trying again. You can temporarily release the build from Armorcode console"
            elif [ $status = "FAILED" ]
            then
                echo "Exiting with error code 1"
                exit 1
            else
                break
            fi
        done
        '''
    }
}    
pipeline {
    agent any
    stages {
    stage('Install Gitleaks') {
  steps {
    sh '''#!/bin/bash
        curl -LO https://github.com/zricethezav/gitleaks/releases/download/v7.6.0/gitleaks-linux-amd64 && chmod +x gitleaks-linux-amd64 && sudo mv gitleaks-linux-amd64 /usr/local/bin/gitleaks && gitleaks --version
    '''
  }
}
  stage('Run Gitleaks') {
  steps {
    dir('https://github.com/LearnWithAshish/vulnado.git') {
      sh '''#!/bin/bash
          sudo gitleaks detect -f json -r https://github.com/LearnWithAshish/vulnado.git -v --report=/home/ubuntu/gitleaks/gitleaks.json
          exit 0
         '''
        }
    }
  }
    stage('Checkout') {
        // Check out your Git repository
        steps {
        git 'https://github.com/LearnWithAshish/vulnado.git'
    }
    }
    stage('Dependency Check') {
        // Download the Dependency Check CLI
        steps {
        sh 'curl -LO https://github.com/jeremylong/DependencyCheck/releases/download/v8.2.1/dependency-check-8.2.1-release.zip'

        // Unzip the Dependency Check CLI
        sh 'rm -rf dependency-check || true'
        sh 'unzip -qq dependency-check-8.2.1-release.zip'

        // Run Dependency Check on the repository
        sh './dependency-check/bin/dependency-check.sh --scan . --format XML --out dependency-check-report.xml' 
    }
}
    stage('SonarQube') {
  steps {
    withSonarQubeEnv('SonarQube Server') {
      sh 'mvn clean package sonar:sonar'
      sh 'cat target/sonar/report-task.txt'
    }
  }
    }
    }
        // add here
    
        
        // no code below
    
}
