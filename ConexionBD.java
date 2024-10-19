package tortrix;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/bdtortrixsa";
    private static final String USER = "root";
    private static final String PASSWORD = "Elcorazon1.";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Menú Principal:");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Listar Productos");
            System.out.println("3. Actualizar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("5. Salir");
            System.out.print("eliga una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    agregarProducto(scanner);
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    actualizarProducto(scanner);
                    break;
                case 4:
                    eliminarProducto(scanner);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 5);
        
        scanner.close();
    }

    private static void agregarProducto(Scanner scanner) {
        System.out.print("Ingrese el nombre del producto: ");
        String nombreProducto = scanner.nextLine();
        System.out.print("Ingrese el precio unitario: ");
        double precioUnitario = scanner.nextDouble();
        System.out.print("Ingrese la cantidad: ");
        int cantidadProducto = scanner.nextInt();
        System.out.print("Ingrese la fecha de vencimiento (YYYY-MM-DD): ");
        String fechaVencimiento = scanner.next();

        String query = "INSERT INTO productos (nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nombreProducto);
            pstmt.setDouble(2, precioUnitario);
            pstmt.setInt(3, cantidadProducto);
            pstmt.setDate(4, Date.valueOf(fechaVencimiento));
            pstmt.executeUpdate();
            System.out.println("Producto agregado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarProductos() {
        String query = "SELECT * FROM productos";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("Código: " + rs.getInt("codigoProducto") +
                                   ", Nombre: " + rs.getString("nombreProducto") +
                                   ", Precio: " + rs.getDouble("precioUnitario") +
                                   ", Cantidad: " + rs.getInt("cantidadProducto") +
                                   ", Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarProducto(Scanner scanner) {
        System.out.print("Ingrese el código del producto a actualizar: ");
        int codigo = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        System.out.print("Ingrese el nuevo nombre del producto: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Ingrese el nuevo precio unitario: ");
        double nuevoPrecio = scanner.nextDouble();
        System.out.print("Ingrese la nueva cantidad: ");
        int nuevaCantidad = scanner.nextInt();
        System.out.print("Ingrese la nueva fecha de vencimiento (YYYY-MM-DD): ");
        String nuevaFecha = scanner.next();

        String query = "UPDATE productos SET nombreProducto = ?, precioUnitario = ?, cantidadProducto = ?, fechaVencimiento = ? WHERE codigoProducto = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nuevoNombre);
            pstmt.setDouble(2, nuevoPrecio);
            pstmt.setInt(3, nuevaCantidad);
            pstmt.setDate(4, Date.valueOf(nuevaFecha));
            pstmt.setInt(5, codigo);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto actualizado exitosamente.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminarProducto(Scanner scanner) {
        System.out.print("Ingrese el código del producto a eliminar: ");
        int codigo = scanner.nextInt();

        String query = "DELETE FROM productos WHERE codigoProducto = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, codigo);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto eliminado exitosamente.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

  /*  public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }// fin conectar()
    
    public static void insertarProducto(String codigo,String nombre, double precio, int cantidad, String fecha) {
    String query = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?,?, ?, ?, ?)";
    try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, codigo);
        pst.setString(2, nombre);
        pst.setDouble(3, precio);
        pst.setInt(4, cantidad);
        pst.setDate(5, java.sql.Date.valueOf(fecha));
        pst.executeUpdate();
        System.out.println("Producto insertado correctamente");
    } catch (SQLException e) {
     System.out.println("Error, el id ya existe"); 
    }
    }//fin insertarProducto()
    
    public static void listarProductos() {
    String query = "select * from producto;";  
    try (Connection con = ConexionBD.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
        boolean hayResultados = false;
        while (rs.next()) {
            hayResultados = true; 
            System.out.println("Código: " + rs.getString("codigoProducto"));
            System.out.println("Nombre: " + rs.getString("nombreProducto"));
            System.out.println("Precio: " + rs.getDouble("precioUnitario"));
            System.out.println("Cantidad: " + rs.getInt("cantidadProducto"));
            System.out.println("Fecha de Vencimiento: " + rs.getDate("fechaVencimiento"));     
            System.out.println("");
        }
        if (!hayResultados) {
            System.out.println("No hay productos disponibles.");
        }//fin if
     
    } catch (SQLException e) {
        
    }//fin catch
}// fin listarProductos()
    
    public static void buscarProducto(int codigoProducto) throws SQLException {
    String query = "SELECT * FROM producto WERE codigoProducto = ?";
    try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
        pst.setInt(1, codigoProducto);
      try (ResultSet rs = pst.executeQuery()){
        if (rs.next()){
            System.out.println("Id encontrdo");
            System.out.println("Código: "+rs.getInt("codigoProducto"));
            System.out.println("nombre: "+rs.getInt("nombreProducto"));
            System.out.println("precio: "+rs.getInt("precioUnitario"));
            System.out.println("cantidad: "+rs.getInt("cantidadProducto"));
            System.out.println("fecha vencimiento: "+rs.getInt("fechaVencimiento"));
        }else
            System.out.println("NO ESISTE EL RESGISTRO");
      }
    }
    } catch (SQLException e){
    System.err.println("Error el buscar id"+e.getMessage());
}
}// fin buscarProducto
    
public static void acturalizarProducto(int codigoProducto,String nombre, double precio){ 
    String query = "UPDATE producto SET nombreProducto = ?,precioUnitario = ? WHERE codigoProducto =?";
    try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
        pst.setString(1, codigoProducto);
        pst.executeUpdate();
        System.out.println("Producto eliminado correctamente");
    } catch (SQLException e) {
        //e.printStackTrace();
    }
}// fin eliminarProducto
    public static void main(String[] args) {
        //conectar();
        //insertarProducto("VRG100", "Virogrip", 50, 150, "2024-10-31");
        listarProductos();
        //actualizarProducto("VRG100","Virogrip funcional",150.99);
        //eliminarProducto("VRG100");
        //listarProductos();     
    }


 int opcion;
        do {
            System.out.println("****************************");
            System.out.println("****** Menu Principal ******");
            System.out.println("****************************");
            System.out.println("1) Ingresar Producto");
            System.out.println("2) Mostrar Producto");
            System.out.println("3) Buscar Producto");
            System.out.println("4) Actualizar Producto");
            System.out.println("5) Eliminar Producto");
            System.out.println("6) Salir");
            System.out.println("");
            System.out.print("Seleccione una opcion del menú: ");
            opcion = scanner.nextInt();
            System.out.println("");
            switch (opcion) {
                case 1: {
                    System.out.println("*** Ingresar Producto ***");
                    System.out.println("_________________________");
                    System.out.print("Código: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Precio Q: ");
                    double precio = scanner.nextDouble();
                    System.out.print("Cantidad: ");
                    int cantidad = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    System.out.print("Fecha de vencimiento (Año,mes,dia): ");
                    String fecha = scanner.nextLine();
                    insertarProducto(codigo, nombre, precio, cantidad, fecha);
                    
                    break;
                    
                }//case 1
                case 2: {
                    System.out.println("*** Mostrar Producto ***");
                    System.out.println("________________________");
                    ConexionBD.listarProductos();
                    break;
                }//case 2 
                case 3:{
                    System.out.println("*** Buscar Producto ***");
                    System.out.println("_______________________");
                    System.out.print("Ingrese el código del producto a buscar: ");
                    int codigoProducto = scanner.nextInt();
                    ConexionBD.buscarProducto(codigoProducto);
                    break;
                }
                case 4: {
                    System.out.println("*** Actualizar Producto ***");
                    System.out.println("___________________________");
                    System.out.print("Ingrese el código del producto a actualizar: ");
                    int codigoProducto = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer
                    System.out.print("Nuevo nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Nuevo precio: ");
                    double precio = scanner.nextDouble();
                    ConexionBD.actualizarProducto(codigoProducto, nombre, precio);
                    break;
                }//case 3 
                case 5: {
                    System.out.println("*** Eliminar Producto ***");
                    System.out.println("_________________________");
                    System.out.print("Ingrese el código del producto a eliminar: ");
                    int codigoProducto = scanner.nextInt();
                    ConexionBD.eliminarProducto(codigoProducto);
                    break;
                }//case 4
                case 6: {
                    System.exit(0);
                    break;
                }//case 4
                default:
                    System.out.println("Ingrese una opción válida del menú.");
            }//fin switch()

        } while (opcion !=6);
    }// main
// fin class

*/