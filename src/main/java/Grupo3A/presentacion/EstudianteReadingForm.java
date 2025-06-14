package Grupo3A.presentacion;

import Grupo3A.dominio.User;
import Grupo3A.persistencia.EstudianteDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Importa la clase DefaultTableModel, utilizada para crear y manipular modelos de datos para JTable.
import Grupo3A.dominio.Estudiante; // Importa la clase User, que representa la entidad de usuario en el dominio de la aplicación.
import Grupo3A.Utils.CUD; // Importa el enum  CUD (Create, Update, Delete).
import Grupo3A.persistencia.UserDAO;

import java.awt.event.KeyAdapter; // Importa la clase KeyAdapter, una clase adaptadora para recibir eventos de teclado.
import java.awt.event.KeyEvent; // Importa la clase KeyEvent, que representa un evento de teclado.
import java.util.ArrayList; // Importa la clase ArrayList, una implementación de la interfaz List que permite almacenar colecciones dinámicas de objetos.


public class EstudianteReadingForm extends JDialog {
    private JTextField textNombre;
    private JButton btnCrear;
    private JTable tbEstudiante;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JPanel MainPanel;

    private EstudianteDAO estudianteDAO; // Instancia de UserDAO para realizar operaciones de base de datos de usuarios.
    private MainForm mainForm; // Referencia a la ventana principal de la aplicación.

    public EstudianteReadingForm(MainForm mainForm) {
        this.mainForm = mainForm; // Asigna la instancia de MainForm recibida a la variable local.
        estudianteDAO = new EstudianteDAO(); // Crea una nueva instancia de UserDAO al instanciar este formulario.
        setContentPane(MainPanel); // Establece el panel principal como el contenido de este diálogo.
        setModal(true); // Hace que este diálogo sea modal, bloqueando la interacción con la ventana principal hasta que se cierre.
        setTitle("Buscar Estudiante"); // Establece el título de la ventana del diálogo.
        pack(); // Ajusta el tamaño de la ventana para que todos sus componentes se muestren correctamente.
        setLocationRelativeTo(mainForm);// Centra la ventana del diálogo relative a la ventana principal.

        // Agrega un listener de teclado al campo de texto txtNombre.
        textNombre.addKeyListener(new KeyAdapter() {
            // Sobrescribe el método keyReleased, que se llama cuando se suelta una tecla.
            @Override
            public void keyReleased(KeyEvent e) {
                // Verifica si el campo de texto txtNombre no está vacío.
                if (!textNombre.getText().trim().isEmpty()) {
                    // Llama al método search para buscar usuarios según el texto ingresado.
                    search(textNombre.getText());
                } else {
                    // Si el campo de texto está vacío, crea un modelo de tabla vacío y lo asigna a la tabla de usuarios para limpiarla.
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    tbEstudiante.setModel(emptyModel);
                }
            }
        });

        // Agrega un ActionListener al botón btnCreate.
        btnCrear.addActionListener(s -> {
            // Crea una nueva instancia de UserWriteForm para la creación de un nuevo usuario, pasando la MainForm, la constante CREATE de CUD y un nuevo objeto User vacío.
            EstudianteWriteForm estudianteWriteForm = new EstudianteWriteForm(this.mainForm, CUD.CREATE, new Estudiante());
            // Hace visible el formulario de escritura de usuario.
            estudianteWriteForm.setVisible(true);
            // Limpia la tabla de usuarios creando y asignando un modelo de tabla vacío  para refrescar la lista después de la creación.
            DefaultTableModel emptyModel = new DefaultTableModel();
            tbEstudiante.setModel(emptyModel);
        });

        // Agrega un ActionListener al botón btnUpdate.
        btnUpdate.addActionListener(s -> {
            // Llama al método getUserFromTableRow para obtener el usuario seleccionado en la tabla.
            Estudiante estudiante = getEstudianteFromTableRow();
            // Verifica si se seleccionó un usuario en la tabla (getUserFromTableRow no devolvió null).
            if (estudiante != null) {
                // Crea una nueva instancia de UserWriteForm para la actualización del usuario seleccionado, pasando la MainForm, la constante UPDATE de CUD y el objeto User obtenido.
                EstudianteWriteForm estudianteWriteForm = new EstudianteWriteForm(this.mainForm, CUD.UPDATE, estudiante);
                // Hace visible el formulario de escritura de usuario.
                estudianteWriteForm.setVisible(true);
                // Limpia la tabla de usuarios creando y asignando un modelo de tabla vacío para refrescar la lista después de la actualización.
                DefaultTableModel emptyModel = new DefaultTableModel();
                tbEstudiante.setModel(emptyModel);
            }
        });

        // Agrega un ActionListener al botón btnEliminar.
        btnDelete.addActionListener(s -> {
            // Llama al método getUserFromTableRow para obtener el usuario seleccionado en la tabla.
            Estudiante estudiante = getEstudianteFromTableRow();
            // Verifica si se seleccionó un usuario en la tabla (getUserFromTableRow no devolvió null).
            if (estudiante != null) {
                // Crea una nueva instancia de UserWriteForm para la eliminación del usuario seleccionado, pasando la MainForm, la constante DELETE de CUD y el objeto User obtenido.
                EstudianteWriteForm estudianteWriteForm = new EstudianteWriteForm(this.mainForm, CUD.DELETE, estudiante);
                // Hace visible el formulario de escritura de usuario.
                estudianteWriteForm.setVisible(true);
                // Limpia la tabla de usuarios creando y asignando un modelo de tabla vacío  para refrescar la lista después de la eliminación.
                DefaultTableModel emptyModel = new DefaultTableModel();
                tbEstudiante.setModel(emptyModel);
            }
        });
    }

