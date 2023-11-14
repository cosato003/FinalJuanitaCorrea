import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class Cliente {
    String tipoCliente;

    public Cliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}

class BilleteraDigital {
    Map<String, Double> cuentas;
    List<Transaccion> transacciones;
    Random random;

    public BilleteraDigital() {
        cuentas = new HashMap<>();
        transacciones = new ArrayList<>();
        random = new Random();
    }

    public void recargarCuenta(String cliente, double monto) {
        double factor = 1.0;
        if (cliente.equals("Persona Juridica")) {
            factor = 2.8;
        } else if (cliente.equals("ONG")) {
            factor = 2.0;
        }

        monto = monto * factor;
        double saldoActual = cuentas.getOrDefault(cliente, 0.0);
        cuentas.put(cliente, saldoActual + monto);
    }

    public void realizarTransaccion(double monto, String clienteEmisor, String clienteReceptor, String ciudadBase) {
        int id = random.nextInt(1000);
        int horaDelDia = random.nextInt(24);
        String[] tiposCliente = {"Persona Juridica", "ONG", "Persona Natural"};
        String tipoClienteEmisor = tiposCliente[random.nextInt(tiposCliente.length)];
        String tipoClienteReceptor = tiposCliente[random.nextInt(tiposCliente.length)];

        double saldoEmisor = cuentas.getOrDefault(clienteEmisor, 0.0);
        double saldoReceptor = cuentas.getOrDefault(clienteReceptor, 0.0);

        if (saldoEmisor >= monto) {
            cuentas.put(clienteEmisor, saldoEmisor - monto);
            cuentas.put(clienteReceptor, saldoReceptor + monto);

            Transaccion transaccion = new Transaccion(id, monto, tipoClienteEmisor, tipoClienteReceptor, horaDelDia, ciudadBase);
            transacciones.add(transaccion);
            guardarTransaccionEnArchivo(transaccion);
        } else {
            System.out.println("Saldo insuficiente para realizar la transacción.");
        }
    }

    private void guardarTransaccionEnArchivo(Transaccion transaccion) {
        try (FileWriter fileWriter = new FileWriter("sample.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            String datosTransaccion = String.format("ID: %d, Monto: %.2f, Emisor: %s, Receptor: %s, Hora: %d, Ciudad: %s",
                    transaccion.id, transaccion.monto, transaccion.tipoClienteEmisor, transaccion.tipoClienteReceptor,
                    transaccion.horaDelDia, transaccion.ciudadBase);

            bufferedWriter.write(datosTransaccion);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar la transacción en el archivo: " + e.getMessage());
        }
    }
}

class Transaccion {
    int id;
    double monto;
    String tipoClienteEmisor;
    String tipoClienteReceptor;
    int horaDelDia;
    String ciudadBase;

    public Transaccion(int id, double monto, String tipoClienteEmisor, String tipoClienteReceptor, int horaDelDia, String ciudadBase) {
        this.id = id;
        this.monto = monto;
        this.tipoClienteEmisor = tipoClienteEmisor;
        this.tipoClienteReceptor = tipoClienteReceptor;
        this.horaDelDia = horaDelDia;
        this.ciudadBase = ciudadBase;
    }
}

public class BilleteraApp {
    public static void main(String[] args) {
        BilleteraDigital billetera = new BilleteraDigital();

        // Ejemplo de recarga de cuenta
        billetera.recargarCuenta("Persona Juridica", 1000.0);
        billetera.recargarCuenta("ONG", 500.0);

        // Ejemplo de transacción
        billetera.realizarTransaccion(200.0, "Persona Juridica", "ONG", "Ciudad A");

        // Mostrar transacciones
        for (Transaccion transaccion : billetera.transacciones) {
            System.out.println("ID de transacción: " + transaccion.id);
            System.out.println("Monto: " + transaccion.monto);
            System.out.println("Tipo de cliente emisor: " + transaccion.tipoClienteEmisor);
            System.out.println("Tipo de cliente receptor: " + transaccion.tipoClienteReceptor);
            System.out.println("Hora del día: " + transaccion.horaDelDia);
            System.out.println("Ciudad base: " + transaccion.ciudadBase);
            System.out.println();
        }
    }
}
