// Cliente de archivos
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteArchivos {
    public static void main(String[] args) {
        String servidorIP = "127.0.0.1";
        int puerto = 5000;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Ingrese el nombre del archivo a solicitar: ");
            String nombreArchivo = scanner.nextLine();
            
            try (Socket socket = new Socket(servidorIP, puerto);
                 DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
                 DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                
                salida.writeUTF(nombreArchivo);
                String respuesta = entrada.readUTF();
                
                if ("EXISTE".equals(respuesta)) {
                    FileOutputStream fileOutput = new FileOutputStream("archivos_cliente/" + nombreArchivo);
                    byte[] buffer = new byte[4096];
                    int bytesLeidos;
                    
                    while ((bytesLeidos = entrada.read(buffer)) != -1) {
                        fileOutput.write(buffer, 0, bytesLeidos);
                    }
                    fileOutput.close();
                    System.out.println("Archivo recibido y guardado correctamente.");
                } else {
                    System.out.println("El archivo no existe en el servidor.");
                }
            } catch (IOException e) {
                System.out.println("Error en la conexión con el servidor: " + e.getMessage());
            }
        }
    }
}
