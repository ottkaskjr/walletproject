Open command prompt and navigate to docker folder located in project root directory. Run the following commands.
    1. "mvn clean package"
    2. "docker build . --tag ottkaskjr/playtech:0 -f Dockerfile"
    3. "docker compose up"

Using the application:
    1. Use "localhost:8070/addnewplayer?username=:name" (POST) to create a new player either providing a username
        by param of json body
    2. Use "localhost:8070/start?username=:name" (GET) to start a game session and use application(intellij) log
        to simulate the game session via Scanner input


Changes or missing features:
    1. Transaction/transaction id - As there is a already a periodical background update and all changes regarding
    balance are done in memory, I abandoned separate transaction objects and only depend balance_version.
    2. Instead of random changes on balance I implemented a manual play session via log
    3. Being unexperienced in using grep I only provided simple System.out.println(..) logs
    4. Also being unexperienced with docker I really hope it works...