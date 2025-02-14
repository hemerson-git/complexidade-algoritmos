package br.edu.ifba.printer.server.models;

public class MyPrinter {
  private String id;
  private int printingSpeed; // quantidade de folhas por minuto
  private int sheetQtty; // quantidade de folhas
  private boolean maintenance;

  public MyPrinter(String id, int printingSpeed, int sheetQtty, boolean maintenance) {
    this.id = id;
    this.printingSpeed = printingSpeed;
    this.sheetQtty = sheetQtty;
    this.maintenance = maintenance;
  }
  
  @Override
  public String toString() {
    return "printer: " + id + "; Velocidade = " + printingSpeed + "; Folhas = " + sheetQtty + "; Manutencao requerida: " + maintenance;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getPrintingSpeed() {
    return printingSpeed;
  }

  public void setPrintingSpeed(int printingSpeed) {
    this.printingSpeed = printingSpeed;
  }

  public int getSheetQtty() {
    return sheetQtty;
  }

  public void setSheetQtty(int sheetQtty) {
    this.sheetQtty = sheetQtty;
  }

  public boolean isMaintenance() {
    return maintenance;
  }

  public void setMaintenance(boolean maintenance) {
    this.maintenance = maintenance;
  }

  public String toJson() {
    return String.format(
      "{\"id\":%s,\"printingSpeed\":%d,\"sheetQtty\":%d,\"maintenance\":%s}",
      id, printingSpeed, sheetQtty, maintenance
    );
  }
}
