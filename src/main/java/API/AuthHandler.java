package API;

import callbacks.AuthCallback;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PropertiesComponent;
import io.socket.client.IO;
import io.socket.client.Socket;
import models.CustomError;

import java.net.URISyntaxException;

public class AuthHandler {


    public static void startSocket(String token, AuthCallback callback) {
        Socket socket = null;

        try {
            IO.Options opts = new IO.Options();
            socket = IO.socket("https://jglo.ricardomaltez.com", opts);
        } catch (URISyntaxException e) {
            callback.error(CustomError.SOCKET_ERROR);
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
//                    PropertiesComponent.getInstance().setValue("accessToken", accessToken);
                    saveAccessToken(accessToken);
                    finalSocket.close();
                    callback.success();
                })
                .on(Socket.EVENT_CONNECT_TIMEOUT, args -> {
                    finalSocket.close();
                    callback.error(CustomError.SOCKET_ERROR);
                })
                .on(Socket.EVENT_CONNECT_ERROR, args -> {
                    finalSocket.close();
                    callback.error(CustomError.SOCKET_ERROR);
                });

        try {
            finalSocket.connect();
        } catch (Exception e) {
            callback.error(CustomError.SOCKET_ERROR);
        }

    }



    private static void saveAccessToken(String accessToken) {
        CredentialAttributes attributes = new CredentialAttributes("jglo:accessToken", "accessToken", AuthHandler.class, false);
        PasswordSafe.getInstance().setPassword(attributes, accessToken);
    }

    public static void logOut(){
        saveAccessToken(null);
    }

    public static boolean isLoggedIn(){
        String accessToken = loadAccessToken();
        return accessToken != null;
    }


    public static String loadAccessToken() {
        CredentialAttributes attributes = new CredentialAttributes("jglo:accessToken", "accessToken", AuthHandler.class, false);
        return PasswordSafe.getInstance().getPassword(attributes);
    }
}
