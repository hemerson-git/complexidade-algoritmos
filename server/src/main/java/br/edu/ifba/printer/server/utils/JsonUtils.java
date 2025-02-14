package br.edu.ifba.printer.server.utils;

import java.util.List;

import br.edu.ifba.printer.server.models.MyPrinter;

public class JsonUtils {
  public static String toJsonArray(List<MyPrinter> printers) {
    StringBuilder json = new StringBuilder();

    json.append("[");

    for (int i = 0; i < printers.size(); i++) {
        json.append(printers.get(i).toJson());
        if (i < printers.size() - 1) {
            json.append(",");
        }
    }

    json.append("]");

    return json.toString();
  }
}
