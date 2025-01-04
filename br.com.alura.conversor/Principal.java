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

        Scanner leitura = new Scanner(System.in); 
        String opcao=""; 
        String moedaInicial = "USD";
        double valorInicial = 1;
        String moedaFinal = "USD";
        double valorFinal = 1;
        String sair = "sair";

 
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) 
                .setPrettyPrinting() 
                .create();

        while (!opcao.equalsIgnoreCase(sair)) {

            System.out.println("Seja bem vindo(a) ao Conversor de Moedas!");
            System.out.println("Digite a moeda inicial:");
            moedaInicial = leitura.nextLine();
            System.out.println("Digite a moeda final:");
            moedaFinal = leitura.nextLine();
            System.out.println("Digite o valor a ser corvertido:");
            valorInicial = leitura.nextDouble();


            String endereco = "https://v6.exchangerate-api.com/v6/4bd1f36289d5f1a9016fe246/latest/"+moedaInicial;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco)) 
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            Moeda moeda = gson.fromJson(
                    gson.fromJson(json, JsonObject.class) 
                            .getAsJsonObject("conversion_rates") 
                            .toString(), Moeda.class); 

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


