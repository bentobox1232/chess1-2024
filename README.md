# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.


[sequencing diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJptLoDMZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXUcTkSxv2UFq7q6nnr3l8fsaAcCIKCJs7tQf3cFlwizzyA1K1w95fjfMrVbPNTf92hHo9TZDkuSuHlKkzcM-XtMVoIA7E3RCR8Img31I0fIMRyidDsyjboQn8eMk1TdM8L9XM0HzQt9EMIwdBQO1Ky0fRmFrbxfEwYim16Vs+E+N4kjeNJ0i7CQezyCjMJbUdUJgSdWJVUZpLQJdPVUEJ3xxRS2JUi0MLUxCdWApkYAEr5hLDQygRBMNjLvFCNNePRZVfFd7xw8hXNdAiYx4hMYBTNMCjIHyHiomidDo4xBhkCthhgABxHlHg4+tuMbZgPS6KJoiSoTRK0HkpIM-CRyeeTjjAFKyn0rM-XU8dNOCbS5RoKAkEBcwfzVVS9yQ3UzOPb5flU2zLzDGB3JQLSXQ-GBqtqyRRgGkzgiPQTTwAKgm0FirKBygKc5qog+YYAB4DpQYIZuw2Szsu67boqoistIkL8nOhwrp5YJIs0aLiyMbA9CgbAEFUOAv1UZaPE4hsAmyscOryhJkiKkrTDKv102u1keWHWTKuc3EYeW+qYPQJr4Tm28Fo6rqeotSnDLWu8NuGraxpxu0L1BBC2sPMzQMNX5rrgxaXyF+9enYI4eWZPg7s8h6pbKJWCPvAKImCvHFb4CIkBkAHaOB8wUBRCB3BgAApCBp2SnljAUBBQGtTKkYZLy4lODt0mu0qGvQdMArgCBJygSprqVomuhJ06YAAKwdtAKdUyow4j6Bo4Nmm33mnFGe63rWezdmgM515ues7M9vsrC6cAovKCZimY74TOsvDyOK6kUzq5PcWDa7pGe+gevrqO+lVcTlQwCsCOUFGDvR-jceoGnt0q5gU5Yj4YQrMDtB3l+LPI5gABeWvcbQAB1AAJQ03nVlAlav1-NZlnL4RctzG5Oqjby-9ZJvSRrrMioVwpSFNkDeiOhgCWEQIqWAwBsAQ0IM4Vw8MMoBW9mraIFkhIiQyOoPyP95a4khngVaKtWqFzlFQlBtCZY7yIVZfm9lWF6nYS-Th15WGMiiLwyeqUt73UTmFEBzUJFAKkb5V6YCSJBUgfkeREVNB5kwEAA)

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


