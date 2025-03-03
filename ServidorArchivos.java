// Servidor de archivos
import java.io.*;
import java.net.*;

public class ServidorArchivos {
    public static void main(String[] args) {
        int puerto = 5000;
        String carpetaArchivos = "archivos_servidor"; // Carpeta donde estarán los archivos de prueba
        
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexión en el puerto " + puerto);
            
            while (true) {
                try (Socket cliente = servidor.accept();
                     DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                     DataOutputStream salida = new DataOutputStream(cliente.getOutputStream())) {
                    
                    System.out.println("Cliente conectado: " + cliente.getInetAddress());
                    
                    String nombreArchivo = entrada.readUTF();
                    File archivo = new File(carpetaArchivos + "/" + nombreArchivo);
                    
                    if (archivo.exists() && archivo.isFile()) {
                        salida.writeUTF("EXISTE");
                        FileInputStream fileInput = new FileInputStream(archivo);
                        byte[] buffer = new byte[4096];
                        int bytesLeidos;
                        
                        while ((bytesLeidos = fileInput.read(buffer)) != -1) {
                            salida.write(buffer, 0, bytesLeidos);
                        }
                        fileInput.close();
                        System.out.println("Archivo enviado: " + nombreArchivo);
                    } else {
                        salida.writeUTF("NO_EXISTE");
                        System.out.println("Archivo no encontrado: " + nombreArchivo);
                    }
                } catch (Exception e) {
                    System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
