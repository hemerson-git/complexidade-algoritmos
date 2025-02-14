package br.edu.ifba.printer.client;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.printer.client.models.MyPrinter;
import br.edu.ifba.printer.client.utils.GeneratePrinterData;

public class App {
    private static final int TOTAL_PRINTERS = 10;

    public static void main(String[] args) throws Exception {
        List<Thread> processes = new ArrayList<>();
        
        // O(N)
        for (int i = 0; i < TOTAL_PRINTERS; i++) {
            MyPrinter printer = new GeneratePrinterData().generateData();
            
            HttpHelper httpHelper = new HttpHelper();
            httpHelper.config(printer);

            Thread process = new Thread(httpHelper);
            processes.add(process);
            process.start();
        }

        System.out.println("Enviando Leituras para o servidor!");

        // O(N)
        for (Thread process : processes) {
            try {
                process.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
