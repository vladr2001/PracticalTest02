package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {
    EditText pokemonName;
    Button pokemonButton;
    ImageView image;
    TextView abilities;
    TextView types;
    EditText port;
    ServerThread serverThread;
    ClientThread clientThread;

    ButtonListener buttonListener = new ButtonListener();

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.pokemonButton) {
                String sPort = port.getEditableText().toString();
                if (sPort == null || sPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread != null && serverThread.getPort() == Integer.parseInt(sPort))
                    return;
                serverThread = new ServerThread(Integer.parseInt(sPort));
                if (serverThread.getServerSocket() == null) {
                    Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                    return;
                }
                serverThread.start();


                String pName = pokemonName.getEditableText().toString();
                if (pName == null || pName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }

                abilities.setText(Constants.EMPTY_STRING);
                types.setText(Constants.EMPTY_STRING);

                clientThread = new ClientThread("localhost", Integer.parseInt(sPort), pName, image, abilities, types);
                clientThread.start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        pokemonButton = (Button) findViewById(R.id.pokemonButton);
        pokemonName = (EditText) findViewById(R.id.pokemonName);
        image = (ImageView) findViewById(R.id.pokemonPic);
        abilities = (TextView) findViewById(R.id.pokemonAb);
        types = (TextView) findViewById(R.id.pokemonTypes);
        port = (EditText) findViewById(R.id.port);
        pokemonButton.setOnClickListener(buttonListener);
    }
}