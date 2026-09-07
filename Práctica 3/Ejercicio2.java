//Salinas Luciano Ludwin Cadmiel
import java.io.*;

class Result {

    /*
     * Complete the 'comprimir' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts STRING datos as parameter.
     */

    public static String comprimir(String datos) {
        if (datos == null || datos.isEmpty()) {
            return "";
        }
        
        StringBuilder resultado = new StringBuilder();
        int contador = 1;
        
        for (int i = 1; i <= datos.length(); i++) {
            
            if (i < datos.length() && datos.charAt(i) == datos.charAt(i - 1) && contador < 9) {
                contador++;
            } else {
                
                resultado.append(contador);
                resultado.append(datos.charAt(i - 1));
                
               
                contador = 1;
            }
        }
        
        return resultado.toString();
    }
}

public class Ejercicio2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String datos = bufferedReader.readLine();

        String result = Result.comprimir(datos);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
