package br.edu.ifba.printer.client.models;

public class MyPrinter {
  private String id;
  private int speed;
  private int sheets;
  private boolean maintenance = false;
  
  public MyPrinter(String id, int speed, int sheets) {
    this.id = id;
    this.speed = speed;
    this.sheets = sheets;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public int getSheets() {
    return sheets;
  }

  public void setSheets(int sheets) {
    this.sheets = sheets;
  }

  public boolean isMaintenance() {
    return maintenance;
  }

  public void setMaintenance(boolean maintenance) {
    this.maintenance = maintenance;
  }
}
