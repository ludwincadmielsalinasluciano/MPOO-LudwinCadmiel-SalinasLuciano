import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CuentaBancariaService service = new CuentaBancariaService();
        int opcion = 0;

        do {
            System.out.println("\n========== SISTEMA BANCARIO ==========");
            System.out.println("1. Transferir dinero");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            if (!sc.hasNextInt()) {
                System.out.println("Entrada inválida. Ingrese un número.");
                sc.nextLine();
                continue;
            }

            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Cuenta de origen: ");
                    String cOrigen = sc.nextLine();
                    System.out.print("Cuenta de destino: ");
                    String cDestino = sc.nextLine();
                    System.out.print("Cantidad a transferir: ");
                    double montoTransferir = sc.nextDouble();
                    sc.nextLine();

                    if (service.transferir(cOrigen, cDestino, montoTransferir)) {
                        System.out.println("-> Transferencia completada exitosamente.");
                    } else {
                        System.out.println("-> Error: Validación fallida.");
                    }
                    break;
                case 2:
                    System.out.print("Cuenta destino: ");
                    String cDep = sc.nextLine();
                    System.out.print("Cantidad a depositar: ");
                    double montoDep = sc.nextDouble();
                    sc.nextLine();

                    if (service.depositar(cDep, montoDep)) {
                        System.out.println("-> Depósito completado con éxito.");
                    } else {
                        System.out.println("-> Error: No se pudo realizar el depósito.");
                    }
                    break;
                case 3:
                    System.out.print("Cuenta a retirar: ");
                    String cRet = sc.nextLine();
                    System.out.print("Cantidad a retirar: ");
                    double montoRet = sc.nextDouble();
                    sc.nextLine();

                    if (service.retirar(cRet, montoRet)) {
                        System.out.println("-> Retiro completado con éxito.");
                    } else {
                        System.out.println("-> Error: Saldo insuficiente o cuenta no válida.");
                    }
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no reconocida.");
            }
        } while (opcion != 4);
        sc.close();
    }
}
