package br.com.alura.screenmatch.service.traducao;

import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.net.URLEncoder;

public class MyMemoryApiConnector {
    private static String BASE_URL = "https://api.mymemory.translated.net/get?q=";
    private static ConverteDados conversor = new ConverteDados();

    public static String pegaTraducao(String text) {
        ConsumoApi consumo = new ConsumoApi();

        String texto = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt");

        String url = BASE_URL + texto + "&langpair=" + langpair;
        String response = consumo.obterDados(url);
        DadosTraducao textoTraduzido;

        try {
            textoTraduzido = conversor.obterDados(response, DadosTraducao.class);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return textoTraduzido.traducao().textoTraduzido();
    }
}
