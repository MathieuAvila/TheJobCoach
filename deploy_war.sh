export PATH=/home/avila/jdk1.7.0/bin/:/home/avila/jdk1.7.0:$PATH
cd war
rm -f TheJobCoach.war
jar -cvf TheJobCoach.war .
scp -v -i ~/EC2/thejobcachkey.pem TheJobCoach.war ubuntu@ec2-79-125-113-220.eu-west-1.compute.amazonaws.com:/home/ubuntu/
