package API;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class AuthHandler {


    public static void startStocket() {
        Socket socket = null;
        try {
            socket = IO.socket("http://localhost:8000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Socket finalSocket = socket;
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                finalSocket.emit("foo", "hi");
                finalSocket.disconnect();
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });
        socket.connect();

    }


}
