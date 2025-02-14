package br.edu.ifba.printer.server;

import br.edu.ifba.printer.server.models.MyPrinter;
import br.edu.ifba.printer.server.utils.FilterPrinter;
import br.edu.ifba.printer.server.utils.JsonUtils;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/printer")
public class Routes {
  @GET()
  @Path("{id}/{speed}/{sheets}/{maintenance}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response receiveData(
    @PathParam("id") String id, 
    @PathParam("speed") int speed, 
    @PathParam("sheets") int sheetQtty, 
    @PathParam("maintenance") boolean maintenance) 
  {
      MyPrinter printer = new MyPrinter(id, speed, sheetQtty, maintenance);
      DataStore.addData(printer);
      System.out.println("Data received: " + printer.toString());
      return Response.ok("ok").build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllData() {
    String JsonResponse = JsonUtils.toJsonArray(DataStore.getAllData());
    return Response.ok(JsonResponse).build();
  }

  @GET
  @Path("/filter/{maintenance}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response filterData(@PathParam("maintenance") boolean maintenance) {
    FilterPrinter filter = new FilterPrinter();
    String JsonResponse = JsonUtils.toJsonArray(filter.filterByMaintenance(maintenance, DataStore.getAllData()));
    return Response.ok(JsonResponse).build();
  }
}
