export PATH=/home/avila/jdk1.7.0/bin/:/home/avila/jdk1.7.0:$PATH
cd war
rm -f TheJobCoach.war
jar -cvf TheJobCoach.war .
chmod a+r TheJobCoach.war
mv TheJobCoach.war /var/lib/tomcat6/webapps/