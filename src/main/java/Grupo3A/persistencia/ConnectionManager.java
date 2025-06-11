package Grupo3A.persistencia;

import java.sql.Connection; // Representa una conexión a la base de datos.
import java.sql.DriverManager; // Gestiona los drivers JDBC y establece conexiones.
import java.sql.SQLException;

public class ConnectionManager {


    private static final String STR_CONNECTION = "jdbc:mysql://127.0.0.1:3306/securitydb2025?" +
            "user=root&" +
            "password=luna11";

    private Connection connection;

    private static ConnectionManager instance;

    private ConnectionManager() {
        this.connection = null;
        try {
            // Carga el driver JDBC de Microsoft SQL Server. Esto es necesario para que Java pueda
            // comunicarse con la base de datos SQL Server.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Si el driver no se encuentra, se lanza una excepción indicando el error.
            throw new RuntimeException("Error al cargar el driver JDBC de MYSQL", e);
        }
    }

    public synchronized Connection connect() throws SQLException {
        // Verifica si la conexión ya existe y si no está cerrada.
        if (this.connection == null || this.connection.isClosed()) {
            try {
                // Intenta establecer la conexión utilizando la cadena de conexión.
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            } catch (SQLException exception) {
                // Si ocurre un error durante la conexión, se lanza una excepción SQLException
                // con un mensaje más descriptivo que incluye el mensaje original de la excepción.
                throw new SQLException("Error al conectar a la base de datos: " + exception.getMessage(), exception);
            }
        }
        // Retorna la conexión (ya sea la existente o la recién creada).
        return this.connection;
    }

    public void disconnect() throws SQLException {
        // Verifica si la conexión existe (no es nula).
        if (this.connection != null) {
            try {
                // Intenta cerrar la conexión.
                this.connection.close();
            } catch (SQLException exception) {
                // Si ocurre un error al cerrar la conexión, se lanza una excepción SQLException
                // con un mensaje más descriptivo.
                throw new SQLException("Error al cerrar la conexión: " + exception.getMessage(), exception);
            } finally {
                // El bloque finally se ejecuta siempre, independientemente de si hubo una excepción o no.
                // Aquí se asegura que la referencia a la conexión se establezca a null,
                // indicando que ya no hay una conexión activa gestionada por esta instancia.
                this.connection = null;
            }
        }
    }

    public static synchronized ConnectionManager getInstance() {
        // Verifica si la instancia ya ha sido creada.
        if (instance == null) {
            // Si no existe, crea una nueva instancia de JDBCConnectionManager.
            instance = new ConnectionManager();
        }
        // Retorna la instancia existente (o la recién creada).
        return instance;
    }

}
