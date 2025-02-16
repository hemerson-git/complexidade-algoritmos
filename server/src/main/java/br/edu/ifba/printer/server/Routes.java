package br.edu.ifba.printer.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  private static final String ENCRYPTION_ALGORITHM = "RSA";
  private static final String PRIVATE_KEY_PATH = "/misc/ifba/workspaces/complexidade/10/servidor/pacientes/chave/privada.chv";
  
  private PrivateKey chave = null;

    private PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      if (chave == null) {
        File file = new File(PRIVATE_KEY_PATH);
        byte[] bytes;
        try (FileInputStream stream = new FileInputStream(file)) {
            bytes = stream.readAllBytes();
        }

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        chave = kf.generatePrivate(spec);
      }

      return chave;
    }
  
  private String decrypt(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
      InvalidKeyException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());

    byte[] decrypted = cipher.doFinal(encrypted);

    return new String(decrypted);
  }
    
  @GET()
  @Path("{id}/{speed}/{sheets}/{maintenance}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response receiveData(@PathParam("encrypted") String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException
  {
      String json = decrypt(Base64.getUrlDecoder().decode(encrypted));

      ObjectMapper mapper = new ObjectMapper();
      JsonNode dict = mapper.readTree(json);

      String id = dict.get("id").asText();
      int speed = dict.get("speed").asInt();
      int sheetQtty = dict.get("sheetQtty").asInt();
      boolean maintenance = dict.get("maintenance").asBoolean();
    
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
