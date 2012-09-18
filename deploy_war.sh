cd war
rm -f TheJobCoach.war
jar -cvf TheJobCoach.war .
scp -v -i ~/EC2/thejobcachkey.pem TheJobCoach.war ubuntu@ec2-79-125-113-220.eu-west-1.compute.amazonaws.com:/home/ubuntu/
