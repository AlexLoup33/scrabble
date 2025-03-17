[![](img/Scrabble.png)](https://gitlab.emi.u-bordeaux.fr/pdp-2025/scrabble-1)

# README - Projet de Programmation 2025 : Scrabble Java

**Table of Contents**
1. **[About The Project](#about-the-project)**
2. **[Technologies](#technologies)**
3. **[Clone The Repository](#clone-the-repository)**
4. **[Usage](#usage)**
5. **[Contact](#contact)**

## About The Project
This project is a Java implementation of the famous game Scrabble, using multiples technologies, algorithmes, data structures and design patterns. The game is supposed to be played from 2 to 4 players, either by human or AI. The game can be played in a console or a graphical interface. Multiples options will allow the user to customize the game.

## Technologies
The project is developed in Java, using the following technologies :

[![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://openjdk.org/projects/jdk/17/) [![Shell Script](https://img.shields.io/badge/shell_script-%23121011.svg?style=for-the-badge&logo=gnu-bash&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://ba-sh.com/fr/fr/) [![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://git-scm.com/) [![GitLab](https://img.shields.io/badge/GitLab-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://gitlab.com/) [![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://www.linux.org/) 

[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://maven.apache.org/) [![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://junit.org/junit5/) [![JavaFX](https://img.shields.io/badge/JavaFX-5382A1?style=for-the-badge&logo=java&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://openjfx.io/) [![Jcoco](https://img.shields.io/badge/Jacoco-FF4088?style=for-the-badge&logo=jacoco&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://www.jacoco.org/jacoco/) [![Checkstyle](https://img.shields.io/badge/Checkstyle-3B4252?style=for-the-badge&logo=checkstyle&logoColor=ECEFF4&color=3B4252&labelColor=3B4252)](https://checkstyle.org/)

## Clone The Repository
To clone the repository, use the following command :
* **Clone with HTML**
```sh
git clone https://gitlab.emi.u-bordeaux.fr/pdp-2025/scrabble-1.git
```

* **Clone with SSH**
```sh
git clone git@gitlab.emi.u-bordeaux.fr:pdp-2025/scrabble-1.git
```

## Usage
To use the project, you can run the script `scrabble` in the scrabble directory. This script will compile the project if the .jar file is not present in the target directory, and then run the game with the options given in the command line.

* **Run the script**
```sh
./scrabble [options]
```
note : The options are used with the following syntax : `-flag value`.


For more information about the options, you can run the script with the `-h` option.
* **Run the script with the help option**
```sh
./scrabble -h
```

## Contact
* **Project Link** : [Scrabble Java](https://gitlab.emi.u-bordeaux.fr/pdp-2025/scrabble-1)


* **Andrieu Ludovic - ludovic.andrieu@etu.u-bordeaux.fr**
* **Cassard Aymeric - aymeric.cassard@etu.u-bordeaux.fr**
* **Cazet Valentin - valentin.cazet@etu.u-bordeaux.fr**
* **Lou-Poueyou Alexandre - alexandre.lou-poueyou@etu.u-bordeaux.fr**
* **Mayol Jean - jean.mayol@etu.u-bordeaux.fr**
* **Petry Mathéo - matheo.petry@etu.u-bordeaux.fr**


# PdP Java Projects HOWTO

## List dependencies

```console
#> mvn site
#> firefox target/site/dependencies.html
```

## Build and tests

```console
#> mvn clean install
#> mvn package
#> mvn test
```

The `package` target should generate a `target/*.jar` file.

The coverage is obtained with:

```console
#> mvn jacoco:report
#> firefox target/site/jacoco/index.html
```

## Documentation

```console
#> mvn javadoc:javadoc
#> firefox target/site/apidocs/index.html
```

If you want to try to fix some javadoc errors, you can try (but it is not always
working):

```console
#> mvn javadoc:fix
```

## Checkstyle

If you want to check if the code is compliant with the Google Java Style, you need to check that the following plugin is present in the `pom.xml` file:

```xml
<build>
    <plugins>
      ...
      <!-- Checkstyle Plugin -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>3.6.0</version>
        <configuration>
          <configLocation>google_checks.xml</configLocation>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

Then, you can run the checkstyle with:

```console
#> mvn checkstyle:checkstyle
#> firefox target/reports/checkstyle.html
```

## PMD Static Analysis

If you want to run the PMD static analysis, you need to check that the following plugin is present in the `pom.xml` file:

```xml
  <build>
    <plugins>
      ...
      <!-- PMD Plugin -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.26.0</version>
        <configuration>
          <rulesets>
            <!-- If you want to start small, take only this line -->
            <!-- <ruleset>/rulesets/java/quickstart.xml</ruleset> -->
            <!-- All the rulesets -->
            <ruleset>/category/java/bestpractices.xml</ruleset>
            <ruleset>/category/java/codestyle.xml</ruleset>
            <ruleset>/category/java/design.xml</ruleset>
            <ruleset>/category/java/documentation.xml</ruleset>
            <ruleset>/category/java/errorprone.xml</ruleset>
            <ruleset>/category/java/multithreading.xml</ruleset>
            <ruleset>/category/java/performance.xml</ruleset>
            <ruleset>/category/java/security.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

Using only `quickstart.xml` is already a really good start and might be sufficient. It’s not about addressing all the suggestions, but rather the ones that seem most relevant.

Then, you can run the PMD static analysis with:

```console
#> mvn pmd:pmd
#> mvn pmd:check
#> firefox target/reports/pmd.html
```
