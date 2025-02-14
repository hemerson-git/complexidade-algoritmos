package br.edu.ifba.printer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.edu.ifba.printer.client.models.MyPrinter;
import br.edu.ifba.printer.client.utils.ConsolePrinter;

public class HttpHelper implements Runnable {
  private static final String SERVER_URL = "http://localhost:8080/";
  private static final String PRINTER_URL = SERVER_URL + "printer";
  private static final int SPEED_LIMIT = 40;

  private MyPrinter printer;
  
  public void config(MyPrinter printer) {
    this.printer = printer;
  }
  
  public String sendData(MyPrinter data) throws Exception {
      // Complexity: O(N)
      String addPrinterURL = PRINTER_URL + "/" + data.getId() + "/" + data.getSpeed() + "/" + data.getSheets();
      String response = "";
      boolean hasMaintenance = verifyGroupMaintenance(printer);
      
      addPrinterURL += hasMaintenance ? "/true" : "/false";
      
      System.out.println(addPrinterURL);
      
      try {
        @SuppressWarnings("deprecation")
        URL url = new URL(addPrinterURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new Exception("Servidor nao encontrado");
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

      String message = response.equals("ok") ? "Leitura enviada com sucesso: " + printer.getId() : "Falha no envio!";

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
      consolePrinter.print("Impressora " + printer.getId() + " com velocidade " + printer.getSpeed());
    }
    
    return hasMaintenance;
  }
}
