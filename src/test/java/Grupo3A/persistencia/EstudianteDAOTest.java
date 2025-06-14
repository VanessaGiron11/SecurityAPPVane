package Grupo3A.persistencia;

import org.junit.jupiter.api.BeforeEach; // Anotación para indicar que el método se ejecuta antes de cada método de prueba.
import org.junit.jupiter.api.Test;
import Grupo3A.dominio.Estudiante;

import java.math.BigDecimal;
import java.util.ArrayList;              // Clase para crear listas dinámicas de objetos, utilizada en algunas pruebas. // Clase para generar números aleatorios, útil para crear datos de prueba.

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteDAOTest {
    private EstudianteDAO estudianteDAO; // Instancia de la clase UserDAO que se va a probar.

    @BeforeEach
    void setUp() {
        // Método que se ejecuta antes de cada método de prueba (@Test).
        // Su propósito es inicializar el entorno de prueba, en este caso,
        // creando una nueva instancia de UserDAO para cada prueba.
        estudianteDAO = new EstudianteDAO();
    }

    private Estudiante create(Estudiante estudiante) throws SQLException {
        // Llama al método 'create' del UserDAO para persistir el usuario en la base de datos (simulada).
        Estudiante res = estudianteDAO.create(estudiante);
        // Realiza aserciones para verificar que la creación del usuario fue exitosa
        // y que los datos del usuario retornado coinciden con los datos originales.
        assertNotNull(res, "El usuario creado no debería ser nulo."); // Verifica que el objeto retornado no sea nulo.
        assertEquals(estudiante.getNombre(), res.getNombre(), "El nombre del usuario creado debe ser igual al original.");
        assertEquals(estudiante.getEdad(), res.getEdad(), "La edad del usuario creado debe ser igual al original.");
        assertEquals(estudiante.getCalificacion(), res.getCalificacion(), "La calificacion del usuario creado debe ser igual al original.");
        // Retorna el objeto estudiante creado (tal como lo devolvió el UserDAO).
        return res;
    }

    private void update(Estudiante estudiante) throws SQLException{
        // Modifica los atributos del objeto User para simular una actualización.
        estudiante.setNombre(estudiante.getNombre() + "_u"); // Añade "_u" al final del nombre.
        estudiante.setEdad(estudiante.getEdad() + 1);
        String calificacionConU = "u" + estudiante.getCalificacion().toString();

        // Llama al método 'update' del UserDAO para actualizar el usuario en la base de datos (simulada).
        boolean res = estudianteDAO.update(estudiante);

        // Realiza una aserción para verificar que la actualización fue exitosa.
        assertTrue(res, "La actualización del usuario debería ser exitosa.");

        // Llama al método 'getById' para verificar que los cambios se persistieron correctamente.
        // Aunque el método 'getById' ya tiene sus propias aserciones, esta llamada adicional
        // ayuda a asegurar que la actualización realmente tuvo efecto en la capa de datos.
        getById(estudiante);
    }

    private void getById(Estudiante estudiante) throws SQLException {
        // Llama al método 'getById' del UserDAO para obtener un usuario por su ID.
        Estudiante res = estudianteDAO.getById(estudiante.getId());

        // Realiza aserciones para verificar que el usuario obtenido coincide
        // con el usuario original (o el usuario modificado en pruebas de actualización).
        assertNotNull(res, "El estudiante obtenido por ID no debería ser nulo.");
        assertEquals(estudiante.getId(), res.getId(), "El ID del estudiante obtenido debe ser igual al original.");
        assertEquals(estudiante.getNombre(), res.getNombre(), "El nombre del estudiante obtenido debe ser igual al esperado.");
        assertEquals(estudiante.getEdad(), res.getEdad(), "La edad del estudiante obtenido debe ser igual al esperado.");
        assertEquals(estudiante.getCalificacion(), res.getCalificacion(), "La calificacion del estudiante obtenido debe ser igual al esperado.");
    }

    private void search(Estudiante estudiante) throws SQLException {
        // Llama al método 'search' del UserDAO para buscar usuarios por nombre.
        ArrayList<Estudiante> estudiantes = estudianteDAO.search(estudiante.getNombre());
        boolean find = false; // Variable para rastrear si se encontró un usuario con el nombre buscado.

        // Itera sobre la lista de usuarios devuelta por la búsqueda.
        for (Estudiante estudianteItem : estudiantes) {
            // Verifica si el nombre de cada usuario encontrado contiene la cadena de búsqueda.
            if (estudianteItem.getNombre().contains(estudiante.getNombre())) {
                find = true; // Si se encuentra una coincidencia, se establece 'find' a true.
            }
            else{
                find = false; // Si un nombre no contiene la cadena de búsqueda, se establece 'find' a false.
                break;      // Se sale del bucle, ya que se esperaba que todos los resultados contuvieran la cadena.
            }
        }
        // Realiza una aserción para verificar que todos los usuarios con el nombre buscado fue encontrado.
        assertTrue(find, "el nombre buscado no fue encontrado : " + estudiante.getNombre());
    }
    private void delete(Estudiante estudiante) throws SQLException{
        // Llama al método 'delete' del UserDAO para eliminar un usuario por su ID.
        boolean res = estudianteDAO.delete(estudiante);

        // Realiza una aserción para verificar que la eliminación fue exitosa.
        assertTrue(res, "La eliminación del estudiante debería ser exitosa.");

        // Intenta obtener el usuario por su ID después de la eliminación.
        Estudiante res2 = estudianteDAO.getById(estudiante.getId());

        // Realiza una aserción para verificar que el usuario ya no existe en la base de datos
        // después de la eliminación (el método 'getById' debería retornar null).
        assertNull(res2, "El estudiante debería haber sido eliminado y no encontrado por ID.");
    }





}