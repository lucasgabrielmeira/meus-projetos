package lika.java;

import java.io.*;
import java.util.ArrayList;

public class ControleDados {

    public static void salvarProdutos(ArrayList<Produto> produtos, String arquivo) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            outputStream.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Produto> carregarProdutos(String arquivo) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (ArrayList<Produto>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retorna uma lista vazia em caso de erro
        }
    }
}
