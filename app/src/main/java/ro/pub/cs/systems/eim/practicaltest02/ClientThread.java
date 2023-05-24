package ro.pub.cs.systems.eim.practicaltest02;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

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
                final String finalInfo1 = pokemonInfo;
                final String finalInfo2;
                final String finalInfo3;

                abilities.post(new Runnable() {
                    @Override
                    public void run() {
                        abilities.setText(finalInfo1);
                    }
                });

                finalInfo2 = bufferedReader.readLine();
                Log.i(Constants.TAG, finalInfo1);

                types.post(new Runnable() {
                    @Override
                    public void run() {
                        types.setText(finalInfo2);
                    }
                });
                Log.i(Constants.TAG, finalInfo2);

                finalInfo3 = bufferedReader.readLine();
                Log.i(Constants.TAG, finalInfo3);

                /*image.post(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGetCartoon = new HttpGet(finalInfo3);
                        HttpResponse httpResponse = null;
                        try {
                            httpResponse = httpClient.execute(httpGetCartoon);
                            HttpEntity httpEntity = httpResponse.getEntity();
                            image.setImageBitmap(BitmapFactory.decodeStream(httpEntity.getContent()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });*/
                break;
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
