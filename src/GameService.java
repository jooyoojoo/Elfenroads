import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameService {

    // this class represents a GameService on the lobby system
    // this is different from a GameSession.
    // we need a GameService in order to create a GameSession with that game
    // but a GameService can only be created by a user with the service role

    private User gameServiceUser;
    private User adminUser;
    private String gameServicePassword;
    private String gameServiceColor = "01FFFF";
    private String gameServiceName;
    private String gameDisplayName;
    private int minSessionPlayers;
    private int maxSessionPlayers;


    public GameService (User pAdminUser, String pGameServiceName, String pGameServicePassword, int pMinSessionPlayers, int pMaxSessionPlayers, String pGameDisplayName) throws IOException
    {
        // first, we need to create a user to manage the GameService
        adminUser = pAdminUser;
        gameServiceName = pGameServiceName;
        gameServicePassword = pGameServicePassword;
        minSessionPlayers = pMinSessionPlayers;
        maxSessionPlayers = pMaxSessionPlayers;
        gameDisplayName = pGameDisplayName;
        createGameServiceUser();
        // now, we have created the game service user and we can go on to create the actual game session (using that user)
        createGameService();


    }

    public void createGameServiceUser() throws IOException
    {
        // first, check if a service user already exists (will do this later)
        // if a user does not already exist, we will just create one

        // this method will make a call to Users and create a user with the service role
        URL url = new URL("http://127.0.0.1:4242/api/users/" + gameServiceName + "?access_token" + adminUser.getAccessToken());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"name\": \"" + gameServiceName + "\",\n");
        out.writeBytes("    \"preferredColour\": \"01FFFF\", \n");
        out.writeBytes("    \"" + gameServiceColor + "\": \"01FFFF\",\n");
        out.writeBytes("    \"role\": \"ROLE_SERVICE\"\n");
        out.writeBytes("}");
        out.flush();
        out.close();

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);
        System.out.println(content.toString());

        User gameServiceUser = new User(gameServiceName, gameServicePassword);
        this.gameServiceUser = gameServiceUser;
    }

    public void createGameService() throws IOException
    {
        String token = gameServiceUser.getAccessToken();
        URL url = new URL("http://127.0.0.1:4242/api/gameservices/" + gameServiceName + "?access_token=" + gameServiceUser.getAccessToken());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"location\": \"\",\n");
        out.writeBytes("    \"maxSessionPlayers\": \"" + maxSessionPlayers + "\",\n");
        out.writeBytes("    \"" + minSessionPlayers + "\": \"3\",\n");
        out.writeBytes("    \"name\": \"" + gameServiceName + "\",\n");
        out.writeBytes("    \"displayName\": \"" + gameDisplayName + "\",\n");
        out.writeBytes("    \"webSupport\": \"true\"\n");
        out.writeBytes("}");
        out.flush();
        out.close();

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);
        System.out.println(content.toString());



    }







}
