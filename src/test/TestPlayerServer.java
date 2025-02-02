package test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import networking.PlayerServer;
import utils.NetworkUtils;

public class TestPlayerServer
{
    public static void main(String[] args) throws IOException
    {
        //String command = "./ngrok tcp 6666";
        //Process proc = Runtime.getRuntime().exec(command);

        System.out.println(NetworkUtils.validateNgrok());
        System.out.println("No errors in the new method validateNgrok!");

        System.out.println(NetworkUtils.getServerInfo() + " is the ngrok address.");
        String [] tokenizedAddr = NetworkUtils.tokenizeNgrokAddr();
        for (String entry : tokenizedAddr)
        {
            System.out.println(entry);
        }


        System.out.println("WARNING: THE FOLLOWING TESTS ARE NOT WORKING AND NEED TO BE UPDATED");

        for (int i = 0; i < 1000000000; i++)
        {
            PlayerServer server = new PlayerServer(1);
            server.setMessage("It worked from the Server!! " + "You are now connected");
            // server.start(6666); NEED TO UPDATE THIS METHOD CALL FOR THIS TEST TO RUN
        }
        PlayerServer server = new PlayerServer(1);
        server.setMessage("It worked from the Server!! " + "You are now connected");
        // server.start(6666); NEED TO UPDATE THIS METHOD CALL FOR THIS TEST TO RUN
    }
}