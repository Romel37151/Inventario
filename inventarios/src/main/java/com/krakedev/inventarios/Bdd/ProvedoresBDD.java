package com.krakedev.inventarios.Bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.Proveedores;
import com.krakedev.inventarios.entidades.TipoDocumentos;
import com.krakedev.inventarios.entidades.UnidadDeMedida;
import com.krakedev.inventarios.exception.KrakedevException;
import com.krakedev.inventarios.utils.ConexionBDD;


public class ProvedoresBDD {
	public ArrayList<Proveedores> buscar(String sub) throws KrakedevException{
		ArrayList<Proveedores> Proveedores= new ArrayList<Proveedores>();
		Connection con= null;
		PreparedStatement ps= null;
		ResultSet rs= null;
		Proveedores cl=null;
		try {
			con=ConexionBDD.obtenerConexion();
				ps=con.prepareStatement("select pro.identificador, pro.tipo_de_documento,td.descripcion, pro.nombre, pro.telefono, pro.correo, pro.direccion"
						+			" from proveedores pro , tipo_de_documentos td"
						+ 			" where pro.tipo_de_documento = td.codigo "
						+ 					" and upper(nombre) like ?"
							);
				
				ps.setString(1,  "%"+sub.toUpperCase()+"%");
				rs=ps.executeQuery();
				
				while(rs.next()) {
					String identificador=rs.getString("identificador");
					String tipoDocumento=rs.getString("tipo_de_documento");
					String TDdescripcion=rs.getString("descripcion");
					String nombre=rs.getString("nombre");
					String telefono=rs.getString("telefono");
					String correo=rs.getString("correo");
					String direccion=rs.getString("direccion");
					TipoDocumentos TD= new TipoDocumentos(tipoDocumento,TDdescripcion);
					cl= new Proveedores(identificador,TD,nombre,telefono,correo,direccion);
					Proveedores.add(cl);
				}
			
		} catch (KrakedevException e) {
			e.printStackTrace();
			throw e;
		}catch (SQLException e) {
				e.printStackTrace();
				throw new KrakedevException("Error al consultar"+e.getMessage());
			}
		
		return Proveedores;
	}
	
public  ArrayList<TipoDocumentos> recuperarTodos() throws KrakedevException{
		
	ArrayList<TipoDocumentos> TD= new ArrayList<TipoDocumentos>();
		
		Connection con= null;
		PreparedStatement ps= null;
		ResultSet rs= null;
		TipoDocumentos cl=null;
		try {
			con=ConexionBDD.obtenerConexion();
				ps=con.prepareStatement("select * from  public.tipo_de_documentos");
				rs=ps.executeQuery();
				
				while(rs.next()) {
					String codigo=rs.getString("codigo");
					String descripcio=rs.getString("descripcion");
				
					cl= new TipoDocumentos(codigo,descripcio);
					TD.add(cl);
				}
			
		} catch (KrakedevException e) {
			e.printStackTrace();
			throw e;
		}catch (SQLException e) {
				e.printStackTrace();
				throw new KrakedevException("Error al consultar"+e.getMessage());
			}
		
		return TD;
	}

public void insertar(Proveedores proveedor) throws KrakedevException{
	
	Connection con= null;
	PreparedStatement ps= null;

	
	try {
		con= ConexionBDD.obtenerConexion();
		ps= con.prepareStatement("insert into proveedores(identificador, tipo_de_documento, nombre, telefono, correo, direccion)"
				
				+ " values(?, ?, ?, ?, ?, ?) ");
		
		ps.setString(1, proveedor.getIdentificador());
		ps.setString(2, proveedor.getTipoDocumento().getCodigo());
		ps.setString(3, proveedor.getNombre());
		ps.setString(4, proveedor.getTelefono());
		ps.setString(5, proveedor.getCorreo());
		ps.setString(6, proveedor.getDireccion());
		
		
	
		ps.executeUpdate();
		

		
	}catch (KrakedevException e) {
		e.printStackTrace();
		throw e;
	}catch (SQLException e) {
		e.printStackTrace();
		throw new KrakedevException("error al ingresar un proveedor" + e.getMessage());
	}
	
	
}

public ArrayList<Producto> buscarProducto(String codProd) throws KrakedevException {
	ArrayList<Producto> productos = new ArrayList<Producto>();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Producto producto = null;

	try {
		con = ConexionBDD.obtenerConexion();
		ps = con.prepareStatement("select prod.codigo_prod, prod.nombre as nombre_producto, "
				+ "udm.codigo_udm as nombre_udm, udm.descripcion as descripcion_udm, "
				+ "prod.precio_venta::numeric, prod.tiene_iva, prod.costo::numeric, "
				+ "prod.categoria, cat.nombre as categoria_nombre, prod.stock "
				+ "from productos prod, unidad_medida udm, categorias cat " + "where prod.udm = udm.codigo_udm and "
				+ "prod.categoria = cat.codigo_cat " + "and upper(prod.nombre) like ?");

		ps.setString(1, "%" + codProd.toUpperCase() + "%");
		rs = ps.executeQuery();

		while (rs.next()) {
			int codigoProducto = rs.getInt("codigo_prod");
			String nombreProducto = rs.getString("nombre_producto");
			String nombreUnidadMedida = rs.getString("nombre_udm");
			String descripcionUnidadMedida = rs.getString("descripcion_udm");
			BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
			Boolean tieneIva = rs.getBoolean("tiene_iva");
			BigDecimal costo = rs.getBigDecimal("costo");
			int codigoCategoria = rs.getInt("categoria");
			String nombreCategoria = rs.getString("categoria_nombre");
			int stock = rs.getInt("stock");

			UnidadDeMedida udm = new UnidadDeMedida(nombreUnidadMedida, descripcionUnidadMedida, null);
			Categoria categoria = new Categoria(codigoCategoria, nombreCategoria, null);

			producto = new Producto(codigoProducto, nombreProducto, udm, precioVenta, tieneIva, costo, categoria,
					stock);
			productos.add(producto);
		}

	} catch (KrakedevException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
		throw new KrakedevException("Error al consultar. Detalle: " + e.getMessage());
	}
	return productos;
}

// Método crear producto
public void crearProducto(Producto producto) throws KrakedevException{
	Connection con = null;
	PreparedStatement ps = null;
	try {
		con = ConexionBDD.obtenerConexion();
		ps = con.prepareStatement(
				"insert into productos (nombre, udm, precio_venta, tiene_iva, costo, categoria, stock) values (?,?,?,?,?,?,?)");
		ps.setString(1, producto.getNombre());
		ps.setString(2, producto.getUnidadMedida().getNombre());
		ps.setBigDecimal(3, producto.getPrecioVenta());
		ps.setBoolean(4, producto.isTieneIva());
		ps.setBigDecimal(5, producto.getCoste());
		ps.setInt(6, producto.getCategoria().getCodigo());
		ps.setInt(7, producto.getStock());
		
		ps.executeUpdate();

	} catch (KrakedevException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
		throw new KrakedevException("Error al consultar. Detalle: " + e.getMessage());
	}

}

}



