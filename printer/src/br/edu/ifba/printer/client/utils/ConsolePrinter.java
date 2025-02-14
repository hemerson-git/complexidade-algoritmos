package br.edu.ifba.printer.client.utils;
import java.io.IOException;
import java.io.OutputStream;

public class ConsolePrinter extends OutputStream {
    @Override
    public void write(int b) throws IOException {
        // Converte o byte para um caractere e escreve no console
        System.out.write(b);
    }

    public void print(String text) {
        try {
            System.out.println(text + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
