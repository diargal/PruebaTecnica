package dominio.unitaria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dominio.Vendedor;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantiaExtendida;
	private Vendedor vendedor;

	@Before
	public void startTest() {
		repositorioProducto = mock(RepositorioProducto.class);
		repositorioGarantiaExtendida = mock(RepositorioGarantiaExtendida.class);
		vendedor = new Vendedor(repositorioProducto, repositorioGarantiaExtendida);
	}

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		// assert
		assertTrue(existeProducto);
	}

	@Test
	public void productoNoTieneGarantiaTest() {

		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		// assert
		assertFalse(existeProducto);
	}

	@Test(expected = GarantiaExtendidaException.class)
	public void tresVocalesTest() {

		String codigo = "doremiwqqgh";
		String nombre = "Cristobal Colón";
		vendedor.generarGarantia(codigo, nombre);

	}

	@Test
	public void conteoConVocalesTest() {

		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		RepositorioGarantiaExtendida repositorioGarantiaExtendida = mock(RepositorioGarantiaExtendida.class);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantiaExtendida);

		int actual = vendedor.countVocals("mAmUmiw");

		assertEquals(3, actual);
	}

	@Test
	public void conteoSinVocalesTest() {

		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		RepositorioGarantiaExtendida repositorioGarantiaExtendida = mock(RepositorioGarantiaExtendida.class);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantiaExtendida);

		int actual = vendedor.countVocals("wwwRRRTy");

		assertEquals(0, actual);
	}

	@Test
	public void calcularFechaFinalInferiorTest() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {

			Date parametro = sdf.parse("16/08/2018");
			Date expected = sdf.parse("10/12/2018");
			Date actual = vendedor.calcularFechaFinal(130000, parametro);

			assertEquals(expected, actual);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calcularFechaFinalSuperiorTest() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {

			Date parametro = sdf.parse("16/08/2018");
			Date expected = sdf.parse("06/04/2019");
			Date actual = vendedor.calcularFechaFinal(800000, parametro);

			assertEquals(expected, actual);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calcularFechaFinalInferiorLunesTest() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {

			Date parametro = sdf.parse("19/08/2018");
			Date expected = sdf.parse("14/12/2018");
			Date actual = vendedor.calcularFechaFinal(130000, parametro);
			assertEquals(expected, actual);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calcularFechaFinalSuperiorLunesTest() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {

			Date parametro = sdf.parse("19/08/2018");
			Date expected = sdf.parse("10/04/2019");
			Date actual = vendedor.calcularFechaFinal(800000, parametro);

			assertEquals(expected, actual);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calcularFechaFinalSuperiorDomingoTest() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {

			Date parametro = sdf.parse("24/08/2018");
			Date expected = sdf.parse("16/04/2019");
			Date actual = vendedor.calcularFechaFinal(800000, parametro);
			System.out.println(actual);
			assertEquals(expected, actual);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void calcularValorGarantiaSuperiorTest() {

		long expected = (long) vendedor.calcularValorGarantia(650000);
		long actual = 130000;
		assertEquals(expected, actual);
	}

	@Test
	public void calcularValorGarantiaInferiorTest() {

		long expected = (long) vendedor.calcularValorGarantia(200000);
		long actual = 20000;
		assertEquals(expected, actual);

	}
}
