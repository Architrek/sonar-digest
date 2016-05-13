![alt text]("https://lh3.googleusercontent.com/5nx2NfAfpm7yqDZ4xPEACehw8g9q9F36V8gczwccNlpqVCnVWG-uMOHIno3rrzpM17B0HLWp38VAnRJ-KR4QcN9otEyk7IVfjINIb0I77k2Oa5kMUt1hl19V55B33cdd9Lm0_tNmwr8jZPqnWczzWuY3ITu8KmDRhm46Fl3R4xh9kRRboKHMVzyaSWFnceNdr1FJL8hMaENxM2WZt5Zeqp4ykakloNKfz-d4Gsm59-PKBj5iqc98tGP-47dd_Qz3C6JoSDEzxvLMAaZdQ7bSqIObR4r01uaHiR_M4eLobprPFVkJqN7c1oXz8YVt9FW0-i2137Z5IBwKPoH189Tbc1-CEeAULESyRCo0Fsmq_47TmNCzAEqkP6v2Fv7lBItkTEQ8Q_oTzj-fJs_uY8sVDGi9ctawLYoNxqI9x_lE2X9xDxwGKffXIbTVRkHQMd-wbbQxvHhQOplbeWDFtKf6gu-A1C3H7SmsNM9R4BZWcg7D0OeFWcBLGotTAel5itSz1E3elkfGA4YooVDXCeCyvuzffZspWuJc9VIVf0IqEsvuOh-tlKpqWgFrdrVBiG8wZC0XpmKaMt270mfHajznnPCfH5UvhVY=w1439-h199-no"© Andreetto Consulting)


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
