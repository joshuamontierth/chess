# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAHZM9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCQUKDddw-Gsv0+J4bSWXY+gBc5NyVbUhxgBAEKQNBYXgxDUXRWJsUHXVe2TcoSiQMVLDNOFSPIkN3kLJlkzdDluT5AVqzucVJWrHsix06cB2VIcbgAWQgDgUE1aTE1kllygExDc0YsjOwbc90C0l0S0KMtvV9f0D0Dfzs0CpIoxjEdzO0hUrO3ScVVi0cLwnKcZy43dRKElc10wOiQR4korgGaKxkA68vj-C9vxvaj73QjBn1fd9as-eqgKagL-1ajizE4MDvD8QIvBQNtRN8ZhkPSTJMAfPJrJ3ecKmkXkuXqLlmhaAjVCI7pmvHdqCqBei+guy9-nKwqaIy8F+IQxaRI+rNxIxKSbJkiyPJgBSlMzbNYXu4K+1LPS9sM+6TIgKVkqTNKqvBe68oTAl3INLzPqhlKQt08oZD2g7svipGUYbYmYYxrK1Q1bHDHSirygWn61DKiqt2oq4qOKO90vWsBurfXoDhAyaIMCSEODg6EYAAcXzVlltQtbOuYKrsJV-ajvsfNzuG3Krto56avu9jOJul7B086E1dGVQROd9Xfsk1nAdSg1QcscGcyJtHXVhzl4cFRGJWR7L6csxm+Kx1zcaBg1yTAF2UB5aFYWhyywo5GByEjmAACoaZgY3Xfj9HNsyoc4AgDUUHAIiAB4s4cpyEHyH32atpXYiz1RSoQddnv54X5x6KZq7UcZKn6eePWkReAEYnwAZgAFieFDMhUu4Vi+HQEFAZsj8PCYvnnr18xvvYXGfmBGiFraOpycWYBfSXZ76eeqhF4VGXvmVeG9t57ymAfU0dUT5PDPhfK+TE+i33zPfUYj9n4uFfgccaoFPBTUgtgHwUBsDcHgMaTIqt8wpBWmhL+mFp7lBwg0I2Jtghm3QO+O++Z37HGunOa2XCHongAeg6+qw7ZziYbxVMVCUBZ1hHABRWcvZYn7qnP2KYQaUEUoHBskMRH5zSoXCOBko4iMrqjPGJZE7DhEZomQtjSiQkRDCJR89u7ORMWHMxxcIqVkFCvaQswYHIL8ogkAl86qzF4TFeejJ2wxkAbXMO9ivGOWcv3QRwJygqPTJkNRPNx5PXtlPLaVw55gLXo9K6pwxYS3fNU0Y4DGrSwmoQuWARLDOQEskGAAApCAQkaGigCFE5s2tGF622lUSkeEWjz1NnFf875yHAF6VAJuAkoBxJqfwkWlt7bCNWS1Zizxz5bJ2dAReYiQm2zKTI+xMAABWIy0BKOGUJYpaI-pONDjogOQcjFnKCmk0K7JzH8ksWCpIMdabdkBRUhupRSIApcSAVIrdmw3LhCE3xkKyz6RhVXGpldUnIvSo7TJPcMVpxTO8n5+ZYQbOuc3aAsxSKEtJsXSkVRpAKEpoAkugo2WUDxTAAAvEZfMEKUVvWZi5AGA8TlDI+cU1cpS+bUuYXuQ5lVCiNJ-j1KW+DZbTQCF4TZ8BuB4E7NgchhB4iJDoVrMWsjqqVF2nyA6R1jC3hehzfVTzuL1zeiAO1cJpDOQpCgdR-1eK+xCuUHucaFAajzvK8O5NfVcnLpXG42b-E+v2vmiuCK45UpLRTctFL1bZpeUq0wrlcm7kQOmZcJSJ7lN1ZUkN9SjU6yaWamWQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
