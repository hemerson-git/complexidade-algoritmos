package br.edu.ifba.printer.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifba.printer.client.models.MyPrinter;
import br.edu.ifba.printer.client.utils.ConsolePrinter;

public class HttpHelper implements Runnable {
  private static final String SERVER_URL = "http://localhost:8080/";
  private static final String PRINTER_URL = SERVER_URL + "printer/";
  private static final int SPEED_LIMIT = 40;

  private static final String ENCRYPTION_ALGORITHM = "RSA";
  private static final String PUBLIC_KEY_PATH = "C:/Users/hemer/OneDrive/Área de Trabalho/IFBA/complexidade_algoritmos/printer_version2/printer/key/public.key";
  
  private MyPrinter printer;
  
  public void config(MyPrinter printer) {
    this.printer = printer;
  }
  
  private PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    File file = new File(PUBLIC_KEY_PATH);
    FileInputStream stream = new FileInputStream(file);
    byte[] bytes = stream.readAllBytes();
    stream.close();

    X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
    KeyFactory kf = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
    PublicKey publicKey = kf.generatePublic(spec);

    return publicKey;
  }

  private byte[] encrypt(String json) throws NoSuchAlgorithmException, NoSuchPaddingException,
          InvalidKeyException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {
      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());

      byte[] encrypted = cipher.doFinal(json.getBytes());

      return encrypted;
  }
  
  public String sendData(MyPrinter data) throws Exception {
      Map<String, String> json = new HashMap<>();
      json.put("id", data.getId());
      json.put("speed", String.valueOf(data.getSpeed()));
      json.put("sheets", String.valueOf(data.getSheets()));

      
      // Complexity: O(N)
      String response = "";
      ObjectMapper mapper = new ObjectMapper();
      boolean hasMaintenance = verifyGroupMaintenance(printer);
      
      json.put("maintenance", hasMaintenance ? "true" : "false");
      String addPrinterURL = PRINTER_URL + new String(Base64.getUrlEncoder().encode(encrypt(mapper.writeValueAsString(json))));
      
      System.out.println(addPrinterURL);
      
      try {
        @SuppressWarnings("deprecation")
        URL url = new URL(addPrinterURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new Exception("Server not found");
        }

        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader buffer = new BufferedReader(in);
        response = buffer.readLine();
        
        conn.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }

      return response;
  }

  @Override
  public void run() {
    try {
      String response = sendData(printer);

      String message = response.equals("ok") ? "Reading sent successfully: " + printer.getId() : "Sending failed!";

      try(ConsolePrinter consolePrinter = new ConsolePrinter()) {
        consolePrinter.print(response);
      }
      
        try (ConsolePrinter consolePrinter = new ConsolePrinter()) {
          consolePrinter.print(message);
        }
      
      Thread.sleep(400);
    } catch (Exception e) {
      e.printStackTrace();
    }
      
  }

  public Boolean verifyGroupMaintenance(MyPrinter printer) throws IOException {
    // Complexity: O(1)
    Boolean hasMaintenance = printer.getSpeed() < SPEED_LIMIT;
    
    try (ConsolePrinter consolePrinter = new ConsolePrinter()) {
      consolePrinter.print("Printer " + printer.getId() + " with speed " + printer.getSpeed());
    }
    
    return hasMaintenance;
  }
}
