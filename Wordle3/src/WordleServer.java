import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class WordleServer {
    private static final String WORDS_FILE = "words.txt";
    private Set<String> words;
    private GuessHandler guessHandler;

    public WordleServer() throws IOException {
        words = new HashSet<>(Files.readAllLines(Paths.get(WORDS_FILE)));
        String randomWord = getRandomWord(5);
        guessHandler = new GuessHandler(randomWord);
        startListening();
    }

    private void startListening() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket socket = serverSocket.accept();
            handleConnection(socket);
        }
    }

    private void handleConnection(Socket socket) {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
        ) {
            guessHandler.printInstructions();
            String respuesta;
            do {
                respuesta = reader.readLine();
                String pista = guessHandler.handleGuess(respuesta);
                writer.println(pista);
            } while (!guessHandler.isWordGuessed() && !respuesta.equalsIgnoreCase("Correcto!"));
        } catch (IOException e) {
            System.err.println("Error manejando la conexión: " + e.getMessage());
        }
    }

    private String getRandomWord(int longitud) {
        List<String> listaPalabras = words.stream()
                .filter(w -> w.length() == longitud)
                .collect(Collectors.toList());
        return listaPalabras.get(new Random().nextInt(0, listaPalabras.size()));
    }

    public static void main(String[] args) throws IOException {
        new WordleServer();
    }
}