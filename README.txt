How to user the application:
    1. Run
    2. Use "localhost:8070/addnewplayer" (POST) to create a new player either providing a username by param of json body
    3. Use "localhost:8070/start" (GET) to start a game session and user application log Scanner to simulate the game session


Changes or missing features:
    1. Transaction/transaction id - As there is a already a periodical background update and all changes regarding
    balance are done in memory, I abandoned separate transaction objects and only depend balance_version.
    2. Instead of random changes on balance I implemented a manual play session via log
    3. Being unexperienced in using grep I only provided simple System.out.println(..) logs