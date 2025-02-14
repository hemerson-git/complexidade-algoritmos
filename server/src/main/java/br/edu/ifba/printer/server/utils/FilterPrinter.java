package br.edu.ifba.printer.server.utils;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.printer.server.models.MyPrinter;

public class FilterPrinter {
  public List<MyPrinter> filterByMaintenance(boolean maintenance, List<MyPrinter> printers) {
    List<MyPrinter> filteredPrinters = new ArrayList<>();

    //O(N)
    for(MyPrinter printer : printers) {
      if(printer.isMaintenance() == maintenance) {
        filteredPrinters.add(printer);
      }
    }
    
    return filteredPrinters;
  }
}
