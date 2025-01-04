package br.com.alura.conversor;

import br.com.alura.modelos.Moeda;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner leitura = new Scanner(System.in); // Scanner para capturar a entrada do usuário
        String opcao=""; // Variável para armazenar a escolha do usuário
        String moedaInicial = "USD";
        double valorInicial = 1;
        String moedaFinal = "USD";
        double valorFinal = 1;
        String sair = "sair";

        // Inicializa o objeto Gson com formatação personalizada
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) // Define a política de nomeação de campos como CamelCase
                .setPrettyPrinting() // Configura o Gson para imprimir o JSON de forma legível
                .create();

        while (!opcao.equalsIgnoreCase(sair)) {

            // Exibe o menu de opções ao usuário
            System.out.println("Seja bem vindo(a) ao Conversor de Moedas!");
            System.out.println("Digite a moeda inicial:");
            moedaInicial = leitura.nextLine();
            System.out.println("Digite a moeda final:");
            moedaFinal = leitura.nextLine();
            System.out.println("Digite o valor a ser corvertido:");
            valorInicial = leitura.nextDouble();


            // URL da API de taxas de câmbio com chave de API
            String endereco = "https://v6.exchangerate-api.com/v6/4bd1f36289d5f1a9016fe246/latest/"+moedaInicial;

            // Cria um cliente HTTP para enviar solicitações
            HttpClient client = HttpClient.newHttpClient();

            // Cria uma solicitação HTTP para o endpoint da API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco)) // Define a URI da API
                    .build();

            // Envia a solicitação e recebe a resposta em formato String
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Armazena o corpo da resposta JSON em uma String
            String json = response.body();

            // Converte o JSON recebido para o objeto `Moeda`
            Moeda moeda = gson.fromJson(
                    gson.fromJson(json, JsonObject.class) // Converte a String JSON para um JsonObject
                            .getAsJsonObject("conversion_rates") // Obtém o objeto "conversion_rates" que contém as taxas de câmbio
                            .toString(), Moeda.class); // Converte as taxas de câmbio para o objeto `Moeda`

            // Configura o formato decimal para exibir até 14 casas decimais
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ENGLISH);
            DecimalFormat decimalFormat = new DecimalFormat("0.00", dfs);


            if (moedaFinal.equalsIgnoreCase("USD")) {
                valorFinal = valorInicial * moeda.USD();

            } else if (moedaFinal.equalsIgnoreCase("ARS")) {
                valorFinal = valorInicial * moeda.ARS();

            } else if (moedaFinal.equalsIgnoreCase("BRL")) {
                valorFinal = valorInicial * moeda.BRL();

            } else if (moedaFinal.equalsIgnoreCase("COP")) {
                valorFinal = valorInicial * moeda.COP();

            } else {
                System.out.println("Valor incorreto!");
            }

            System.out.print("O valor " + valorInicial + " " + moedaInicial.toUpperCase() + " corresponde a " + decimalFormat.format(valorFinal) + " " + moedaFinal.toUpperCase());
            System.out.println("\n");
            System.out.println("Digite qualquer tecla para continuar ou 0 para sair:");
            opcao = leitura.nextLine();

        }

        System.out.println("Programa finalizado");

    }
}


