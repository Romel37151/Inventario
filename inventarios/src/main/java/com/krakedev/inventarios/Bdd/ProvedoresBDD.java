package com.krakedev.inventarios.Bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedores;
import com.krakedev.inventarios.entidades.TipoDocumentos;
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


}