    private void search(String query) {
        try {
            // Llama al método 'search' del UserDAO para buscar usuarios cuya información
            // coincida con la cadena de búsqueda 'query'. La implementación específica
            ArrayList<Estudiante> estudiantes = estudianteDAO.search(query);
            // Llama al método 'createTable' para actualizar la tabla de usuarios
            // en la interfaz gráfica con los resultados de la búsqueda.
            createTable(estudiantes);
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso de búsqueda
            // (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error al usuario.
            return; // Sale del método 'search' después de mostrar el error.
        }
    }

    public void createTable(ArrayList<Estudiante> estudiantes) {

        // Crea un nuevo modelo de tabla por defecto (DefaultTableModel).
        // Se sobrescribe el método isCellEditable para hacer que todas las celdas de la tabla no sean editables.
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Retorna para indicar que ninguna celda debe ser editable.
            }
        };

        // Define las columnas de la tabla. Los nombres de las columnas corresponden
        // a los atributos que se mostrarán de cada objeto User.
        model.addColumn("Id");
        model.addColumn("Nombre");
        model.addColumn("Edad");
        model.addColumn("Calificacion");

        // Establece el modelo de tabla creado como el modelo de datos para la
        // JTable 'tableUsers' (la tabla que se muestra en la interfaz gráfica).
        this.tbEstudiante.setModel(model);

        // Declara un array de objetos 'row' que se utilizará temporalmente para agregar filas.
        Object row[] = null;

        // Itera a través de la lista de objetos User proporcionada.
        for (int i = 0; i < estudiantes.size(); i++) {
            // Obtiene el objeto User actual de la lista.
            Estudiante estudiante = estudiantes.get(i);
            // Agrega una nueva fila vacía al modelo de la tabla.
            model.addRow(row);
            // Establece el valor del ID del usuario en la celda correspondiente de la fila actual (columna 0).
            model.setValueAt(estudiante.getId(), i, 0);
            // Establece el valor del nombre del usuario en la celda correspondiente de la fila actual (columna 1).
            model.setValueAt(estudiante.getNombre(), i, 1);
            // Establece el valor del email del usuario en la celda correspondiente de la fila actual (columna 2).
            model.setValueAt(estudiante.getEdad(), i, 2);
            // Establece el valor del estatus del usuario (probablemente obtenido a través de un método 'getStrEstatus()')
            // en la celda correspondiente de la fila actual (columna 3).
            model.setValueAt(estudiante.getCalificacion(), i, 3);
        }

        // Llama al método 'hideCol' para ocultar la columna con índice 0 (la columna del ID).
        // Esto es común cuando el ID es necesario internamente pero no se quiere mostrar al usuario.
        hideCol(0);
    }
    private void hideCol(int pColumna) {
        // Obtiene el modelo de columnas de la JTable y establece el ancho máximo de la columna especificada a 0.
        // Esto hace que la columna no sea visible en la vista de datos de la tabla.
        this.tbEstudiante.getColumnModel().getColumn(pColumna).setMaxWidth(0);
        // Establece el ancho mínimo de la columna especificada a 0.
        // Esto asegura que la columna no ocupe espacio incluso si el layout manager intenta ajustarla.
        this.tbEstudiante.getColumnModel().getColumn(pColumna).setMinWidth(0);
        // Realiza las mismas operaciones para el encabezado de la tabla.
        // Esto asegura que el nombre de la columna también se oculte y no ocupe espacio en la parte superior de la tabla.
        this.tbEstudiante.getTableHeader().getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tbEstudiante.getTableHeader().getColumnModel().getColumn(pColumna).setMinWidth(0);
    }

    // Método privado para obtener el objeto User seleccionado de la fila de la tabla.
    private Estudiante getEstudianteFromTableRow() {
        Estudiante estudiante = null; // Inicializa la variable user a null.
        try {
            // Obtiene el índice de la fila seleccionada en la tabla.
            int filaSelect = this.tbEstudiante.getSelectedRow();
            int id = 0; // Inicializa la variable id a 0.

            // Verifica si se ha seleccionado alguna fila en la tabla.
            if (filaSelect != -1) {
                // Si se seleccionó una fila, obtiene el valor de la primera columna  ID de esa fila.
                id = (int) this.tbEstudiante.getValueAt(filaSelect, 0);
            } else {
                // Si no se seleccionó ninguna fila, muestra un mensaje de advertencia al usuario.
                JOptionPane.showMessageDialog(null,
                        "Seleccionar una fila de la tabla.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null; // Retorna null ya que no se puede obtener un usuario sin una fila seleccionada.
            }

            // Llama al método 'getById' del UserDAO para obtener el objeto User correspondiente al ID obtenido de la tabla.
            estudiante = estudianteDAO.getById(id);

            // Verifica si se encontró un usuario con el ID proporcionado.
            if (estudiante.getId() == 0) {
                // Si el ID del usuario devuelto es 0 (o alguna otra indicación de que no se encontró),
                // muestra un mensaje de advertencia al usuario.
                JOptionPane.showMessageDialog(null,
                        "No se encontró ningún estudiante.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null; // Retorna null ya que no se encontró ningún usuario con ese ID.
            }

            // Si se encontró un usuario, lo retorna.
            return estudiante;
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error al usuario con la descripción de la excepción.
            return null; // Retorna null en caso de error.
        }
    }
}
