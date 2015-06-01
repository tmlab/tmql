## How to Install the TMQL4J Web UI ##

#### Requirements ####

To use Tmql4J Web UI, the following applications are needed:

  * Tomcat 7 with the manager app (which is delivered with the server)
  * Maven 3

#### Prepare the tomcat ####

Download the tomcat application and unpack the archive. You can find the current version at
http://tomcat.apache.org/ .

After downloading specify a user in _TOMCATROOT/conf/tomcat-users.xml_ e.g.

```
<?xml version='1.0' encoding='utf-8'?>
<tomcat-users>
  <role rolename="manager-gui"/>
  <role rolename="admin-gui"/>
  <user username="username" password="password" roles="manager-gui, admin-gui"/>
</tomcat-users>
```

This user is able to deploy a web application.

After that start the tomcat server with

```
TOMCATROOT/bin/catalina.sh start
```

Visit http://localhost:8080 and the tomcat start page should be visible.

### Installing from Source ###



#### Prepare the deployment ####

Maven provides plugins, which deploy a web application directly into a tomcat instance. To use this feature it is necessary to
modify the pom.xml of the majortom-server project. Look for the following entry and add the url to your tomcat instance.

```
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<server.url>http://localhost:8080</server.url>
	</properties>
```

The login data of the server is stored in the _settings.xml_ of maven. You can find it at:

```
HOME/.m2
```
If you don't find it there you might find it at
```
HOME/.m2/conf
```


If you don't have a _settings.xml_ copy the following, else just add the server part to your file:

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository/>
  <interactiveMode/>
  <usePluginRegistry/>
  <offline/>
  <pluginGroups/>
  <servers>
	  <server>
	        <id>tomcat-server</id>
	        <username>username</username>
	        <password>password</password>
	  </server>
  </servers>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>
```

Some configuration properties are needed to start the server.
Go to _SOURCEROOT/src/main/webapp_ and rename the file _server.properties.sample_. Open the file with a text editor and modify the values.
Their meaning is described in the file.

After that you can deploy the server with

```
mvn tomcat:deploy
```


You will find the admin interface at

```
http://localhost:8080/tmql4j-web-ui/
```

If you deploy to another server than the localhost you need to adapt the host and port.

ATTENTION: You must have the trailing / or the admin page won't be found.


### Installing from WAR-File ###

Download the provided WAR file from [downloads](http://code.google.com/p/tmql/downloads/list).

If your tomcat is running open the web manager availabe under

```
TOMCAT_URL/manager/html
```

At the deploy section you can found the entry _WAR file to deploy_. Select the downloaded war file by using the _browse_ button. To finish the deployment press the _deploy_ button.


Then you will find the web ui under

You will find the admin interface at

```
TOMCAT_URL/tmql4j-web-ui/
```