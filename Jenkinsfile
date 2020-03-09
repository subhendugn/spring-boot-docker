node {
   echo 'Scm Checkout'
   
   stage('Scm Checkout'){
       git credentialsId: 'githib', url: 'https://github.com/subhendugn/spring-boot-docker.git', branch: 'master'
   }

   stage('Mvn Package'){
       def mvnHome = tool name: 'Maven3', type: 'maven'
       def mvnCMD =  "${mvnHome}/bin/mvn"
       sh label: '', script: "${mvnCMD} clean package"
   }

   stage('Build Docker Image'){
       sh 'docker build -t subhendugn/spring-boot-docker:latest .'
   }

   stage('Push Docker Image'){
    withCredentials([string(credentialsId: 'docker', variable: 'test_docker')]) {
            sh "docker login -u subhendugn -p ${test_docker} -e subhenduguhaneogi.93@gmail.com https://registry.hub.docker.com"
        }
       //sh 'docker run -p 7070:7070 -t subhendugn/spring-boot-docker:latest'
       sh 'docker push subhendugn/spring-boot-docker:latest'
   }

   stage('Create EC2'){
       withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'AKIARVFMMXD5EJINEEKO', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            //def awsOutput = sh(script:"/usr/local/bin/aws ec2 run-instances --image-id ami-0d9462a653c34dab7 --count 1 --instance-type t2.micro --key-name amazon-linux-2-ec2 --security-group-ids sg-07d4afe85eebeb153 --subnet-id subnet-7a2b0e12 --region ap-south-1")
            sh(script: "/usr/local/bin/aws ec2 run-instances --image-id ami-0d9462a653c34dab7 --count 1 --instance-type t2.micro --key-name jenkinsKey --security-group-ids sg-07d4afe85eebeb153 --subnet-id subnet-7a2b0e12 --region ap-south-1 | tee test.json ")
        }
   }

    stage ("Wait for EC2 Instance to warm up")
    {
       sh 'sleep 40'
    }

//   stage('Run Container on Dev Server'){
//       def dockerRun = 'docker run -p 8080:7070 -d subhendugn/spring-boot-docker:latest'
//       def privateIp = sh(script: "jq -r '.Instances[0].PrivateIpAddress' test.json" , returnStdout: true)
//       //def cmd = ("/usr/local/bin/aws ec2 describe-instances --instance-ids '${instanceId}'").trim()
//       //sh(script: "/usr/local/bin/aws ec2 describe-instances --filters Name=private-ip-address,Values=${instanceId} echo | tee test2.json")
       
//       //def publicDns =  sh(script: "jq -r '.Instances[0].PublicDnsName' test2.json" , returnStdout: true)
       
//       def publicDnsName = sh(script: "/usr/local/bin/aws ec2 describe-instances --query Reservations[0].Instances[0].PublicDnsName --filters Name=private-ip-address,Values=${privateIp}" , returnStdout: true)
       
//       def updateEc2 = "sudo yum update -y"
//       def installDocker = "sudo yum install -y docker"
//       def dockerStart = "sudo service docker start"
//       def dockerPermission = "sudo usermod -a -G docker ec2-user"
       

//       sshagent(['test_ec2']) {
          
//             sh (script:"ssh -tt -o StrictHostKeyChecking=no ec2-user@${publicDnsName} '${updateEc2} ${installDocker} ${dockerStart} ${dockerPermission} ${dockerRun} ' ")
//         }
//   }
   def jobName = "ec2"
   stage ("Calling Job : ${jobName}")
    {
        def privateIp = sh(script: "jq -r '.Instances[0].PrivateIpAddress' test.json" , returnStdout: true)
        def publicDnsName = sh(script: "/usr/local/bin/aws ec2 describe-instances --query Reservations[0].Instances[0].PublicDnsName --filters Name=private-ip-address,Values=${privateIp}" , returnStdout: true)
        
        final publicDns = publicDnsName.substring(1, publicDnsName.length()-2)
       build job: "${jobName}", parameters: [string(name: 'PUBLIC_IP', value: String.valueOf(publicDns))]
    }
   
}