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

    
    
    
    
    
}







public class PruebaJuegoAhorcado {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
