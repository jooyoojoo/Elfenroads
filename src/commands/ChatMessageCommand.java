package commands;

import domain.Player;
import loginwindow.ChatBoxGUI;

// will help us implement the chat feature
public class ChatMessageCommand implements GameCommand {

    private String message;
    //private String senderName;
    private Player player;

    public ChatMessageCommand (String pMessage, Player pSender)
    {
        message = pMessage;
        player = pSender;
    }

    @Override
    /**
     * @pre the ChatBoxGUI has been initialized (done in GameScreen constructor)
     */
    public void execute()
    {
        String msgPlusName = player.getName() + ": " + message; // the GUI chatbox will automatically add a newline
        ChatBoxGUI.getInstance().displayMessage(msgPlusName);
        ChatBoxGUI.clearInputArea(); // clear the input text (will only do anything if the local player is the one who sent the message)
    }




}