import java.io.*;
import java.net.*;

public class WordleClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.out.println("Las palabras son de 5 letras.");
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            String respuesta;
            String palabra;
            boolean correcto = false;
            while (!correcto) {
                System.out.print("Ingresa tu conjetura: ");
                palabra = consoleReader.readLine();
                writer.println(palabra);
                respuesta = reader.readLine();
                System.out.println(respuesta);
                if (respuesta.equalsIgnoreCase("Correcto!")) {
                    correcto = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error conectándose al servidor: " + e.getMessage());
        }
    }
}