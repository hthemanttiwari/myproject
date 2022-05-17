pipeline {
  agent any
  
  tools {
    maven "MAVEN_HOME"
  }

  parameters {
    string(name: 'BranchName', defaultValue: 'master', description: 'branch to build')
    choice(name: 'ArtifactRepo', choices: ['snapshot', 'releases'], description: 'Artifact repository')
  }
  
  stages {
    stage('Declarative: Configurations') {
      steps {
                echo "${params.configuration}"
      }
    }

    stage('Maven: Build Test') {
      steps {
        sh 'mvn clean test'             
      }
    }

    stage('SonarQube: Sonar Analysis') {
      steps{
        withSonarQubeEnv('scan') {
          sh "mvn sonar:sonar"
        }
      }
    }
        
    stage('Maven: Package') {
      steps {
        sh 'mvn clean package'             
      }
    }
    
    stage ('JFrog: Upload and Download Artifactory'){
      steps{
        rtUpload (
          serverId: 'central',
          spec: '''{
            "files": [{
              "pattern": "target/*.war",
              "target": "libs-$ArtifactRepo-local/myproject-${BUILD_NUMBER}.war"
            }]
          }''',
        )
        
        rtPublishBuildInfo (
          serverId: 'central'
        )
        
/*       
        rtPromote (
          serverId: 'central',
          targetRepo: 'libs-release-local',
          sourceRepo: 'libs-snapshot-local',
          includeDependencies: true,
          failFast: true,
          copy: true
        )
*/ 

        rtDownload (
          serverId: 'central',
          spec: '''{
            "files": [{
              "pattern": "libs-$ArtifactRepo-local/myproject-${BUILD_NUMBER}.war",
              "target": "Ansible/roles/myproject/files/"
            }]
          }''',
        )         
      }
    }             

/*   
    stage ('Docker: Image Build'){
      steps{
        sh 'docker build -t myproject .'
      }
    }
       
    stage ('Docker Compose: Container Deployment'){
      steps{
        sh 'docker stack rm myproject'
        sh 'sleep 10'
        sh 'docker stack deploy -c docker-compose-myproject.yaml myproject'
      }
    }                
         
    stage ('Ansible: File Preparation')
    {
      steps{
        sh 'mv target/*.war Ansible/roles/myproject/files/myproject-${BUILD_NUMBER}.war'
      }
    }
*/
           
    stage ('Ansible: Docker Build and Deploy'){
      environment {
        ANSIBLE_CONFIG = "/Ansible/ansible.cfg"
      }
      steps{
        sh 'pwd'
        sh label: '', script: 'export ANSIBLE_CONFIG=$ANSIBLE_CONFIG'
        ansiblePlaybook (
          credentialsId: 'cicd_ssh', 
          disableHostKeyChecking: true, 
          installation: 'ansible', 
          inventory: '/Ansible/ansiserver', 
          playbook: 'Ansible/myproject.yml'
        )
      }
    }
    stage ('Cadvisor Monitoring') {
      steps {
        sh 'docker container stop cadvisor_jenkins'
        sh 'docker container rm cadvisor_jenkins'
        sh 'docker container run --detach=true \
            --volume=/:/roots:ro \
            --volume=/var/run:/var/run:rw \
            --volume=/sys:/sys:ro \
            --volume=/var/lib/docker:/var/lib/docker:ro \
            --publish=7090:8080 \
            --name=cadvisor_jenkins \
            google/cadvisor:latest'
      }
    }
  }

  post {
    success {
      //emailext body: 'SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})', subject: 'Build Success', to: '123@gmail.com, 129au@gmail.com'
      echo 'success'
    }

    failure {
      //emailext body: 'FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})', subject: 'Build Failed', to: '123@gmail.com, 129au@gmail.com'
      echo 'failure'
    }

    always {
      cleanWs()
    }
  }

}

