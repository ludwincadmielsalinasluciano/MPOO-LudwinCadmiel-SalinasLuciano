import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

class Result {

    /*
     * Complete the 'cifrarMensaje' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts following parameters:
     *  1. STRING mensaje
     *  2. STRING alfabetoInterior
     *  3. INTEGER posicionInicial
     *  4. INTEGER intervaloRotacion
     */

    public static String cifrarMensaje(String mensaje, String alfabetoInterior, int posicionInicial, int intervaloRotacion) {
        StringBuilder resultado = new StringBuilder();
        int letrasProcesadas = 0;
        

        int shift = (posicionInicial % 26 + 26) % 26;

        for (int i = 0; i < mensaje.length(); i++) {
            char c = mensaje.charAt(i);

            if (c == ' ') {
                resultado.append(' ');
                continue;
            }


            if (c >= 'A' && c <= 'Z') {
                int posExterior = c - 'A';
                
           
                int posInterior = (posExterior - shift) % 26;
                if (posInterior < 0) {
                    posInterior += 26;
                }

                resultado.append(alfabetoInterior.charAt(posInterior));
                letrasProcesadas++;

      
                if (intervaloRotacion > 0 && letrasProcesadas % intervaloRotacion == 0) {
                    shift = (shift + 1) % 26;
                }
            } else {

                resultado.append(c);
            }
        }

        return resultado.toString();
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String mensaje = bufferedReader.readLine();

        String alfabetoInterior = bufferedReader.readLine();

        int posicionInicial = Integer.parseInt(bufferedReader.readLine().trim());

        int intervaloRotacion = Integer.parseInt(bufferedReader.readLine().trim());

        String result = Result.cifrarMensaje(mensaje, alfabetoInterior, posicionInicial, intervaloRotacion);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}