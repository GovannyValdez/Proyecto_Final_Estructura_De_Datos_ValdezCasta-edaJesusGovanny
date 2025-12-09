import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

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
    
    
    //shellsort
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
