package Grupo3A.Utils;

import java.nio.charset.StandardCharsets; // Clase que define juegos de caracteres estándar, como UTF-8, utilizado para codificar la contraseña antes de hashearla.
import java.security.MessageDigest;      // Clase que proporciona funcionalidades para algoritmos de resumen de mensajes criptográficos, como SHA-256, para hashear contraseñas.
import java.security.NoSuchAlgorithmException; // Clase para manejar excepciones que ocurren cuando un algoritmo criptográfico solicitado no está disponible en el entorno.
import java.util.Base64;

public class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            // Obtiene una instancia del algoritmo de resumen de mensajes SHA-256.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña a bytes utilizando la codificación UTF-8 y calcula su hash.
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Codifica el array de bytes del hash resultante a una cadena utilizando la codificación Base64
            // para que sea fácilmente almacenable y transportable.
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException ex) {
            // Captura la excepción que ocurre si el algoritmo SHA-256 no está disponible.
            // En este caso, retorna null para indicar que el hasheo falló.
            return null;
        }
    }
}