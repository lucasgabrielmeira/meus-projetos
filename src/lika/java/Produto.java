package lika.java;


import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int codigo;
    private String nome;
    private String descricao;
    private double valorCompra;
    private double valorRevenda;
    private int quantidadeEstoque;

    public Produto(int codigo, String nome, String descricao, double valorCompra, double valorRevenda, int quantidadeEstoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.valorCompra = valorCompra;
        this.valorRevenda = valorRevenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public double getValorRevenda() {
        return valorRevenda;
    }

    public void setValorRevenda(double valorRevenda) {
        this.valorRevenda = valorRevenda;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public double calcularValorTotal() {
        return valorRevenda * quantidadeEstoque;
    }

    public String obterInformacoesProduto() {
        StringBuilder details = new StringBuilder("\n--- Detalhes do Produto ---\n");
        details.append("Código: ").append(codigo).append("\n");
        details.append("Nome: ").append(nome).append("\n");
        details.append("Descrição: ").append(descricao).append("\n");
        details.append("Valor de Compra: R$").append(valorCompra).append("\n");
        details.append("Valor de Revenda: R$").append(valorRevenda).append("\n");
        details.append("Quantidade em Estoque: ").append(quantidadeEstoque).append("\n");
        details.append("Valor Total no Estoque: R$").append(calcularValorTotal()).append("\n");

        return details.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Produto produto = (Produto) obj;

        return codigo == produto.codigo;
    }

    @Override
    public int hashCode() {
        return codigo;
    }
}
