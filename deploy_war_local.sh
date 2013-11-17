export PATH=/home/avila/jdk1.7.0/bin/:/home/avila/jdk1.7.0:$PATH
cd war
rm -f TheJobCoach.war
jar -cvf TheJobCoach.war .
chmod a+r TheJobCoach.war
if [ -d /var/lib/tomcat6/webapps ]
then
  mv TheJobCoach.war /var/lib/tomcat6/webapps/
elif [ -d /var/lib/tomcat7/webapps ]
  then
  mv TheJobCoach.war /var/lib/tomcat7/webapps/
  else
  echo "Unable to find tomcat directory"
fi
