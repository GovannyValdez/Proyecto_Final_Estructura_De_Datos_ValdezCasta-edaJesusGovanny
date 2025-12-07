import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;


interface ahorcadoInterface {
	
    public void verificarArchivo();
    public void crearArchivo();
    public void llenarArchivo();
    public void eliminarArchivo();
}

class Oportunidades {
	
    private byte intentos = 0;
    
    public byte getIntentos() {
        return intentos;
    }
    
    public void setIntentos(byte intentos) {
        this.intentos = intentos;
    }
    
    public void descontarIntentos() {
        intentos = (byte)(intentos - 1);
    }
    
    public void letrañ() {
        intentos = (byte)(intentos + 1);
    }
    
}

class Pila{
	
	
	 	private String[] elementos;
	    private int tope;
	    private int capacidad;
	    
	    public Pila(int capacidad) {
	    	
	        this.capacidad = capacidad;
	        this.elementos = new String[capacidad];
	        this.tope = -1;
	    }
	    
	    
	    
	    public void push(String elemento) {
	        if (tope < capacidad - 1) {
	            tope++;
	            elementos[tope] = elemento;
	        }
	    }

	    
	    public boolean estaVacia() {
	        return tope == -1;
	    }

	    
	    
	    public String pop() {
	        if (!estaVacia()) {
	            String elemento = elementos[tope];
	            tope--;
	            return elemento;
	        }
	        return null;
	    }

	    
	    public String peek() {
	    	
	        if (!estaVacia()) {
	            return elementos[tope];
	        }
	        return null;
	    }

	    
	    public int tamanio() {
	    	
	        return tope + 1;
	    }
	    
	    public String[] obtenerElementos() {
	    	
	        String[] resultado = new String[tope + 1];
	        for (int i = 0; i <= tope; i++) {
	            resultado[i] = elementos[i];
	        }
	        return resultado;
	    }


	    public boolean contiene(String elemento) {
	    	
	        for (int i = 0; i <= tope; i++) {
	            if (elementos[i] != null && elementos[i].equals(elemento)) {
	                return true;
	            }
	        }
	        return false;
	    }
	

	
}

class ManejadorArchivo{
	
	private String nombreArchivo;
    
    public ManejadorArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    
    
    
    public String leerArchivo() {
        FileReader lectura = null;
        BufferedReader br = null;
        String contenido = null;
        
        try {
            lectura = new FileReader(nombreArchivo);
            br = new BufferedReader(lectura);
            contenido = br.readLine();
        } catch (Exception ex) {
            System.out.println("Error al leer el archivo");
        } finally {
            try {
                if (br != null) br.close();
                if (lectura != null) lectura.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contenido;
    }

    
    
    
    public boolean escribirArchivo(String contenido) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(nombreArchivo);
            pw.println(contenido);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error al escribir en el archivo");
            return false;
        } finally {
            if (pw != null) pw.close();
        }
    }
    
    
    
    
    public boolean existeArchivo() {
        File file = new File(nombreArchivo);
        return file.exists();
    }

    
    
    public boolean crearArchivo() {
        File archivo = new File(nombreArchivo);
        try {
            return archivo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    
    public boolean eliminarArchivo() {
        File file = new File(nombreArchivo);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
    
    
    
    
    public String[] cargarPalabras() {
        String contenido = leerArchivo();
        
        if (contenido != null && !contenido.isEmpty()) {
            String separacion = "";
            for (int i = 0; i < contenido.length(); i++) {
                if (!(contenido.substring(i, i+1).equals("/"))) {
                    separacion = separacion + contenido.substring(i, i+1).toUpperCase();
                } else {
                    separacion = separacion + "/";
                }
            }
            return separacion.split("/");
        }
        return null;
    }

    
    
    
    public boolean guardarPalabrasOrdenadas(String[] palabras) {
        palabras = ordenarPalabras(palabras);
        
        String contenido = "";
        for (int i = 0; i < palabras.length; i++) {
            contenido += palabras[i];
            if (i < palabras.length - 1) {
                contenido += "/";
            }
        }
        return escribirArchivo(contenido);
    }
    
    
    
    private String[] ordenarPalabras(String[] palabras) {
        String[] palabrasOrdenadas = palabras.clone();
        int n = palabrasOrdenadas.length;
        
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                String temp = palabrasOrdenadas[i];
                int j;
                
                for (j = i; j >= gap && palabrasOrdenadas[j - gap].compareTo(temp) > 0; j -= gap) {
                    palabrasOrdenadas[j] = palabrasOrdenadas[j - gap];
                }
                
                palabrasOrdenadas[j] = temp;
            }
        }
        
        return palabrasOrdenadas;
    }


}




class JuegoAhorcado extends Oportunidades implements ahorcadoInterface {
	
