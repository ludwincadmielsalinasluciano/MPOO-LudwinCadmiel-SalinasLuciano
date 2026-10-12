import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

class Result {

    /*
     * Complete the 'detectarZonaAjuste' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts INTEGER_ARRAY vagones as parameter.
     */

    public static int[] detectarZonaAjuste(int[] vagones) {

        if (vagones == null || vagones.length <= 1) {
            return new int[]{-1, -1, 0};
        }

        int n = vagones.length;
        int inicio = 0;
        

        while (inicio < n - 1 && vagones[inicio] <= vagones[inicio + 1]) {
            inicio++;
        }


        if (inicio == n - 1) {
            return new int[]{-1, -1, 0};
        }

        int fin = n - 1;
        

        while (fin > 0 && vagones[fin] >= vagones[fin - 1]) {
            fin--;
        }

        int minVentana = vagones[inicio];
        int maxVentana = vagones[inicio];
        for (int i = inicio; i <= fin; i++) {
            if (vagones[i] < minVentana) minVentana = vagones[i];
            if (vagones[i] > maxVentana) maxVentana = vagones[i];
        }

        while (inicio > 0 && vagones[inicio - 1] > minVentana) {
            inicio--;
        }

        while (fin < n - 1 && vagones[fin + 1] < maxVentana) {
            fin++;
        }

        int longitud = fin - inicio + 1;
        
        return new int[]{inicio, fin, longitud};
    }
}

public class Solution {

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(System.in));

        BufferedWriter bufferedWriter =
                new BufferedWriter(
                        new FileWriter(System.getenv("OUTPUT_PATH"))
                );

        String linea = bufferedReader.readLine();

        String[] datos = linea.trim().split("\\s+");

        int[] vagones = new int[datos.length];

        for (int i = 0; i < datos.length; i++) {
            vagones[i] = Integer.parseInt(datos[i]);
        }

        int[] result = Result.detectarZonaAjuste(vagones);

        for (int i = 0; i < result.length; i++) {
            bufferedWriter.write(String.valueOf(result[i]));

            if (i != result.length - 1) {
                bufferedWriter.write(" ");
            }
        }

        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}