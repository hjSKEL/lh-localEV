
maven build
cd /c/Users/KEVIT/git/local-csms/localcsms && C:/project/tool/apache-maven-3.8.8/bin/mvn package -pl web/admin-web -Dmaven.test.skip=true 2>&1 | tail -5

admin-web
#java -Duser.timezone=GMT+09:00 -Dfile.encoding=UTF-8 -Xms{MIN_MEMORY}m -Xmx{MAX_MEMORY}m -jar evAdmin.jar {PORT}
java -Xms128m -Xmx256m -jar C:\Users\KEVIT\git\local-csms\localcsms\web\admin-web\target\evAdmin.jar 8081


