package dominio;

import dominio.repositorio.RepositorioProducto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String TRES_VOCALES = "Este producto no cuenta con garantía extendida";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String nombreCliente) {

		if (tieneGarantia(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		} else if (countVocals(codigo) == 3) {
			throw new GarantiaExtendidaException(TRES_VOCALES);
		}

		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);

		Date fechaSolicitudGarantia = new Date();
		Date fechaFinGarantia = calcularFechaFinal(producto.getPrecio(), fechaSolicitudGarantia);
		double precioGarantia = calcularValorGarantia(producto.getPrecio());

		GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, fechaSolicitudGarantia, fechaFinGarantia,
				precioGarantia, nombreCliente);

		repositorioGarantia.agregar(garantiaExtendida);
	}

	public boolean tieneGarantia(String codigo) {
		Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		if (producto == null) {
			return false;
		} else {
			return true;
		}
	}

	public int countVocals(String codigo) {

		int count = 0;
		String[] cad = codigo.split("");

		for (int i = cad.length - 1; i >= 0; i--) {
			if (cad[i].equalsIgnoreCase("a") || cad[i].equalsIgnoreCase("e") || cad[i].equalsIgnoreCase("i")
					|| cad[i].equalsIgnoreCase("o") || cad[i].equalsIgnoreCase("u")) {
				count++;
			}
		}
		return count;
	}

	public double calcularValorGarantia(double precioProducto) {
		if (precioProducto > 500000) {
			return precioProducto * 0.2;
		} else {
			return precioProducto * 0.1;
		}
	}

	public Date calcularFechaFinal(double precioProducto, Date actual) {

		Calendar calendario = Calendar.getInstance();
		calendario.setTime(actual);

		int dias = 0;

		if (precioProducto > 500000)
			dias = 200;
		else
			dias = 100;

		for (int j = dias; j > 0; j--) {
			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				calendario.add(Calendar.DAY_OF_YEAR, 1);// anexo un día por cada
														// lunes encontrado
			}
			calendario.add(Calendar.DAY_OF_YEAR, 1);
		}

		if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendario.add(Calendar.DAY_OF_YEAR, 2);// para que la garantía
													// venza el martes
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaFinal = new Date(calendario.getTimeInMillis());

		try {
			fechaFinal = sdf.parse(sdf.format(fechaFinal));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return fechaFinal;
	}

}
