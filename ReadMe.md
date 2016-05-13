![alt text](https://goo.gl/photos/1pfJL6ZqUUNZBevPA"© Andreetto Consulting")


# Sonarqube Enabled POM

## Maven reporting
================

This POM enables integrated and aggregated mavan multimodule project reporting on Sonarqube

### Maven set up

Add the following profile to your maven settings.xml

```XML
        <profile>
            <id>sonar</id>

            <repositories>
            <!-- OPTIONAL -->
              <repository>
                <id>jdk14</id>
                <name>Repository ARCHITREK integration</name>
                <url>https://github.com/Architrek/architrek-mvn-repo</url>
                <layout>default</layout>
                <snapshotPolicy>always</snapshotPolicy>
              </repository>
          </repositories>

            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <!-- Optional URL to server. Default value is http://localhost:9000 -->
                <sonar.host.url>
                    http://localhost:9000
                </sonar.host.url>
            </properties>
        </profile>
```

and activate it

```XML
    <activeProfiles>
        <activeProfile>sonar</activeProfile>
    </activeProfiles>
```

### Maven Integration
To integrate the aggregate reporting in your multimodule maven project simply refer to the version requred:


```XML
    <parent>
        <groupId>com.architreck.projects.generisk</groupId>
        <artifactId>generisk-sonarqube</artifactId>
        <version>0.0.5</version>
    </parent>
```
and add the two findbugs-security files to the root of the prject

## Sonarqube

### Set Up
Download [./sonarqube-5.4] (./sonarqube-5.4) and configure the [./sonarqube-5.4/conf/sonar.properties] (sonarqube-5.4/conf/sonar.properties) to bind it with the DB instance to use.

## Usage

1. **Prepare all reporting data: mvn clean test site
2. **Publish all reporting data to the selected sonarqube server: mvn sonar:sonar
