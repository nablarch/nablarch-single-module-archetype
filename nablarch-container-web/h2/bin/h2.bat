@java -cp "h2-1.3.176.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console %* -webAllowOthers -tcpAllowOthers
@if errorlevel 1 pause