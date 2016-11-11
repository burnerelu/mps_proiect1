import java.net.Socket;
import java.net.ConnectException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Client {
    
    static int Connected = 0;
    
    public static void sendMessage(PrintStream ps, String message, int code) {
        System.out.println("Sending " + message);
        ps.println("" + code + "<" + message + ">\n");
    }
    public static void main(String args[]) throws Exception {
        int rc;
        Socket sock = new Socket();
        while(Connected == 0) {
            try {
                Connected = 1;
                sock = new Socket("localhost", 24999);
            }
            catch (ConnectException e) {
                Connected = 0;
                System.out.println("Connection failed. Trying again in 5 seconds..");
                Thread.sleep(5000);
            }
        }
        /* Connected now */
        try {
            PrintStream pstream = new PrintStream(sock.getOutputStream());
            BufferedReader br =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
            char[] inputChars = new char[1024];
            
            sendMessage(pstream, "player1", 200);

            System.out.println("Connected to server");
            
            sendMessage(pstream, "ready", 201); 
            String asdf;

            
            // Some things to keep the loop up
            while(true) {

                asdf = br.readLine();
                
                //rc = br.read(inputChars);
                //if(rc < 0) {
                if(asdf == null) {
                    System.out.println("Socket disconnected");
                    break;
                }
                System.out.println(asdf);
            }

            sock.close();
        } catch (IOException e) {
            System.out.println("Error: Socket disconnected");
        }

    }
}
