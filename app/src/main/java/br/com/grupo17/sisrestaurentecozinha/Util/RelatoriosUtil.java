package br.com.grupo17.sisrestaurentecozinha.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.Pedido;
import br.com.grupo17.sisrestaurentecozinha.R;

public class RelatoriosUtil {
    private int numeroDePedidos, pratoMaisPedido, pratoMenosPedido;
    private Administracao.RelatoriosVendasPresenter presenter;
    private HashMap<String, Integer> map = new HashMap<>();
    private String nomeMais, nomeMenos;
    private List<Pedido> pedidoList;
    private Float somatorioPreco;

    public RelatoriosUtil(Administracao.RelatoriosVendasPresenter presenter) {
        this.numeroDePedidos = 0;
        this.pratoMaisPedido = 0;
        this.pratoMenosPedido = 0;
        this.presenter = presenter;
        this.somatorioPreco = (float) 0;
        this.pedidoList = new ArrayList<>();
    }

    public void addItemList(Pedido pedido) {
        pedidoList.add(pedido);
    }

    public void geraInfos() {
        numeroDePedidos = numeroDePedidos + pedidoList.size();

        for (Pedido pedido : pedidoList) {
            somatorioPreco = somatorioPreco + pedido.getPreco();

            if (map.containsKey(pedido.getPratoNome())) {
                try {
                    map.put(pedido.getPratoNome(), Objects.requireNonNull(map.get(pedido.getPratoNome())) + 1);
                } catch (NullPointerException e) {
                    SpAndToast.showMessage(presenter.getContext(), "Não foi possivel pegar info de um item");
                }
            } else {
                map.put(pedido.getPratoNome(), 1);
            }
        }

        presenter.retriveDados();
    }

    public Map<String, Object> getInfo() {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (pratoMenosPedido == 0) {
                pratoMenosPedido = entry.getValue();
                nomeMenos = entry.getKey();
            } else {
                if (entry.getValue() < pratoMenosPedido) {
                    pratoMenosPedido = entry.getValue();
                    nomeMenos = entry.getKey();
                } else if (entry.getValue() == pratoMenosPedido) {
                    nomeMenos = nomeMenos.concat(", " + entry.getKey());
                }
            }

            if (entry.getValue() > pratoMaisPedido) {
                pratoMaisPedido = entry.getValue();
                nomeMais = entry.getKey();
            } else if (entry.getValue() == pratoMaisPedido) {
                nomeMais = nomeMais.concat(", " + entry.getKey());
            }
        }

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(presenter.getContext().getString(R.string.key_relatorioVendas_nome_mais), this.nomeMais != null ? this.nomeMais : "");
        objectMap.put(presenter.getContext().getString(R.string.key_relatorioVendas_nome_menos), this.nomeMenos != null ? this.nomeMenos : "");
        objectMap.put(presenter.getContext().getString(R.string.key_relatorioVendas_quantidade_mais), this.pratoMaisPedido != -1 ? this.pratoMaisPedido : 0);
        objectMap.put(presenter.getContext().getString(R.string.key_relatorioVendas_quantidade_menos), this.pratoMenosPedido != -1 ? this.pratoMenosPedido : 0);
        objectMap.put(presenter.getContext().getString(R.string.key_relatorioVendas_quantidade_pedido), this.numeroDePedidos != -1 ? this.numeroDePedidos : 0);
        objectMap.put(presenter.getContext().getString(R.string.key_campo_preco), this.somatorioPreco != -1 ? this.somatorioPreco : 0);
        return objectMap;
    }

    public void resetInfo() {
        this.map.clear();
        this.nomeMais = null;
        this.nomeMenos = null;
        this.pedidoList.clear();
        this.numeroDePedidos = 0;
        this.pratoMaisPedido = 0;
        this.pratoMenosPedido = 0;
        this.somatorioPreco = (float) 0;
    }
}