	 private File archivo;
	    private ManejadorArchivo manejadorArchivo;
	    private Pila pilaLetrasIngresadas; 
	    
	    public JuegoAhorcado() {
	        manejadorArchivo = new ManejadorArchivo("archivo.txt");
	        pilaLetrasIngresadas = new Pila(30); 
	    }
	    

	

	@Override
	public void verificarArchivo() {
		
		String palabras[] = null;
        String ROJO = "\u001B[91m";
        String RESET = "\u001B[0m";
        String VERDE = "\u001B[32m";
        String AMARILLO = "\u001B[33m";
        
        String contenido = manejadorArchivo.leerArchivo();
        
        if (contenido == null || contenido.isEmpty()) {
            System.out.println(ROJO + "El archivo de texto está vacío" + RESET);
            System.out.println("Llena el archivo");
            llenarArchivo();
            return;
        }
        
        String separacion = "";
        for (int i = 0; i < contenido.length(); i++) {
            if (!(contenido.substring(i, i+1).equals("/"))) {
                separacion = separacion + contenido.substring(i, i+1).toUpperCase();
            } else {
                separacion = separacion + "/";
            }
        }
        
        palabras = separacion.split(",");
        
        System.out.println(VERDE + "Cargando..." + RESET);
        System.out.println("Las palabras cargadas son: " + AMARILLO + Arrays.toString(palabras) + RESET);
        System.out.println("Se cargaron: " + palabras.length + " palabras");

		
	}

	@Override
	public void crearArchivo() {
		
		String RESET = "\u001B[0m";
        String VERDE = "\u001B[32m";
        
        if (manejadorArchivo.crearArchivo()) {
            System.out.println(VERDE + "Archivo creado con éxito" + RESET);
        } else {
            System.out.println("Error al crear el archivo de texto");
        }

		
	}

