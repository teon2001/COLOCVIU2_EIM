package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private final String word;

    private final TextView anagramsTextView;

    private Socket socket;

    public ClientThread(String address, int port, String word, TextView anagramsTextView) {
        this.address = address;
        this.port = port;
        this.word = word;
        this.anagramsTextView = anagramsTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println(word);
            printWriter.flush();

            String anagrams;
            while ((anagrams = bufferedReader.readLine()) != null) {
                final String finalizedInformation = anagrams;
                anagramsTextView.post(() -> anagramsTextView.setText(finalizedInformation));
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
