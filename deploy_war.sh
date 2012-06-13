cd war
rm -f TheJobCoach.war
jar -cvf TheJobCoach.war .
scp TheJobCoach.war tomcat6@www.thejobcoach.fr:/var/lib/tomcat6/webapps/




