REM This folder contains all third part library, need manually install into Maven repository.
REM Maven commond for install third library.
REM To run below command lines, need CD to "main6\tools\client tools\DispatcherMW" folder.

call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/msTranslatorPublicUtil.jar" -DgroupId=com.welocalize -DartifactId=MT-msTranslatorPublicUtil -Dversion=8.6 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/AOAPI.jar" -DgroupId=com.welocalize -DartifactId=MT-asiaOnline -Dversion=8.6 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/safaba-emtglobal-api-2013.07.17.jar" -DgroupId=com.welocalize -DartifactId=MT-safaba -Dversion=8.6 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/promtUtil.jar" -DgroupId=com.welocalize -DartifactId=MT-promt -Dversion=8.6 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/google-translate-api-v2-java-core-0.52.jar" -DgroupId=com.welocalize -DartifactId=MT-googleTranslate -Dversion=8.6 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/jboss-jaxrpc-api_1.1_spec-1.0.0.Final.jar" -DgroupId=com.welocalize -DartifactId=jaxrpc -Dversion=1.0.0.Final -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/log4j-1.2.16.jar" -DgroupId=org.apache.log4j -DartifactId=log4j -Dversion=1.2.16 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/slf4j-api-1.6.2.jar" -DgroupId=com.welocalize -DartifactId=slf4j-api -Dversion=1.6.2 -Dpackaging=jar  -DgeneratePom=true
call mvn install:install-file -Dfile="C:/Dispatcher/source/DispatcherMW/lib/globalsight.jar" -DgroupId=com.welocalize -DartifactId=globalsight -Dversion=8.6 -Dpackaging=jar  -DgeneratePom=true

