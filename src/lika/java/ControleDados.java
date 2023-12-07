package lika.java;

import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;

public class ControleDados implements Serializable {

    // Método para salvar produtos em um arquivo
    public static void salvarProdutos(ArrayList<Produto> produtos, String nomeArquivo) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            outputStream.writeObject(produtos);
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static ArrayList<Produto> carregarProdutos(String arquivo) {
        ArrayList<Produto> produtos = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object obj = ois.readObject();

            if (obj instanceof ArrayList<?>) {
                produtos = (ArrayList<Produto>) obj;
            } else {
                // Trate o caso em que o objeto lido não é um ArrayList<Produto>
                System.err.println("Erro: O objeto lido não é um ArrayList<Produto>.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return produtos;
    }
}
