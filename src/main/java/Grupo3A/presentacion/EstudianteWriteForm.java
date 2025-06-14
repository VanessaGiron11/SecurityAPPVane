package Grupo3A.presentacion;

import Grupo3A.persistencia.EstudianteDAO; // Importa la interfaz o clase UserDAO, que define las operaciones de acceso a datos para la entidad User.
import Grupo3A.Utils.CBOption; // Importa la clase CBOption, probablemente una clase utilitaria para manejar opciones de un ComboBox (por ejemplo, para asociar un valor con un texto).
import Grupo3A.Utils.CUD;
import javax.swing.*;

import Grupo3A.dominio.Estudiante;
import Grupo3A.persistencia.UserDAO;

public class EstudianteWriteForm extends JDialog{
    private JPanel mainPanel;
    private JTextField textNombre;
    private JTextField textEdad;
    private JTextField textCalificacion;
    private JButton btnOK;
    private JButton btnCancelar;

    private EstudianteDAO estudianteDAO; // Instancia de la clase UserDAO para interactuar con la base de datos de usuarios.
    private MainForm mainForm; // Referencia a la ventana principal de la aplicación.
    private CUD cud; // Variable para almacenar el tipo de operación (Create, Update, Delete) que se está realizando en este formulario.
    private Estudiante en; // Variable para almacenar el objeto User que se está creando, actualizando o eliminando.

    // Constructor de la clase UserWriteForm. Recibe la ventana principal, el tipo de operación CUD y un objeto User como parámetros.
    public EstudianteWriteForm(MainForm mainForm, CUD cud, Estudiante estudiante) {
        this.cud = cud; // Asigna el tipo de operación CUD recibida a la variable local 'cud'.
        this.en = estudiante; // Asigna el objeto User recibido a la variable local 'en'.
        this.mainForm = mainForm; // Asigna la instancia de MainForm recibida a la variable local 'mainForm'.
        estudianteDAO = new EstudianteDAO(); // Crea una nueva instancia de UserDAO al instanciar este formulario.
        setContentPane(mainPanel); // Establece el panel principal como el contenido de este diálogo.
        setModal(true); // Hace que este diálogo sea modal, bloqueando la interacción con la ventana principal hasta que se cierre.
        init(); // Llama al método 'init' para inicializar y configure the form based on 'cud'
        pack(); // Ajusta el tamaño de la ventana para que todos sus componentes se muestren correctamente.
        setLocationRelativeTo(mainForm); // Centra la ventana del diálogo relative a la ventana principal.

        // Agrega un ActionListener al botón 'btnCancel' para cerrar la ventana actual (UserWriteForm).
        btnCancelar.addActionListener(s -> this.dispose());
        // Agrega an ActionListener to the 'btnOk' to trigger the save/update/delete action
        btnOK.addActionListener(s -> ok());
    }

    private void init() {
        switch (this.cud) {
            case CREATE:
                // Si la operación es de creación, establece el título de la ventana como "Crear Usuario".
                setTitle("Crear Estudiante");
                // Establece el texto del botón de acción principal (btnOk) como "Guardar".
                btnOK.setText("Guardar");
                break;
            case UPDATE:
                // Si la operación es de actualización, establece el título de la ventana como "Modificar Usuario".
                setTitle("Modificar Estudiante");
                // Establece el texto del botón de acción principal (btnOk) como "Guardar".
                btnOK.setText("Guardar");
                break;
            case DELETE:
                // Si la operación es de eliminación, establece el título de la ventana como "Eliminar Usuario".
                setTitle("Eliminar Estudiante");
                // Establece el texto del botón de acción principal (btnOk) como "Eliminar".
                btnOK.setText("Eliminar");
                break;
        }
        setValuesControls(this.en);
    }

    private void setValuesControls(Estudiante estudiante) {
        // Llena el campo de texto 'txtName' con el nombre del usuario.
        textNombre.setText(String.valueOf(estudiante.getNombre()));
        textEdad.setText(String.valueOf(estudiante.getEdad()));
        textCalificacion.setText(String.valueOf(estudiante.getCalificacion()));
        // Si la operación actual es la eliminación de un usuario (CUD.DELETE).
        if (this.cud == CUD.DELETE) {
            // Deshabilita la edición del campo de texto 'txtName' para evitar modificaciones.
            textNombre.setEditable(false);
            // Deshabilita la edición del campo de texto 'txtEmail' para evitar modificaciones.
            textEdad.setEditable(false);
            // Deshabilita el ComboBox 'cbStatus' para evitar cambios en el estatus.
            textCalificacion.setEditable(false);
        }
    }

    private boolean getValuesControls() {
        boolean res = false;

        if (textNombre.getText().trim().isEmpty()) {
            return res;
        } else if (textEdad.getText().trim().isEmpty()) {
            return res;
        } else if (textCalificacion.getText().trim().isEmpty()) {
            return res;
        } else if (this.cud != CUD.CREATE && this.en.getId() == 0) {
            return res;
        }

        res = true;
        // Establece el nombre del usuario.
        this.en.setNombre(textNombre.getText());
        this.en.setEdad(Integer.parseInt(textEdad.getText())); // CORREGIDO: antes estaba mal asignado
        this.en.setCalificacion(new java.math.BigDecimal(textCalificacion.getText()));

        return res;
    }

    private void ok() {
        try {
            // Obtener y validar los valores de los controles del formulario.
            boolean res = getValuesControls();

            // Si la validación de los controles fue exitosa.
            if (res) {
                boolean r = false; // Variable para almacenar el resultado de la operación de la base de datos.

                // Realiza la operación de la base de datos según el tipo de operación actual (CREATE, UPDATE, DELETE).
                switch (this.cud) {
                    case CREATE:
                        // Caso de creación de un nuevo usuario.
                        // Llama al método 'create' de userDAO para persistir el nuevo usuario (this.en).
                        Estudiante estudiante = estudianteDAO.create(this.en);
                        // Verifica si la creación fue exitosa comprobando si el nuevo usuario tiene un ID asignado.
                        if (estudiante.getId() > 0) {
                            r = true; // Establece 'r' a true si la creación fue exitosa.
                        }
                        break;
                    case UPDATE:
                        // Caso de actualización de un usuario existente.
                        // Llama al método 'update' de userDAO para guardar los cambios del usuario (this.en).
                        r = estudianteDAO.update(this.en); // 'r' será true si la actualización fue exitosa, false en caso contrario.
                        break;
                    case DELETE:
                        // Caso de eliminación de un usuario.
                        // Llama al método 'delete' de userDAO para eliminar el usuario (this.en).
                        r = estudianteDAO.delete(this.en); // 'r' será true si la eliminación fue exitosa, false en caso contrario.
                        break;
                }

                // Si la operación de la base de datos (creación, actualización o eliminación) fue exitosa.
                if (r) {
                    // Muestra un mensaje de éxito al usuario.
                    JOptionPane.showMessageDialog(null,
                            "Transacción realizada exitosamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    // Cierra la ventana actual (UserWriteForm).
                    this.dispose();
                } else {
                    // Si la operación de la base de datos falló.
                    JOptionPane.showMessageDialog(null,
                            "No se logró realizar ninguna acción",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    return; // Sale del método.
                }
            } else {
                // Si la validación de los controles falló (algún campo obligatorio está vacío o inválido).
                JOptionPane.showMessageDialog(null,
                        "Los campos con * son obligatorios",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return; // Sale del método.
            }
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return; // Sale del método.
        }
    }
}

