package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.Bdd.ProvedoresBDD;
import com.krakedev.inventarios.entidades.Proveedores;
import com.krakedev.inventarios.entidades.TipoDocumentos;
import com.krakedev.inventarios.exception.KrakedevException;

@Path("proveedores")

public class ServicxiosProveedores {
	@Path("buscar/{subParam}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("subParam") String sub) {
		ProvedoresBDD con = new ProvedoresBDD();

		ArrayList<Proveedores> RM = null;

		try {
			RM = con.buscar(sub);
			return Response.ok(RM).build();
		} catch (KrakedevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@Path("TipoDocumento")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerTipoDocumento() {
		ProvedoresBDD tipoD = new ProvedoresBDD();
		ArrayList<TipoDocumentos> TD = null;
		try {
			TD = tipoD.recuperarTodos();
			return Response.ok(TD).build();
		} catch (KrakedevException e) {
			e.printStackTrace();
			return Response.serverError().build();

		}

	}
	
	@Path("crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Response crear ( Proveedores proveedor){
		ProvedoresBDD provBDD = new ProvedoresBDD();
	
		try {
			provBDD.insertar(proveedor);
			return Response.ok("Proveedor insertado").build();
			
		} catch (KrakedevException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
}
