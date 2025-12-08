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

	
	 public void inicioAhorcado(String palabraSecretaMasIdioma) {
	        Scanner scanner = new Scanner(System.in);
	        
	        String ROJO = "\u001B[91m";
	        String RESET = "\u001B[0m";
	        String AMARILLO = "\u001B[33m";
	        String VERDE = "\u001B[32m";
	        
	        boolean ganador = false;
	        setIntentos((byte) 8);
	        
	        pilaLetrasIngresadas = new Pila(30);
	        
	        String palabraIdioma[] = palabraSecretaMasIdioma.split("-");
	        String palabraSecreta = palabraIdioma[0];
	        String idioma = palabraIdioma[1];
	        
	        if (palabraSecreta.length() == 0) {
	            System.out.println("Error no hiciste correctamente el ingreso de palabras :(");
	            return;
	        }
	        
	        if (idioma.equalsIgnoreCase("2")) {
	            System.out.println("Estoy pensando en una palabra en INGLÉS con " + palabraSecreta.length() + " letras");
	        } else {
	            System.out.println("Estoy pensando en una palabra en ESPAÑOL con " + palabraSecreta.length() + " letras");
	        }
	        
	        System.out.println("--------------------------------------------------------------");
	        
	        do {
	            if (getIntentos() == 0) {
	                break;
	            }
	            
	            System.out.println("Tienes " + getIntentos() + " oportunidades para adivinar");
	            mostrarMonito(getIntentos());
	            System.out.println();
	            System.out.println("Letras disponibles: " + obtenerLetrasDisponibles(idioma));
	            System.out.print("Ingresa una letra: ");
	            
	            String letra = scanner.next().toUpperCase();
	            
	            if (letra.length() > 1) {
	                System.out.println("Ingresaste más de un carácter");
	                continue;
	            }
	            
	            if (letra.length() == 1) {
	                char charLetra = letra.charAt(0);
	                
	                if (charLetra >= 48 && charLetra <= 57) {
	                    System.out.println("Esto no es una letra, es un número");
	                    continue;
	                }
	                
	                if (!Character.isLetter(charLetra)) {
	                    System.out.println("Ingresaste un carácter especial");
	                    continue;
	                }
	                
	                if (idioma.equals("2") && letra.equals("Ñ")) {
	                    System.out.println("La letra 'ñ' no existe en el abecedario del idioma inglés");
	                    descontarIntentos();
	                    letrañ();
	                    continue;
	                }
	                
	                if (pilaLetrasIngresadas.contiene(letra)) {
	                    System.out.println("Ya habías ingresado esa letra");
	                    continue;
	                }
	                
	                pilaLetrasIngresadas.push(letra);
	                
	                boolean letraEncontrada = false;
	                for (int i = 0; i < palabraSecreta.length(); i++) {
	                    if (palabraSecreta.substring(i, i + 1).equals(letra)) {
	                        letraEncontrada = true;
	                        break;
	                    }
	                }
	                
	                if (letraEncontrada) {
	                    System.out.println(VERDE + "✅ Acertaste!!" + RESET);
	                } else {
	                    System.out.println(ROJO + "❌ La letra NO está en la palabra SECRETA" + RESET);
	                    descontarIntentos();
	                }
	                
	                String palabraActual = obtenerPalabraAdivinada(palabraSecreta);
	                for (int i = 0; i < palabraActual.length(); i++) {
	                    if (palabraActual.codePointAt(i) <= 90 && palabraActual.codePointAt(i) >= 65) {
	                        System.out.print(palabraActual.charAt(i) + " ");
	                    } else {
	                        System.out.print("_ ");
	                    }
	                }
	                System.out.println();
	            }
	            
	            if (seAdivinoLaPalabra(palabraSecreta)) {
	                ganador = true;
	                break;
	            }
	            
	        } while (getIntentos() > 0);
	        
	        if (getIntentos() == 0) {
	            mostrarMonito((byte) 0);
	            System.out.println("Te has quedado sin oportunidades disponibles :(");
	            System.out.println("La palabra secreta era: " + AMARILLO + palabraSecreta + RESET);
	        } else if (ganador == true) {
	            System.out.println(VERDE + "¡Felicidades! GANASTE :D!!!" + RESET);
	        }
	    }
	
	
	
	 public boolean seAdivinoLaPalabra(String palabraSecreta) {
	        String[] letrasIngresadas = pilaLetrasIngresadas.obtenerElementos();
	        int cont = 0;
	        
	        for (int i = 0; i < palabraSecreta.length(); i++) {
	            String posicion = palabraSecreta.substring(i, i + 1);
	            for (int j = 0; j < letrasIngresadas.length; j++) {
	                if (posicion.equals(letrasIngresadas[j])) {
	                    cont++;
	                    break;
	                }
	            }
	        }
	        
	        return cont == palabraSecreta.length();
	    }

	 
	 public String obtenerPalabraAdivinada(String palabraSecreta) {
	        String[] letrasIngresadas = pilaLetrasIngresadas.obtenerElementos();
	        String carac = "!#$%&/()=? '<>-_.:,;}";
	        String palabraCodificada = "";
	        
	        for (int i = 0; i < palabraSecreta.length(); i++) {
	            palabraCodificada = palabraCodificada + carac.substring(i, i + 1);
	        }
	        
	        for (int i = 0; i < letrasIngresadas.length; i++) {
	            for (int j = 0; j < palabraSecreta.length(); j++) {
	                if (palabraSecreta.substring(j, j + 1).equals(letrasIngresadas[i])) {
	                    char c = palabraCodificada.charAt(j);
	                    palabraCodificada = palabraCodificada.replace(c, letrasIngresadas[i].charAt(0));
	                }
	            }
	        }
	        
	        return palabraCodificada;
	    }

	
	
	 public String obtenerLetrasDisponibles(String idioma) {
	        char[] letrasDisponibles;
	        String[] letrasIngresadas = pilaLetrasIngresadas.obtenerElementos();
	        
	        if (idioma.equals("1")) {
	            letrasDisponibles = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	        } else {
	            letrasDisponibles = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	        }
	        
	        for (int j = 0; j < letrasIngresadas.length; j++) {
	            String letraIngresada = letrasIngresadas[j];
	            if (letraIngresada != null && !letraIngresada.equals(" ")) {
	                char letra = letraIngresada.toUpperCase().charAt(0);
	                
	                int posicion = buscarLetra(letrasDisponibles, letra);
	                if (posicion != -1) {
	                    letrasDisponibles[posicion] = '_';
	                }
	            }
	        }
	        
	        String letrasMayusculas = "";
	        String letrasMinusculas = "";
	        
	        for (int i = 0; i < letrasDisponibles.length; i++) {
	            if (letrasDisponibles[i] != '_') {
	                letrasMayusculas += letrasDisponibles[i] + " ";
	                if (letrasDisponibles[i] == 'Ñ') {
	                    letrasMinusculas += "ñ ";
	                } else {
	                    letrasMinusculas += Character.toLowerCase(letrasDisponibles[i]) + " ";
	                }
	            } else {
	                letrasMayusculas += "_ ";
	            }
	        }
	        
	        if (letrasMayusculas.length() > 0) {
	            letrasMayusculas = letrasMayusculas.substring(0, letrasMayusculas.length() - 1);
	        }
	        if (letrasMinusculas.length() > 0) {
	            letrasMinusculas = letrasMinusculas.substring(0, letrasMinusculas.length() - 1);
	        }
	        
	        return letrasMayusculas + "\n" + letrasMinusculas;
	    }

	
	
	public void mostrarMonito(byte intentosRestantes) {
        System.out.println("-----------");
        
        switch (intentosRestantes) {
            case 8:
                System.out.println("   ");
                System.out.println("   ");
                System.out.println("   ");
                System.out.println("   ");
                System.out.println("   ");
                System.out.println("   ");
                break;
            case 7:
                System.out.println("   _______");
                System.out.println("  |/");
                System.out.println("  |");
                System.out.println("  |");
                System.out.println("  |");
                System.out.println(" _|___");
                break;
            case 6:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |");
                System.out.println("  |");
                System.out.println("  |");
                System.out.println(" _|___");
                break;
            case 5:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |      ( )");
                System.out.println("  |");
                System.out.println("  |");
                System.out.println(" _|___");
                break;
            case 4:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |      ( )");
                System.out.println("  |       |");
                System.out.println("  |");
                System.out.println(" _|___");
                break;
            case 3:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |      ( )");
                System.out.println("  |      \\|");
                System.out.println("  |");
                System.out.println(" _|___");
                break;
            case 2:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |      ( )");
                System.out.println("  |      \\|/");
                System.out.println("  |");
                System.out.println(" _|___");
                break;
            case 1:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |      ( )");
                System.out.println("  |      \\|/");
                System.out.println("  |       |");
                System.out.println(" _|___");
                break;
            case 0:
                System.out.println("   _______");
                System.out.println("  |/      |");
                System.out.println("  |      ( )");
                System.out.println("  |      \\|/");
                System.out.println("  |      / \\");
                System.out.println(" _|___");
                break;
        }
        
        System.out.println("-----------");
    }
	
	
	
	
}

public class PruebaJuegoAhorcado {

	public static void main(String[] args) {
		
		JuegoAhorcado juegoA = new JuegoAhorcado();
        juegoA.menuOpciones();
		
		
	}

}
