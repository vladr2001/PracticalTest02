package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    Socket socket;
    private String address;
    private int port;
    private String pokemonName;
    ImageView image;
    TextView abilities;
    TextView types;

    public ClientThread(String address, int port, String pokemonName, ImageView image, TextView abilities, TextView types) {
        this.address = address;
        this.port = port;
        this.pokemonName = pokemonName;
        this.image = image;
        this.abilities = abilities;
        this.types = types;
    }

    public void run() {
        System.out.println("ajunge in client thread");
        try {
            try {
                socket = new Socket(address, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(pokemonName);
            printWriter.flush();
            String pokemonInfo;
            while ((pokemonInfo = bufferedReader.readLine()) != null) {
                final String finalInfo = pokemonInfo;

                abilities.post(new Runnable() {
                    @Override
                    public void run() {
                        abilities.setText("");
                    }
                });

                types.post(new Runnable() {
                    @Override
                    public void run() {
                        abilities.setText("");
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
