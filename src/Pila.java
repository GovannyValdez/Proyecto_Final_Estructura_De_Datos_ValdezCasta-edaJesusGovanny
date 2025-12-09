
class Pila{
	
	
	 	private String[] elementos;
	    private int cima;
	    private int capacidad;
	    
	    public Pila(int capacidad) {
	    	
	        this.capacidad = capacidad;
	        this.elementos = new String[capacidad];
	        this.cima = -1;
	        
	    }
	    
	    
	    
	    public void push(String elemento) {
	    	
	        if (cima < capacidad - 1) {
	            cima++;
	            elementos[cima] = elemento;
	        }
	    }

	    
	    public boolean estaVacia() {
	    	
	        return cima == -1;
	        
	    }

	    
	    public String pop() {
	    	
	        if (!estaVacia()) {
	        	
	            String elemento = elementos[cima];
	            cima--;
	            return elemento;
	            
	        }
	        
	        return null;
	    }

	    
	    public String peek() {
	    	
	        if (!estaVacia()) {
	        	
	            return elementos[cima];
	        }
	        
	        return null;
	    }

	    
	    public int tamanio() {
	    	
	        return cima + 1;
	    }
	    
	    public String[] obtenerElementos() {
	    	
	        String[] resultado = new String[cima + 1];
	        
	        for (int i = 0; i <= cima; i++) {
	        	
	            resultado[i] = elementos[i];
	            
	        }
	        
	        return resultado;
	    }


	    public boolean contiene(String elemento) {
	    	
	        for (int i = 0; i <= cima; i++) {
	        	
	            if (elementos[i] != null && elementos[i].equals(elemento)) {
	            	
	                return true;
	            }
	        }
	        
	        return false;
	    }
	

	
}
