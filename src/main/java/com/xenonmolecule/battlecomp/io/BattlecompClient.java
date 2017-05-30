package com.xenonmolecule.battlecomp.io;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

public abstract class BattlecompClient {
    /* Gradle dependencies:
      compile group: 'org.json', name: 'json', version: '20090211'
      compile ('io.socket:socket.io-client:0.8.3') {
        // excluding org.json which is provided by Android
        exclude group: 'org.json', module: 'json'
      }
     */
    private Socket socket;
    private int gameId;
    private boolean connected;
    private GameState state = GameState.DISCONNECTED;
    private String preifx = "";

    public BattlecompClient(String prefix) {
        this.preifx = prefix;
    }

    public BattlecompClient() {
        this("");
    }

    public void runAsync(Runnable r) {
        //new Thread(r).start();
        r.run();
    }

    public void connect(String IP) {
         /*
        ** Init socket
         */
        try {
            socket = IO.socket(IP);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        /*
        ** Listeners that will call the methods inside the class
         */

        // Connecting
        socket.on("connect", args -> {
            runAsync(this::onConnect);
        });

        socket.on("failure", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Uh oh, Battlecomp ran into a brick wall...");
                for (Object arg : args)
                    System.out.println("--> " + arg);
            });
        });

        socket.on("victory", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> We won!");
                victory();
                setState(GameState.COMPLETED);
            });
        });

        socket.on("loss", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> We lost, plz git gud");
                loss();
                setState(GameState.COMPLETED);
            });
        });

        socket.on("updateBoard", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp sent our hits: ");
                int[][] map = new int[10][10];
                try {
                    System.out.println(args[0]);
                    JSONArray lines = ((JSONObject) args[0]).getJSONArray("board");
                    System.out.println("-->    0 1 2 3 4 5 6 7 8 9");
                    System.out.println("-->   *--------------------*");
                    for (int i = 0; i < lines.length(); i++) {
                        System.out.print("--> " + i + " |");
                        JSONArray line = lines.getJSONArray(i);
                        for (int j = 0; j < line.length(); j++) {
                            map[i][j] = line.getInt(j);
                            if (line.getInt(j) == 1)
                                System.out.print("X ");
                            else if (line.getInt(j) == 2)
                                System.out.print("O ");
                            else
                                System.out.print("  ");
                        }
                        System.out.println("|");
                    }
                    System.out.println("-->   *--------------------*");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(preifx+"> Battlecomp calling onMapUpdate ");
                onMapUpdate(map);
            });
        });

        socket.on("updateOppDisp", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp sent our opponents hits: ");
                int[][] map = new int[10][10];
                try {
                    JSONArray lines = ((JSONObject) args[0]).getJSONArray("board");
                    System.out.println("-->    0 1 2 3 4 5 6 7 8 9");
                    System.out.println("-->   *--------------------*");
                    for (int i = 0; i < lines.length(); i++) {
                        System.out.print("--> " + i + " |");
                        JSONArray line = lines.getJSONArray(i);
                        for (int j = 0; j < line.length(); j++) {
                            map[i][j] = line.getInt(j);
                            if (line.getInt(j) == 1)
                                System.out.print("X ");
                            else if (line.getInt(j) == 2)
                                System.out.print("O ");
                            else
                                System.out.print("  ");
                        }
                        System.out.println("|");
                    }
                    System.out.println("-->   *--------------------*");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onOppMapUpdate(map);
            });
        });

        // Test message and response
        socket.on("success", args -> {
            /*System.out.println("> Got success:");
            for (Object o : args) {
                if (o instanceof JSONObject)
                    System.out.println("--> "+o.toString());
            }*/
        });

        // Handle opponents being sunk
        socket.on("sinkUpdate", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Got sink update:");
                for (Object o : args) {
                    if (o instanceof JSONObject)
                        System.out.println("--> " + o.toString());
                }
            });
        });

        // Call disconnect
        socket.on("disconnect", args -> {
            runAsync(this::onDisconnect);
        });

        // Handle another form of disconnect
        socket.on("partnerDisconnect", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Partner disconnected from match!");
                onDisconnect();
            });
        });

        // Print ID
        socket.on("getId", args -> {
            runAsync(() -> System.out.println(args[0]));
        });

        // When it's our turn, pick a coordinate and send it back
        socket.on("takeTurn", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp asked us to take turn!");
                Coordinate toAttack = takeTurn();
                System.out.println(preifx+"> Sending attack request to (" + toAttack.getX() + ", " + toAttack.getY() + ")");
                socket.emit("submitTurn", toAttack.toJson());
            });
        });

        // When we can start, tell implementation
        socket.on("canStart", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp game is now able to be started!");
                setState(GameState.CAN_START);
                onCanStart();
            });
        });

        // When we can start, tell implementation
        socket.on("canPlay", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp game is now able to be played!");
                setState(GameState.CAN_PLAY);
                onCanPlay();
            });
        });

        // Handle sunk ships
        socket.on("sinkUpdate", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp told us we sunk a ship!");
                JSONObject object = (JSONObject) args[0];
                try {
                    int x = object.getInt("coordX");
                    int y = object.getInt("coordY");
                    int orientation = object.getInt("orientation");
                    String shipType = object.getString("type");
                    System.out.println(shipType + " at (" + x + "," + y + ")");
                    onShipSunk(shipType, x, y, orientation);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("--> Failed to handle sunk ship!");
                }
            });
        });

        // When we can start, tell implementation
        socket.on("getBoard", args -> {
            runAsync(() -> {
                System.out.println(preifx+"> Battlecomp sent an instance of the board!");
                try {
                    JSONArray lines = ((JSONObject) args[0]).getJSONArray("board");
                    System.out.println("-->    0 1 2 3 4 5 6 7 8 9");
                    System.out.println("-->   *--------------------*");
                    for (int i = 0; i < lines.length(); i++) {
                        System.out.print("--> " + i + " |");
                        JSONArray line = lines.getJSONArray(i);
                        for (int j = 0; j < line.length(); j++) {
                            if (line.getInt(j) == 1)
                                System.out.print("# ");
                            else if (line.getInt(j) == 2)
                                System.out.print("X ");
                            else
                                System.out.print("  ");
                        }
                        System.out.println("|");
                    }
                    System.out.println("-->   *--------------------*");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        // Handle being told to place our game objects
        socket.on("setupBoard", ignored -> {
            runAsync(() -> {
                System.out.println(preifx+"> Server requesting board setup...");
                setState(GameState.BOARD_SETUP);
                List<PlacedShip> ships = placeShips();
                JSONArray toSend = new JSONArray();
                for (PlacedShip ship : ships)
                    toSend.put(ship.toJson());
                JSONObject arrayWrap = new JSONObject();
                try {
                    arrayWrap.put("ships", toSend);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(arrayWrap);
                socket.emit("setBoard", arrayWrap);
            });
        });

        socket.on("success", args -> {
            runAsync(() -> {
                JSONObject object = (JSONObject) args[0];
                System.out.println(object);
                try {
                    if (object.getString("action").equals("StartPlay")) {
                        System.out.println(preifx+"> Successfully playing Battlecomp game " + gameId + "!");
                        setState(GameState.PLAYING);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("success", args -> {
            runAsync(() -> {
                JSONObject object = (JSONObject) args[0];
                try {
                    if (object.getString("action").equals("SetBoard")) {
                        System.out.println(preifx+"> Successfully set board for game " + gameId);
                        setState(GameState.BOARD_WAITING);
                        getBoard();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("success", args -> {
            runAsync(() -> {
                JSONObject object = (JSONObject) args[0];
                try {
                    if (object.getString("action").equals("JoinGame")) {
                        System.out.println("-------> "+object);
                        this.gameId = gameId;
                        System.out.println("-------> " + gameId);
                        System.out.println(preifx+"> Successfully joined Battlecomp game " + gameId + "!");
                        setState(GameState.JOINED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("success", args -> {
            runAsync(() -> {
                JSONObject object = (JSONObject) args[0];
                try {
                    if (object.getString("action").equals("StartGame")) {
                        System.out.println(preifx+"> Successfully started Battlecomp game " + gameId + "!");
                        setState(GameState.STARTED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("success", args -> {
            runAsync(() -> {
                JSONObject object = (JSONObject) args[0];
                try {
                    if (object.getString("action").equals("CreateGame")) {
                        gameId = object.getInt("message");
                        System.out.println(preifx+"> Successfully created Battlecomp game " + gameId);
                        setState(GameState.JOINED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        /*
        ** Attempt to connect
         */

        System.out.println(preifx+"> Attempting to connect to Battlecomp server...");
        socket.connect();
    }

    public void onConnect() {
        connected = true;
        setState(GameState.CONNECTED);
        System.out.println(preifx+"> Successfully connected to Battlecomp server!");
    }

    public void onDisconnect() {
        connected = false;
        setState(GameState.DISCONNECTED);
        System.out.println(preifx+"> Disconnected from Battlecomp server!");
    }

    public void onCanStart() {
        start();
    }

    public void onCanPlay() {
        play();
    }

    // Creates game
    public void createGame() {
        System.out.println(preifx+"> Attempting to host Battlecomp game");
        socket.emit("createGame", "");
    }

    // Join a hosted game
    public void joinGame(int gameId) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("ID", gameId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        this.gameId = gameId;
        System.out.println(preifx+"> Attempting to join Battlecomp game " + gameId);
        socket.emit("joinGame", obj);
    }

    public void start() {
        socket.emit("startGame", "");
    }

    public void play() {
        socket.emit("startPlay", "");
    }

    public int getGameId() {
        return gameId;
    }

    public boolean isConnected() {
        return connected;
    }

    public void getBoard() {
        socket.emit("getBoard", "");
    }

    public void getId() {
        socket.emit("getId", "");
    }

    public abstract void onGamestateUpdate(GameState state);

    /**
     * Called when it's time for the user to take their turn
     *
     * @return The coordinate to shoot at
     */
    public abstract Coordinate takeTurn();

    public abstract List<PlacedShip> placeShips();

    public abstract void onShipSunk(String shipType, int x, int y, int orientation);

    public abstract void onMapUpdate(int[][] map);

    public abstract void onOppMapUpdate(int[][] map);

    public void victory() {};

    public void loss() {};

    public GameState getState() {
        return state;
    }

    private void setState(GameState state) {
        this.state = state;
        System.out.println(preifx+"> Battlecomp state set to " + state);
        onGamestateUpdate(state);
    }
}
