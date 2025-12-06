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
	
	
}







public class PruebaJuegoAhorcado {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
