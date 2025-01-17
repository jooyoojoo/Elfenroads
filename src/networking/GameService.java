package networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameService {

    // this class represents a networking.GameService on the lobby system
    // this is different from a networking.GameSession.
    // we need a networking.GameService in order to create a networking.GameSession with that game
    // but a networking.GameService can only be created by a user with the service role

    private User gameServiceUser;
    private String gameServiceAccountPassword;
    private String gameServiceAccountColor = "01FFFF";
    private String gameServiceName;
    private int minSessionPlayers;
    private int maxSessionPlayers;
    private String gameDisplayName;


    public GameService (String pGameServiceName, String pGameDisplayName, String pGameServiceAccountPassword, int pMinSessionPlayers, int pMaxSessionPlayers) throws IOException, Exception
    {
        // first, we need to create a user to manage the networking.GameService
        gameServiceName = pGameServiceName;
        gameServiceAccountPassword = pGameServiceAccountPassword;
        minSessionPlayers = pMinSessionPlayers;
        maxSessionPlayers = pMaxSessionPlayers;
        gameDisplayName = pGameDisplayName;
        createGameServiceUser();
        // now, we have created the game service user and we can go on to create the actual game service (using that user)
        createGameService();

    }

    public void createGameServiceUser() throws IOException, Exception
    {
        // first, check if the service user already exists. if we try to create a user that already exists, we will get an exception
        boolean usernameTaken = User.doesUsernameExist(gameServiceName);
        String adminToken = User.getAccessTokenUsingCreds("maex", "abc123_ABC123");
        if (usernameTaken)
        {
            User.logout();
            System.out.println("Username was taken.");
            gameServiceUser = User.init(gameServiceName, gameServiceAccountPassword);
        }
        // if a user does not already exist, we will just create one

        else
        {
            // this method will make a call to Users and create a user with the service role
            URL url = new URL("http://3.99.137.208:4242/api/users/" + gameServiceName + "?access_token=" + adminToken);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");

            /* Payload support */
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\n");
            out.writeBytes("    \"name\": \"" + gameServiceName + "\",\n");
            out.writeBytes("    \"password\": \"" + gameServiceAccountPassword + "\",\n");
            out.writeBytes("    \"preferredColour\": \"01FFFF\",\n");
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

           gameServiceUser = User.init(gameServiceName, gameServiceAccountPassword);
            // System.out.println("The token for the gameServiceUser is: " + this.gameServiceUser.getAccessToken());
        }


    }

    public void createGameService() throws IOException
    {
        String token = gameServiceUser.getAccessToken();
        URL url = new URL("http://3.99.137.208:4242/api/gameservices/" + gameServiceName + "?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"location\": \"\",\n");
        out.writeBytes("    \"maxSessionPlayers\": \"" + maxSessionPlayers + "\",\n");
        out.writeBytes("    \"minSessionPlayers\": \"" + minSessionPlayers + "\",\n");
        out.writeBytes("    \"name\": \"" + gameServiceName + "\",\n");
        out.writeBytes("    \"displayName\": \"" + gameDisplayName + "\",\n");
        out.writeBytes("    \"webSupport\": \"false\"\n");
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
