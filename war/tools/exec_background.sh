CUR=$(dirname $0)/../WEB-INF/classes
WAR=$(dirname $0)/../
echo Going into $CUR
cd $CUR

l=.
echo ===
ls $WAR/WEB-INF/lib/*.jar
echo ===
for j in $(ls $WAR/WEB-INF/lib/*.jar)
do 
    l=$l:$j
done
export CLASSPATH=$l

echo CLASSPATH=$CLASSPATH

java com.TheJobCoach.userdata.background.BackgroundProcess
