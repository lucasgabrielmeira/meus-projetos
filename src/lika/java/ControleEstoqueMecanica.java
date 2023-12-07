package lika.java;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ControleEstoqueMecanica {

    private static ArrayList<Produto> listaProdutos = new ArrayList<>();
    private static JFrame frame;
    private static JTextField codigoField, nomeField, descricaoField, valorCompraField, valorRevendaField, quantidadeEstoqueField, pesquisaField;
    private static JTable tabelaProdutos;
    private static JButton cadastrarBotao, confirmarEdicaoBotao, cancelarEdicaoBotao, excluirBotao, pesquisarBotao;
    private static JButton registrarVendaBotao, registrarCompraBotao;
    private static JTextField quantidadeOperacaoField;

    public static void main(String[] args) {
        System.out.println("Antes de criar ControleEstoqueMecanica");
    
        SwingUtilities.invokeLater(() -> {
            ControleEstoqueMecanica controleEstoque = new ControleEstoqueMecanica();
            try {
                controleEstoque.criarEExibirGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    
        System.out.println("Depois de criar ControleEstoqueMecanica");
    }
    
   
    void criarEExibirGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    
        frame = new JFrame("MECANICA DO LIKA - Controle de Estoque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);
    
        JLabel tituloLabel = new JLabel("LK MECÂNICA AUTOMOTIVA");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(tituloLabel, BorderLayout.NORTH);
    
        // Adicione mensagens de depuração aqui
        System.out.println("Antes de carregar produtos");
    
        listaProdutos = ControleDados.carregarProdutos("produtos.dat");
    
        System.out.println("Depois de carregar produtos");
    
        JPanel painelCadastro = criarPainelCadastro();
        JPanel painelLista = criarPainelLista();
    
        frame.add(painelCadastro, BorderLayout.WEST);
        frame.add(painelLista, BorderLayout.CENTER);
    
        registrarVendaBotao.addActionListener(new RegistrarVendaActionListener());
        registrarCompraBotao.addActionListener(new RegistrarCompraActionListener());
    
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ControleDados.salvarProdutos(listaProdutos, "produtos.dat");
            }
        });
    
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    
        // Adicione outra mensagem de depuração aqui
        System.out.println("GUI exibida com sucesso");
    
        // Adicione esta linha para garantir a inicialização da tabela e dos botões
        atualizarTabelaProdutos();
    }
    

    private static class RegistrarVendaActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int quantidade = Integer.parseInt(quantidadeOperacaoField.getText());
                if (quantidade > 0) {
                    int indiceSelecionado = tabelaProdutos.getSelectedRow();
                    if (indiceSelecionado != -1) {
                        Produto produtoSelecionado = listaProdutos.get(indiceSelecionado);
                        
                        // Verificar se há quantidade suficiente em estoque
                        if (quantidade <= produtoSelecionado.getQuantidadeEstoque()) {
                            // Realizar a venda: diminuir a quantidade em estoque
                            produtoSelecionado.setQuantidadeEstoque(produtoSelecionado.getQuantidadeEstoque() - quantidade);
                            JOptionPane.showMessageDialog(frame, "Venda registrada com sucesso!",
                                    "Venda Registrada", JOptionPane.INFORMATION_MESSAGE);
                            
                            // Atualizar a tabela e salvar os dados
                            atualizarTabelaProdutos();
                            ControleDados.salvarProdutos(listaProdutos, "produtos.dat");
                            quantidadeOperacaoField.setText("");  // Limpa o campo de quantidadeOperacaoField

                        } else {
                            JOptionPane.showMessageDialog(frame, "Quantidade em estoque insuficiente.",
                                    "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Por favor, selecione um produto para realizar a venda.",
                                "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, insira uma quantidade válida.",
                            "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, insira uma quantidade válida.",
                        "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

  
     private static class RegistrarCompraActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int quantidade = Integer.parseInt(quantidadeOperacaoField.getText());
                if (quantidade > 0) {
                    int indiceSelecionado = tabelaProdutos.getSelectedRow();
                    if (indiceSelecionado != -1) {
                        Produto produtoSelecionado = listaProdutos.get(indiceSelecionado);
                        
                        // Realizar a compra: aumentar a quantidade em estoque
                        produtoSelecionado.setQuantidadeEstoque(produtoSelecionado.getQuantidadeEstoque() + quantidade);
                        JOptionPane.showMessageDialog(frame, "Compra registrada com sucesso!",
                                "Compra Registrada", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Atualizar a tabela e salvar os dados
                        atualizarTabelaProdutos();
                        ControleDados.salvarProdutos(listaProdutos, "produtos.dat");
                        quantidadeOperacaoField.setText("");  // Limpa o campo de quantidadeOperacaoField
                    } else {
                        JOptionPane.showMessageDialog(frame, "Por favor, selecione um produto para realizar a compra.",
                                "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor, insira uma quantidade válida.",
                            "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, insira uma quantidade válida.",
                        "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
        
    public class MyTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
            // Adiciona uma borda colorida na célula
            JComponent jc = (JComponent) c;
            jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
    
            // Define outras cores ou estilos conforme necessário
            return c;
        }
    }

    private static void atualizarTabelaProdutos() {
        DefaultTableModel model = (DefaultTableModel) tabelaProdutos.getModel();
        model.setRowCount(0);
    
        // Adiciona o renderer personalizado à tabela
        for (int i = 0; i < tabelaProdutos.getColumnCount(); i++) {
            cadastrarBotao.addActionListener(new CadastrarProdutoActionListener());

        }
    
        for (Produto produto : listaProdutos) {
            model.addRow(new Object[]{produto.getCodigo(), produto.getNome(), produto.getDescricao(),
                    produto.getValorRevenda(), produto.getValorCompra(), produto.getQuantidadeEstoque()});
        }
    }
    

    
    private static void limparCampos() {
        codigoField.setText("");
        nomeField.setText("");
        descricaoField.setText("");
        valorCompraField.setText("");
        valorRevendaField.setText("");
        quantidadeEstoqueField.setText("");
    }

    private static JPanel criarPainelCadastro() {
        JPanel painelCadastro = new JPanel(new GridBagLayout());
        painelCadastro.setBackground(Color.WHITE);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre os componentes
    
        // Adiciona rótulo e campo para Código do Produto
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCadastro.add(new JLabel("Código do Produto:"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 0;
        codigoField = new JTextField(10);
        painelCadastro.add(codigoField, gbc);
    
        // Adiciona rótulo e campo para Nome do Produto
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelCadastro.add(new JLabel("Nome do Produto:"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 1;
        nomeField = new JTextField(20);
        painelCadastro.add(nomeField, gbc);
    
        // Adiciona rótulo e campo para Descrição do Produto
        gbc.gridx = 0;
        gbc.gridy = 2;
        painelCadastro.add(new JLabel("Descrição do Produto:"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 2;
        descricaoField = new JTextField(20);
        painelCadastro.add(descricaoField, gbc);
    
        // Adiciona rótulo e campo para Valor de Compra
        gbc.gridx = 0;
        gbc.gridy = 3;
        painelCadastro.add(new JLabel("Valor de Compra (R$):"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 3;
        valorCompraField = new JTextField(10);
        painelCadastro.add(valorCompraField, gbc);
    
        // Adiciona rótulo e campo para Valor de Revenda
        gbc.gridx = 0;
        gbc.gridy = 4;
        painelCadastro.add(new JLabel("Valor de Revenda (R$):"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 4;
        valorRevendaField = new JTextField(10);
        painelCadastro.add(valorRevendaField, gbc);
    
        // Adiciona rótulo e campo para Quantidade em Estoque
        gbc.gridx = 0;
        gbc.gridy = 5;
        painelCadastro.add(new JLabel("Quantidade em Estoque:"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 5;
        quantidadeEstoqueField = new JTextField(10);
        painelCadastro.add(quantidadeEstoqueField, gbc);
    
        // Adiciona rótulo e campo para Tipo de Operação
        gbc.gridx = 0;
        gbc.gridy = 6;
        painelCadastro.add(new JLabel("Tipo de Operação:"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 6;
        JComboBox<String> tipoOperacaoComboBox = new JComboBox<>(new String[]{"Venda", "Compra"});
        painelCadastro.add(tipoOperacaoComboBox, gbc);
    
        // Adiciona rótulo e campo para Quantidade da Operação
        gbc.gridx = 0;
        gbc.gridy = 7;
        painelCadastro.add(new JLabel("Quantidade da Operação:"), gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 7;
        quantidadeOperacaoField = new JTextField(10);
        painelCadastro.add(quantidadeOperacaoField, gbc);
    
        // Adiciona rótulo e botões para Registrar Venda e Registrar Compra
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JPanel painelBotoesOperacao = new JPanel(new GridLayout(1, 2, 5, 0));
        registrarVendaBotao = new JButton("Registrar Venda");
        registrarVendaBotao.addActionListener(new RegistrarVendaActionListener());
        registrarVendaBotao.setBackground(new Color(255, 165, 0)); // Cor laranja
        painelBotoesOperacao.add(registrarVendaBotao);
    
        registrarCompraBotao = new JButton("Registrar Compra");
        registrarCompraBotao.addActionListener(new RegistrarCompraActionListener());
        registrarCompraBotao.setBackground(new Color(0, 191, 255)); // Cor azul claro
        painelBotoesOperacao.add(registrarCompraBotao);
    
        painelCadastro.add(painelBotoesOperacao, gbc);

        // Criação do botão cadastrarBotao
    cadastrarBotao = new JButton("Cadastrar Produto");
    cadastrarBotao.addActionListener(new CadastrarProdutoActionListener());
    cadastrarBotao.setBackground(new Color(0, 128, 0)); // Cor verde
    painelBotoesOperacao.add(cadastrarBotao);

    painelCadastro.add(painelBotoesOperacao, gbc);
    
        // Adiciona rótulo e botões para Confirmar e Cancelar Edição
        gbc.gridx = 0;
        gbc.gridy = 9;
        JPanel painelBotoesEdicao = new JPanel(new FlowLayout());
        confirmarEdicaoBotao = new JButton("Confirmar Edição");
        confirmarEdicaoBotao.addActionListener(new ConfirmarEdicaoActionListener());
        confirmarEdicaoBotao.setVisible(false);
        painelBotoesEdicao.add(confirmarEdicaoBotao);
    
        cancelarEdicaoBotao = new JButton("Cancelar Edição");
        cancelarEdicaoBotao.addActionListener(new CancelarEdicaoActionListener());
        cancelarEdicaoBotao.setVisible(false);
        painelBotoesEdicao.add(cancelarEdicaoBotao);
    
        painelCadastro.add(painelBotoesEdicao, gbc);
    
        // Adiciona botão para Excluir Produto Selecionado
        gbc.gridx = 0;
        gbc.gridy = 10;
        excluirBotao = new JButton("Excluir Produto Selecionado");
        excluirBotao.addActionListener(new ExcluirProdutoActionListener());
        excluirBotao.setBackground(new Color(255, 0, 0)); // Cor vermelha
        painelCadastro.add(excluirBotao, gbc);
    
        // Adiciona espaço vazio
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        painelCadastro.add(Box.createVerticalGlue(), gbc);
    
        return painelCadastro;
    }    
    
    
    private static JPanel criarPainelLista() {
        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BorderLayout());
        painelLista.setBackground(Color.WHITE);

        // Adiciona rótulo acima da tabela
        JPanel painelRotulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelRotulo.setBackground(Color.WHITE);

        // Rótulo para identificar o campo de pesquisa
        JLabel rotuloPesquisa = new JLabel("Pesquisar Produto:");
        painelRotulo.add(rotuloPesquisa);

        // Ajuste o tamanho do campo de pesquisa conforme necessário
        pesquisaField = new JTextField(20);
        pesquisaField.setPreferredSize(new Dimension(200, pesquisaField.getPreferredSize().height));
        painelRotulo.add(pesquisaField);

        // Adiciona botão de pesquisa
        pesquisarBotao = new JButton("Pesquisar");
        pesquisarBotao.addActionListener(new PesquisarProdutoActionListener());
        painelRotulo.add(pesquisarBotao);

        painelLista.add(painelRotulo, BorderLayout.NORTH);

        tabelaProdutos = new JTable(new DefaultTableModel(new Object[]{"Código", "Nome", "Descrição", "Valor Revenda", "Valor Compra", "Quantidade Estoque"}, 0));
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        painelLista.add(scrollPane, BorderLayout.CENTER);

        JButton visualizarBotao = new JButton("Visualizar Produto Selecionado");
        visualizarBotao.addActionListener(new VisualizarProdutoActionListener());
        JButton editarBotao = new JButton("Editar Produto Selecionado");
        editarBotao.addActionListener(new EditarProdutoActionListener());

        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(Color.WHITE);
        painelBotoes.add(visualizarBotao);
        painelBotoes.add(editarBotao);

        painelLista.add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabelaProdutos();

        return painelLista;
    }

    private static class CadastrarProdutoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int codigo = Integer.parseInt(codigoField.getText());
                String nome = nomeField.getText();
                String descricao = descricaoField.getText();
                double valorCompra = Double.parseDouble(valorCompraField.getText());
                double valorRevenda = Double.parseDouble(valorRevendaField.getText());
                int quantidadeEstoque = Integer.parseInt(quantidadeEstoqueField.getText());

                Produto novoProduto = new Produto(codigo, nome, descricao, valorCompra, valorRevenda, quantidadeEstoque);
                listaProdutos.add(novoProduto);

                limparCampos();
                atualizarTabelaProdutos();
                ControleDados.salvarProdutos(listaProdutos, "produtos.dat");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos corretamente.",
                        "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class ConfirmarEdicaoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int indiceSelecionado = tabelaProdutos.getSelectedRow();
            if (indiceSelecionado != -1) {
                try {
                    Produto produtoSelecionado = listaProdutos.get(indiceSelecionado);
                    produtoSelecionado.setCodigo(Integer.parseInt(codigoField.getText()));
                    produtoSelecionado.setNome(nomeField.getText());
                    produtoSelecionado.setDescricao(descricaoField.getText());
                    produtoSelecionado.setValorCompra(Double.parseDouble(valorCompraField.getText()));
                    produtoSelecionado.setValorRevenda(Double.parseDouble(valorRevendaField.getText()));
                    produtoSelecionado.setQuantidadeEstoque(Integer.parseInt(quantidadeEstoqueField.getText()));

                    limparCampos();
                    confirmarEdicaoBotao.setVisible(false);
                    cancelarEdicaoBotao.setVisible(false);
                    cadastrarBotao.setEnabled(true);

                    atualizarTabelaProdutos();
                    ControleDados.salvarProdutos(listaProdutos, "produtos.dat");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, preencha todos os campos corretamente.",
                            "Erro na Edição", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static class CancelarEdicaoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            limparCampos();
            confirmarEdicaoBotao.setVisible(false);
            cancelarEdicaoBotao.setVisible(false);
            cadastrarBotao.setEnabled(true);
        }
    }

    private static class VisualizarProdutoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int indiceSelecionado = tabelaProdutos.getSelectedRow();
            if (indiceSelecionado != -1) {
                Produto produtoSelecionado = listaProdutos.get(indiceSelecionado);
                JOptionPane.showMessageDialog(frame, produtoSelecionado.obterInformacoesProduto(),
                        "Detalhes do Produto", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static class EditarProdutoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int indiceSelecionado = tabelaProdutos.getSelectedRow();
            if (indiceSelecionado != -1) {
                Produto produtoSelecionado = listaProdutos.get(indiceSelecionado);
                codigoField.setText(String.valueOf(produtoSelecionado.getCodigo()));
                nomeField.setText(produtoSelecionado.getNome());
                descricaoField.setText(produtoSelecionado.getDescricao());
                valorCompraField.setText(String.valueOf(produtoSelecionado.getValorCompra()));
                valorRevendaField.setText(String.valueOf(produtoSelecionado.getValorRevenda()));
                quantidadeEstoqueField.setText(String.valueOf(produtoSelecionado.getQuantidadeEstoque()));

                confirmarEdicaoBotao.setVisible(true);
                cancelarEdicaoBotao.setVisible(true);
                cadastrarBotao.setEnabled(false);
            }
        }
    }

    private static class ExcluirProdutoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int indiceSelecionado = tabelaProdutos.getSelectedRow();
            if (indiceSelecionado != -1) {
                int confirmacao = JOptionPane.showConfirmDialog(frame,
                        "Tem certeza que deseja excluir o produto selecionado?",
                        "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    listaProdutos.remove(indiceSelecionado);
                    atualizarTabelaProdutos();
                    ControleDados.salvarProdutos(listaProdutos, "produtos.dat");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Por favor, selecione um produto para excluir.",
                        "Nenhum Produto Selecionado", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private static class PesquisarProdutoActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String termoPesquisa = pesquisaField.getText().toLowerCase();
            ArrayList<Produto> produtosEncontrados = new ArrayList<>();

            for (Produto produto : listaProdutos) {
                if (produto.getNome().toLowerCase().contains(termoPesquisa) || String.valueOf(produto.getCodigo()).contains(termoPesquisa)) {
                    produtosEncontrados.add(produto);
                }
            }

            DefaultTableModel model = (DefaultTableModel) tabelaProdutos.getModel();
            model.setRowCount(0);

            for (Produto produto : produtosEncontrados) {
                model.addRow(new Object[]{produto.getCodigo(), produto.getNome(), produto.getDescricao(),
                        produto.getValorRevenda(), produto.getValorCompra(), produto.getQuantidadeEstoque()});
            }
        }
    }
}