	@Override
	public void llenarArchivo() {
		
		 Scanner scanner = new Scanner(System.in);
		 
	        String VERDE = "\u001B[32m";
	        String RESET = "\u001B[0m";
	        String AMARILLO = "\u001B[33m";
	        String ROJO = "\u001B[91m";
	        
	        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
	        System.out.println("║ " + VERDE + "                    ««««««« Instrucciones »»»»»»»   " + RESET + "                         ║");
	        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
	        System.out.println("║ -Llenar a continuación el archivo con palabras en español e inglés           ║");
	        System.out.println("║ -Las palabras en español e inglés se separan con una barra diagonal (/)      ║");
	        System.out.println("║ -Separar cada palabra con una coma (,)                                       ║");
	        System.out.println("║                                                                              ║");
	        System.out.println("║ " + AMARILLO + "Ejemplo: hola,donde/hello,where" + RESET + "                                              ║");
	        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
	        
	        boolean datosCorrectos = false;
	        String palabrasIngresadas = "";
	        
	        while (!datosCorrectos) {
	            System.out.print("Ingrese las palabras: ");
	            palabrasIngresadas = scanner.nextLine().toUpperCase().replace(" ", "");
	            
	            boolean tieneNumeros = palabrasIngresadas.matches(".*\\d.*");
	            boolean tieneCaracteresInvalidos = !palabrasIngresadas.matches("[A-ZÑ,\\/]+");
	            
	            boolean formatoCorrecto = false;
	            if (palabrasIngresadas.contains("/")) {
	                String[] partes = palabrasIngresadas.split("/");
	                if (partes.length == 2) {
	                    String[] palabrasEspanol = partes[0].split(",");
	                    String[] palabrasIngles = partes[1].split(",");
	                    if (palabrasEspanol.length == palabrasIngles.length && palabrasEspanol.length > 0) {
	                        formatoCorrecto = true;
	                    }
	                }
	            }
	            
	            if (tieneNumeros) {
	                System.out.println(ROJO + "❌ Error!!!: No se permiten números." + RESET);
	            } else if (tieneCaracteresInvalidos) {
	                System.out.println(ROJO + "❌ Error!!!: Solo se permiten letras, comas (,) y una barra (/)." + RESET);
	            } else if (!formatoCorrecto) {
	                System.out.println(ROJO + "❌ Error!!!: El formato debe ser palabra1,palabra2/palabra1,palabra2 y tener la misma cantidad en ambos lados." + RESET);
	            } else {
	                datosCorrectos = true;
	            }
	        }
	        
	        if (manejadorArchivo.escribirArchivo(palabrasIngresadas)) {
	            System.out.println(VERDE + "✅ Se agregaron las palabras correctamente." + RESET);
	        } else {
	            System.out.println(ROJO + "❌ Error al guardar el archivo." + RESET);
	        }

		
	}

	@Override
	public void eliminarArchivo() {
		
		String RESET = "\u001B[0m";
        String ROJO = "\u001B[91m";
        String VERDE = "\u001B[32m";
        
        if (manejadorArchivo.existeArchivo()) {
            if (manejadorArchivo.eliminarArchivo()) {
                System.out.println(VERDE + "Se ha eliminado el archivo con éxito" + RESET);
                manejadorArchivo.crearArchivo();
            } else {
                System.out.println(ROJO + "Error al eliminar el archivo de texto" + RESET);
            }
        } else {
            System.out.println(ROJO + "Error, el archivo que intentas eliminar no existe" + RESET);
        }
		
	}
	
	
	
	
	
	
	public boolean verificarLlenadoDePalabras(String palabrasGuardadas[]) {
        String cad = "";
        for (int i = 0; i < palabrasGuardadas.length; i++) {
            cad = cad + palabrasGuardadas[i].replace(",", "");
        }
        
        int caracteresValidos = 0;
        for (int j = 0; j < cad.length(); j++) {
            if (Character.isLetter(cad.charAt(j)) == true) {
                caracteresValidos++;
            }
        }
        
        return caracteresValidos == cad.length();
    }

	
	public boolean validarPalabrasParaJugar() {
        String palabrasCargadas[] = null;
        
        try {
            palabrasCargadas = cargarPalabras();
        } catch (NullPointerException e) {
            System.out.println("Ha ocurrido un error " + Arrays.toString(palabrasCargadas));
            llenarArchivo();
        }
        
        palabrasCargadas = cargarPalabras();
        
        do {
            if (palabrasCargadas == null) {
                llenarArchivo();
                palabrasCargadas = cargarPalabras();
            } else {
                break;
            }
        } while (palabrasCargadas != null);
        
        if (verificarLlenadoDePalabras(palabrasCargadas) == false) {
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.println("ERROR!! Ingresaste caracteres que no son letras en el archivo");
            System.out.println("Inténtalo de NUEVO...");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            return false;
        }
        return true;
    }

	
	
