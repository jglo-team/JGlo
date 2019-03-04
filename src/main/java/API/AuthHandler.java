package API;

import com.intellij.ide.util.PropertiesComponent;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class AuthHandler {


    public static void startSocket(String token) {
        Socket socket = null;
        try {
            socket = IO.socket("http://localhost:9090");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Socket finalSocket = socket;
        finalSocket
                .on(Socket.EVENT_CONNECT, args -> {
                    System.out.println(token);
                    finalSocket.emit("token:set", token);
                })
                .on(Socket.EVENT_DISCONNECT, args -> System.out.println("Disconnected"))
                .on("accessToken", args -> {
                    String accessToken = args[0].toString();
                    PropertiesComponent.getInstance().setValue("accessToken", accessToken);
                    finalSocket.close();
                });

        finalSocket.connect();

    }


}
