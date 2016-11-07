import java.net.Socket;
import java.net.ConnectException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Client {
    static int Connected = 0;
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
            pstream.println("player1");

            char[] inputChars = new char[1024];

            System.out.println("Trying to read");
            br.read(inputChars);
            System.out.println(inputChars);
            
            // Some things to keep the loop up
            while(true) {
                rc = br.read(inputChars);
                if(rc < 0) {
                    System.out.println("Socket disconnected");
                    break;
                }
                System.out.println(inputChars);
            }

            sock.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
