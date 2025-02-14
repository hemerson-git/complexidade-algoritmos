package br.edu.ifba.printer.client.utils;

import java.util.UUID;

import br.edu.ifba.printer.client.models.MyPrinter;

public class GeneratePrinterData {
  public MyPrinter generateData() {
    String id = UUID.randomUUID().toString();
    int speed = (int) (Math.random() * 60) + 10;
    int sheets = (int) (Math.random() * 500) + 100;
    return new MyPrinter(id, speed, sheets);
  }
}
