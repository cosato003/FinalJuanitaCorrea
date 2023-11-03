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

class PersonaNatural extends Cliente {
    public PersonaNatural() {
        super("Persona Natural");
    }
}

class PersonaJuridica extends Cliente {
    public PersonaJuridica() {
        super("Persona Juridica");
    }
}

class ONG extends Cliente {
    public ONG() {
        super("ONG");
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

class BilleteraDigital {
    Map<String, Double> cuentas;
    List<Transaccion> puntosDeControl;
    Random random; // Para generar valores aleatorios

    public BilleteraDigital() {
        cuentas = new HashMap<>();
        puntosDeControl = new ArrayList<>();
        random = new Random(); // Inicializa el generador de números aleatorios
    }

    public void recargarCuenta(String cliente, double monto) {
        try {
            double factor = 1.0;
            if (cliente.equals("Persona Juridica")) {
                factor = 2.8;
            } else if (cliente.equals("ONG")) {
                factor = 2.0;
            }

            monto = monto * factor;
            double saldoActual = cuentas.getOrDefault(cliente, 0.0);
            cuentas.put(cliente, saldoActual + monto);
        } catch (Exception e) {
            System.out.println("Error al recargar la cuenta: " + e.getMessage());
        }
    }

    public void realizarTransaccion(double monto, String clienteEmisor, String clienteReceptor, String ciudadBase) {
        try {
            // Generar valores aleatorios para ID, horaDelDia, tipoClienteEmisor y tipoClienteReceptor
            int id = random.nextInt(1000); // ID entre 0 y 999
            int horaDelDia = random.nextInt(24); // Hora del día entre 0 y 23
            String[] tiposCliente = {"Persona Juridica", "ONG", "Persona Natural"};
            String tipoClienteEmisor = tiposCliente[random.nextInt(tiposCliente.length)];
            String tipoClienteReceptor = tiposCliente[random.nextInt(tiposCliente.length)];

            double saldoEmisor = cuentas.getOrDefault(clienteEmisor, 0.0);
            double saldoReceptor = cuentas.getOrDefault(clienteReceptor, 0.0);

            if (saldoEmisor >= monto) {
                cuentas.put(clienteEmisor, saldoEmisor - monto);
                cuentas.put(clienteReceptor, saldoReceptor + monto);

                Transaccion transaccion = new Transaccion(id, monto, tipoClienteEmisor, tipoClienteReceptor, horaDelDia, ciudadBase);
                puntosDeControl.add(transaccion); // Agregar la transacción a los puntos de control.

                // Llamada para guardar la transacción en el archivo.
                guardarTransaccionEnArchivo(transaccion);
            } else {
                System.out.println("Saldo insuficiente para realizar la transacción.");
            }
        } catch (Exception e) {
            System.out.println("Error al realizar la transacción: " + e.getMessage());
        }
    }

    // Método para guardar una transacción en un archivo.
    private void guardarTransaccionEnArchivo(Transaccion transaccion) {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\Juanita\\Desktop\\finalLogica\\FinalLogi\\sample.txt", true); // Modo "true" para añadir al archivo.

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Construir una cadena con los datos de la transacción.
            String datosTransaccion = String.format("ID: %d, Monto: %.2f, Emisor: %s, Receptor: %s, Hora: %d, Ciudad: %s",
                    transaccion.id, transaccion.monto, transaccion.tipoClienteEmisor, transaccion.tipoClienteReceptor,
                    transaccion.horaDelDia, transaccion.ciudadBase);

            // Escribir los datos de la transacción en el archivo.
            bufferedWriter.write(datosTransaccion);
            bufferedWriter.newLine();

            // Cerrar el BufferedWriter y FileWriter
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error al guardar la transacción en el archivo: " + e.getMessage());
        }
    }
}

public class JuanitaCorrea {
    public static void main(String[] args) {
        BilleteraDigital billetera = new BilleteraDigital();

        // Ejemplo de recarga de cuenta
        billetera.recargarCuenta("Persona Juridica", 1000.0);
        billetera.recargarCuenta("ONG", 500.0);

        // Ejemplo de transacción
        billetera.realizarTransaccion(200.0, "Persona Juridica", "ONG", "Ciudad A");

        // Mostrar puntos de control (transacciones)
        for (Transaccion transaccion : billetera.puntosDeControl) {
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
