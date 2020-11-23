Running the application with docker:
Open command prompt and navigate to docker folder located in project root directory. Run the following commands.
    1. "mvn clean package"
    2. "docker build . --tag ottkaskjr/playtech:1 -f Dockerfile"
    3. "docker compose up"

Using the application:
    1. Use "localhost:8070/addnewplayer?username=:name" (POST) to create a new player either providing a username
        by param of json body (requires postman)
    2. Use "localhost:8070/start?username=:name?random=:true" (GET) to start a random game session with previously
     created user. When random is set to false, it is possible to change balance manually by running the application
     in intellij and using the console. NB! WHILE RUNNING FROM DOCKER IMAGE THE MANUAL INPUT CANNOT BE USED.


Changes or missing features:
    1. Transaction/transaction id - As there is a already a periodical background update and all changes regarding
    balance are done in memory, I abandoned separate transaction objects and only depend balance_version.
    2. Instead of random changes on balance I implemented a manual play session via log
    3. Being unexperienced in using grep I only provided simple System.out.println(..) logs
    4. Also being unexperienced with docker I realised my first initialization of the application will not work as
    intended. It also might have been that I wasn't able to push the latest version to docker to enable random sessions
    or I have provided non-working docker instructions. But I gave my best.