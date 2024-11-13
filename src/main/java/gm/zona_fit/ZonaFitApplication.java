package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	//inyectamos la dependencia de servicios hacia la capa de presentacion
	@Autowired
	private IClienteServicio clienteServicio;

	private static final Logger logger = LoggerFactory.getLogger(
			ZonaFitApplication.class);

	//salto de linea para cualquier sistema operativo
	private final String nl = System.lineSeparator();

	public static void main(String[] args) {
		//Levantamos la fabrica de spring
		SpringApplication.run(ZonaFitApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var salir = false;
		var consola = new Scanner(System.in);
		while(!salir){
			try{
				mostrarMenu();
				var opcion = Integer.parseInt(consola.nextLine().strip());
				salir = ejecutarMenu(opcion, consola);
			}catch (Exception e){
                logger.info(nl + "Error, ingrese un numero entero: " + e.getMessage());
			}
		}
		consola.close();
	}

	private void mostrarMenu(){
		logger.info(nl + "*** Aplicacion Zona Fit ***" + nl);
		logger.info("""
                Menu:
                1. Listar Clientes.
                2. Buscar Cliente.
                3. Agregar Cliente.
                4. Modificar Cliente.
                5. Eliminar Cliente.
                6. Salir.
                Ingrese una opcion:\s""");
	}

	private boolean ejecutarMenu(int opcion, Scanner consola){
		logger.info(nl);
		switch (opcion){
			case 1 -> listarClientes();
			case 2 -> buscarCliente(consola);
			case 3 -> agregarCliente(consola);
			case 4 -> modificarCliente(consola);
			case 5 -> eliminarCliente(consola);
			case 6 -> {
				logger.info("Hasta Luego!!!" + nl + nl);
				return true;
			}
			default -> logger.info(nl + "Opcion invalida: " + opcion);
		}
		logger.info(nl);
		return false;
	}

	private void listarClientes(){
		logger.info("*** Lista Clientes ***" + nl);
		this.clienteServicio.listarClientes().forEach(cliente ->
				logger.info(cliente.toString() + nl));
	}

	private void buscarCliente(Scanner consola){
		logger.info(nl + "Introduce el id del Cliente a buscar: ");
		try{
			var id = Integer.parseInt(consola.nextLine().strip());
			var cliente = this.clienteServicio.buscarClientePorId(id);
			if (cliente != null){
				logger.info(nl + "Cliente encontrado: " + cliente);
			}else
				logger.info(nl + "No se encontro el cliente");
		}catch (Exception e){
			logger.info(nl + "Error, ingrese un numero entero: " + e.getMessage());
		}
	}

	private void agregarCliente(Scanner consola) {
		var cliente = new Cliente();
		try{
			logger.info("Ingrese el nombre del Cliente: ");
			var nombre = consola.nextLine().strip();
			logger.info("Ingrese el apellido del Cliente: ");
			var apellido = consola.nextLine().strip();
			logger.info("Ingrese la membresia del Cliente: ");
			var membresia = Integer.parseInt(consola.nextLine().strip());
			cliente = new Cliente();
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setMembresia(membresia);
			this.clienteServicio.guardarCliente(cliente);
			logger.info(nl + "Cliente agregado exitosamente: " + cliente);
		}catch (Exception e){
			logger.info(nl + "Error, ingrese un numero entero: " + e.getMessage());
		}
	}

	private void modificarCliente(Scanner consola) {
		try{
			logger.info("Ingrese el id del cliente a modificar: ");
			var id = Integer.parseInt(consola.nextLine().strip());
			var cliente = clienteServicio.buscarClientePorId(id);
			if(cliente != null){
				logger.info("Ingrese el nombre del Cliente: ");
				var nombre = consola.nextLine().strip();
				logger.info("Ingrese el apellido del Cliente: ");
				var apellido = consola.nextLine().strip();
				logger.info("Ingrese la membresia del Cliente: ");
				var membresia = Integer.parseInt(consola.nextLine().strip());
				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setMembresia(membresia);
				this.clienteServicio.guardarCliente(cliente);
				logger.info(nl + "Cliente modificado exitosamente" + cliente);
			}else
				logger.info(nl + "No existe el cliente con ese id: " + id);
		}catch (Exception e){
			logger.info(nl + "Error, ingrese un numero entero: " + e.getMessage());
		}
	}

	private void eliminarCliente(Scanner consola) {
		try{
			logger.info("Ingrese el id del Cliente a eliminar: ");
			var id = Integer.parseInt(consola.nextLine().strip());
			var cliente = clienteServicio.buscarClientePorId(id);
			if(cliente != null){
				this.clienteServicio.eliminarCliente(cliente);
				logger.info(nl + "Cliente eliminado: " + cliente);
			}else
				logger.info(nl + "No existe cliente con ese id: " + id);
		}catch (Exception e){
			logger.info(nl + "Error, ingrese un numero entero: " + e.getMessage());
		}
	}
}
