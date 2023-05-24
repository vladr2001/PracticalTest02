package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    public ServerThread getServerThread() {
        return serverThread;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bufferedReader == null || printWriter == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
            return;
        }
        Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
        String pokemon = null;
        try {
            pokemon = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (pokemon == null || pokemon.isEmpty()) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
            return;
        } else {
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Pokemon is " + pokemon);
        }
        HashMap<String, Pokemon> data = serverThread.getData();
        Pokemon pokemonData = null;

        if (data.containsKey(pokemon)) {
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
            pokemonData = data.get(pokemon);
        } else {
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + pokemon);
            System.out.println(httpGet.toString());
            HttpResponse httpGetResponse = null;
            try {
                httpGetResponse = httpClient.execute(httpGet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HttpEntity httpGetEntity = httpGetResponse.getEntity();
            System.out.println(httpGetEntity.toString());

            String pageSourceCode;

            if (httpGetEntity != null) {
                try {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                return;
            }
            System.out.println("pagesourcecode is " + pageSourceCode);

        }
    }
}
