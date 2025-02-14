package br.edu.ifba.printer.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import br.edu.ifba.printer.server.models.MyPrinter;

public class DataStore {
    private static final List<MyPrinter> dataStore = new CopyOnWriteArrayList<>();

    public static void addData(MyPrinter data) {
        dataStore.add(data);
    }

    public static List<MyPrinter> getAllData() {
        return dataStore;
    }
}
