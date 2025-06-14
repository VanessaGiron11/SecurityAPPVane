package Grupo3A.persistencia;

import Grupo3A.dominio.Estudiante;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EstudianteDAO {
    private ConnectionManager conn; // Objeto para gestionar la conexión con la base de datos.
    private PreparedStatement ps;   // Objeto para ejecutar consultas SQL preparadas.
    private ResultSet rs;           // Objeto para almacenar el resultado de una consulta SQL.

    public EstudianteDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Estudiante create(Estudiante estudiante) throws SQLException {
        Estudiante res = null;
        PreparedStatement ps = null;
        try {
            ps = conn.connect().prepareStatement(
                    "INSERT INTO Estudiantes (nombre, edad, calificacion ) VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, estudiante.getNombre());
            ps.setInt(2, estudiante.getEdad());
            ps.setBigDecimal(3, estudiante.getCalificacion());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }
        return res;
    }

    public boolean update(Estudiante estudiante) throws SQLException {
        boolean res = false; // Variable para indicar si la actualización fue exitosa.
        try {
            // Preparar la sentencia SQL para actualizar la información de un usuario.
            ps = conn.connect().prepareStatement(
                    "UPDATE Estudiantes " +
                            "SET nombre = ?, edad = ?, calificacion = ? " +
                            "WHERE id = ?"
            );

            // Establecer los valores de los parámetros en la sentencia preparada.
            ps.setString(1, estudiante.getNombre());  // Asignar el nuevo nombre del usuario.
            ps.setInt(2, estudiante.getEdad()); // Asignar el nuevo correo electrónico del usuario.
            ps.setBigDecimal(3, estudiante.getCalificacion());
            ps.setInt(4, estudiante.getId()); // Asignar el nuevo estado del usuario.
            // Ejecutar la sentencia de actualización y verificar si se afectó alguna fila.

            if(ps.executeUpdate() > 0){
                res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la actualización fue exitosa.
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
        }catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al modificar estudiante: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return res; // Retornar el resultado de la operación de actualización.
    }
    /** * Elimina un usuario de la base de datos basándose en su ID. *   */

    public boolean delete(Estudiante estudiante) throws SQLException{
    boolean res = false; // Variable para indicar si la eliminación fue exitosa.
    try{
        // Preparar la sentencia SQL para eliminar un usuario por su ID.
        ps = conn.connect().prepareStatement(
                "DELETE FROM Estudiantes WHERE id = ?"
        );
        // Establecer el valor del parámetro en la sentencia preparada (el ID del usuario a eliminar).
        ps.setInt(1, estudiante.getId());

        // Ejecutar la sentencia de eliminación y verificar si se afectó alguna fila.
        if(ps.executeUpdate() > 0){
            res = true; // Si executeUpdate() retorna un valor mayor que 0, significa que la eliminación fue exitosa.
        }
        ps.close(); // Cerrar la sentencia preparada para liberar recursos.
    }catch (SQLException ex){
        // Capturar cualquier excepción SQL que ocurra durante el proceso.
        throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
    } finally {
        // Bloque finally para asegurar que los recursos se liberen.
        ps = null;         // Establecer la sentencia preparada a null.
        conn.disconnect(); // Desconectar de la base de datos.
    }

    return res; // Retornar el resultado de la operación de eliminación.
}
    /** * Buscar un usuario de la base de datos basándose en su ID. *   */

    public ArrayList<Estudiante> search(String nombre) throws SQLException{
        ArrayList<Estudiante> records  = new ArrayList<>(); // Lista para almacenar los usuarios encontrados.

        try {
            // Preparar la sentencia SQL para buscar usuarios por nombre (usando LIKE para búsqueda parcial).
            ps = conn.connect().prepareStatement("SELECT id, nombre, edad, calificacion " +
                    "FROM Estudiantes " +
                    "WHERE nombre LIKE ?");

            // Establecer el valor del parámetro en la sentencia preparada.
            // El '%' al inicio y al final permiten la búsqueda de la cadena 'name' en cualquier parte del nombre del usuario.
            ps.setString(1, "%" + nombre + "%");

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Iterar a través de cada fila del resultado.
            while (rs.next()){
                // Crear un nuevo objeto User para cada registro encontrado.
                Estudiante estudiante = new Estudiante();
                // Asignar los valores de las columnas a los atributos del objeto User.
                estudiante.setId(rs.getInt(1));       // Obtener el ID del usuario.
                estudiante.setNombre(rs.getString(2));   // Obtener el nombre del usuario.
                estudiante.setEdad(rs.getInt(3));
                estudiante.setCalificacion(rs.getBigDecimal(4));    // Obtener el estado del usuario.
                // Agregar el objeto estudiante a la lista de resultados.
                records.add(estudiante);
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }
        return records; // Retornar la lista de usuarios encontrados.
    }

/**
 * Obtiene un usuario de la base de datos basado en su ID.
 */
    public Estudiante getById(int id) throws SQLException{
        Estudiante estudiante  = new Estudiante(); // Inicializar un objeto User que se retornará.

        try {
            // Preparar la sentencia SQL para seleccionar un usuario por su ID.
            ps = conn.connect().prepareStatement("SELECT id, nombre, edad, calificacion " +
                    "FROM Estudiantes " +
                    "WHERE id = ?");

            // Establecer el valor del parámetro en la sentencia preparada (el ID a buscar).
            ps.setInt(1, id);

            // Ejecutar la consulta SQL y obtener el resultado.
            rs = ps.executeQuery();

            // Verificar si se encontró algún registro.
            if (rs.next()) {
                //  se encontró un usuario, asignar los valores de las columnas al objeto User.
                estudiante.setId(rs.getInt(1));       // Obtener el ID del usuario.
                estudiante.setNombre(rs.getString(2));   // Obtener el nombre del usuario.
                estudiante.setEdad(rs.getInt(3));  // Obtener el correo electrónico del usuario.
                estudiante.setCalificacion(rs.getBigDecimal(4));    // Obtener el estado del usuario.
            } else {
                // Si no se encontró ningún usuario con el ID especificado, establecer el objeto User a null.
                estudiante = null;
            }
            ps.close(); // Cerrar la sentencia preparada para liberar recursos.
            rs.close(); // Cerrar el conjunto de resultados para liberar recursos.
        } catch (SQLException ex){
            // Capturar cualquier excepción SQL que ocurra durante el proceso.
            throw new SQLException("Error al obtener un estudiante por id: " + ex.getMessage(), ex);
        } finally {
            // Bloque finally para asegurar que los recursos se liberen.
            ps = null;         // Establecer la sentencia preparada a null.
            rs = null;         // Establecer el conjunto de resultados a null.
            conn.disconnect(); // Desconectar de la base de datos.
        }

        return estudiante; // Retornar el objeto User encontrado o null si no existe.
    }

}