	public void menuOpciones() {
        Scanner scanner = new Scanner(System.in);
        
        
        String AZUL = "\u001B[36m";
        String RESET = "\u001B[0m";
        char eleccion = ' ';
        
        do {
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║ " + AZUL + "                   MENU PRINCIPAL                   " + RESET + "   ║");
            System.out.println("╠════════════════════════════════════════════════════════╣");
            System.out.println("║ Elige una opción:                                      ║");
            System.out.println("║                                                        ║");
            System.out.println("║   1) Verificar archivo                                 ║");
            System.out.println("║   2) Llenar archivo con palabras                       ║");
            System.out.println("║   3) Borrar archivo                                    ║");
            System.out.println("║   4) Jugar                                             ║");
            System.out.println("║   5) Salir                                             ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.print("Tu opción: ");
            
            String opc = scanner.next();
            
            if (opc.length() == 1) {
                eleccion = opc.charAt(0);
                
                switch (eleccion) {
                    case '1':
                        verificarArchivo();
                        break;
                    
                    case '2':
                        llenarArchivo();
                        break;
                    
                    case '3':
                        eliminarArchivo();
                        break;
                    
                    case '4':
                        String palabrasCargadas[] = null;
                        
                        if (validarPalabrasParaJugar() == false) {
                            break;
                        } else {
                            palabrasCargadas = cargarPalabras();
                        }
                        
                        String palabraSecreta = "";
                        
                        try {
                            palabraSecreta = elegirPalabra(palabrasCargadas);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("-----------------------------------------------------------------------------------------------");
                            System.out.println("Ha ocurrido un error parece que no llenaste correctamente el archivo de texto");
                            System.out.println("Inténtalo de nuevo correctamente!!");
                            System.out.println("---------------------------------------------------------------------------------------------");
                            llenarArchivo();
                            break;
                        }
                        inicioAhorcado(palabraSecreta);
                        break;
                    
                    case '5':
                        System.out.println("Fin del Juego DEJAVU!!!");
                        System.out.println(AZUL + "Saliendo..." + RESET);
                        break;
                    
                    default:
                        System.out.println("ERROR!!! Ingresa una opción válida del 1 al 5 :)");
                }
                System.out.println("----------------------------------------------------------------------------------");
            }
        } while (eleccion != '5');
    }
	
	
	public String[] cargarPalabras() {
        return manejadorArchivo.cargarPalabras();
    }

	
	
	
	public String elegirPalabra(String arreglo[]) {
        Scanner scanner = new Scanner(System.in);
        
        String palabrasEsp[] = arreglo[0].split(",");
        String palabrasIng[] = arreglo[1].split(",");
        
        int posicionPalabraSeleccionada;
        String palabraSecreta = "";
        String op = "";
        
        System.out.println("--------------------------------------------------------------");
        System.out.println("Bienvenido al juego del Ahorcado");
        
        do {
            System.out.println("Elige en qué idioma quieres jugar");
            System.out.println("➤ 1) Jugar con palabras en ESPAÑOL");
            System.out.println("➤ 2) Jugar con palabras en INGLÉS");
            System.out.println("--------------------------------------------------------------");
            
            op = scanner.next().toUpperCase();
            
            switch (op) {
                case "1":
                    posicionPalabraSeleccionada = new Random().nextInt(palabrasEsp.length);
                    palabraSecreta = palabrasEsp[posicionPalabraSeleccionada];
                    break;
                
                case "2":
                    posicionPalabraSeleccionada = new Random().nextInt(palabrasIng.length);
                    palabraSecreta = palabrasIng[posicionPalabraSeleccionada];
                    break;
                
                default:
                    System.out.println("La opción que elegiste no es correcta");
                    System.out.println("Solo puedes ingresar '1' o '2'");
            }
        } while (!(op.equals("1") || op.equals("2")));
        
        palabraSecreta = palabraSecreta + "-" + op;
        return palabraSecreta;
    }

	
	public int buscarLetra(char[] letrasDisponibles, char letraBuscada) {
        for (int i = 0; i < letrasDisponibles.length; i++) {
            if (letrasDisponibles[i] == letraBuscada) {
                return i; 
            }
        }
        return -1; 
    }

	
	
}


public class PruebaJuegoAhorcado {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
