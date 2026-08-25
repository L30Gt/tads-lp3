package br.edu.ifsp.orderflow.infra;

import br.edu.ifsp.orderflow.domain.ItemPedido;
import br.edu.ifsp.orderflow.domain.Pedido;
import br.edu.ifsp.orderflow.domain.Produto;
import br.edu.ifsp.orderflow.service.IEstoqueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryEstoqueService implements IEstoqueService {
    private final Map<String, Integer> estoque = new HashMap<>();


    @Override
    public void adicionarEstoque(Produto produto, int quantidade) {
        int qtdAtual = this.estoque.getOrDefault(produto.getId(), 0);

        this.estoque.put(produto.getId(), quantidade + qtdAtual);
    }


    @Override
    public int quantidadeDisponivel(Produto produto) {
        return this.estoque.getOrDefault(produto.getId(), 0);
    }

    @Override
    public boolean reservar(Pedido pedido) {

        // reservar apenas caso todos os itens do pedido estiverem disponivel no estoque
        for (ItemPedido item : pedido.getItens()) {

            int qtdDisponivel = this.quantidadeDisponivel(item.getProduto());

            if (item.getQuantidade() > qtdDisponivel) {
                return false; // se NAO retornar 'falso' todos os itens possuem estoque
            }

        }

        // remover quantidade garantida em pedido
        for (ItemPedido item : pedido.getItens()) {

            Produto produto = item.getProduto();
            String produtoId = produto.getId(); // item.getProduto().getId()
            int qtdAtual = this.estoque.getOrDefault(produtoId, 0);

            // reservar qtd do item no estoque
            this.estoque.put(produtoId, qtdAtual - item.getQuantidade());

        }

        return true;
    }

    @Override
    public void liberar(Pedido pedido) {
        List<ItemPedido> itens = pedido.getItens();

//      for (ItemPedido item : itens) {
//          Produto produto = item.getProduto();
//          int disponivel = this.quantidadeDisponivel(item.getProduto());
//          this.estoque.put(produto.getId(), disponivel + item.getQuantidade());
//      }

        for (ItemPedido item : itens) {
            this.adicionarEstoque(item.getProduto(), item.getQuantidade());
        }
    }
}
