![alt text](https://lh4.googleusercontent.com/16I7CeXKB-SrCmml5asljMWN4aVQWmX-AjiCieFHrOIoKrPl2TOwxU0L9OMqm0yiwmZqquIVQ4dPkpbSZGtuaCliBvl_ShQu6Q68HGwFrpN5lb-e2FGDyVJRokkBtcCSevWAaZ79pVCmzOoC3DBKyFulcgJmxeYnYqOLE5Kv2AFnYIiCSHvowAl3b7dDebqn2nZBtXzksO72JpuvzDDlKGS0WNmbm9YjqfgI5A8lxvJ2KyMrbNQfVepW-_koKqm9nL9zsktol8E51irAIw2CAzfsxFKhq_eIuklJ0L_zvK7f4DKEfB5TpzZ9K2rv-TLImZa2k7RmAQgMbpoDOsDrFGlgKGIRY6l2NRQpuuFt-iDGcZ-e3Mi6JlRhVT5kuI5a3KBS6pzw5K-LOyxEsWcEDPWGOOZFTLT5VGUxD6hpVOs3Gn2p26L3w22WqwQoXwfarRN93-7NX6J_wVOcLq90BpIAHnEbGqGvqc-833WN6JnbEVZiurgYddqnDX9hQ7zkYxW4c0tRBWUZvDIHcv1w4C0DHH8SYOJ97se1hsZN8fA0BAoyuipYdQ=s1439-w1439-h199-no "© Andreetto Consulting")


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
