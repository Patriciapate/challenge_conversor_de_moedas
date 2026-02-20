package br.com.alura.conversormoedas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;
        String apiKey = "a3e80f4bb01047b6aa06a8dc";
        String baseUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        while (opcao != 7) {
            System.out.println("*****************************************");
            System.out.println("Seja bem-vindo(a) ao Conversor de Moedas!");
            System.out.println();
            System.out.println("1) Dólar =>> Peso Argentino");
            System.out.println("2) Peso Argentino =>> Dólar");
            System.out.println("3) Dólar =>> Real Brasileiro");
            System.out.println("4) Real Brasileiro =>> Dólar");
            System.out.println("5) Dólar =>> Peso Colombiano");
            System.out.println("6) Peso Colombiano =>> Dólar");
            System.out.println("7) Sair");
            System.out.println("*****************************************");

            System.out.print("Escolha uma opção válida: ");
            opcao = scanner.nextInt();

            if (opcao == 7) {
                System.out.println("Encerrando...");
                break;
            }

            System.out.print("Digite o valor que deseja converter: ");
            double valor = scanner.nextDouble();

            try {
                URI uri = new URI(baseUrl);
                URL url = uri.toURL();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                String json = response.toString();

                double brl = pegarCotacao(json, "BRL");
                double ars = pegarCotacao(json, "ARS");
                double cop = pegarCotacao(json, "COP");

                double resultado = 0;

                switch (opcao) {
                    case 1:
                        resultado = valor * ars;
                        System.out.println("Valor " + valor + " USD = " + resultado + " ARS");
                        break;

                    case 2:
                        resultado = valor / ars;
                        System.out.println("Valor " + valor + " ARS = " + resultado + " USD");
                        break;

                    case 3:
                        resultado = valor * brl;
                        System.out.println("Valor " + valor + " USD = " + resultado + " BRL");
                        break;

                    case 4:
                        resultado = valor / brl;
                        System.out.println("Valor " + valor + " BRL = " + resultado + " USD");
                        break;

                    case 5:
                        resultado = valor * cop;
                        System.out.println("Valor " + valor + " USD = " + resultado + " COP");
                        break;

                    case 6:
                        resultado = valor / cop;
                        System.out.println("Valor " + valor + " COP = " + resultado + " USD");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }

                System.out.println();

            } catch (Exception e) {
                System.out.println("Erro ao acessar a API: " + e.getMessage());
            }
        }

        scanner.close();
    }

    public static double pegarCotacao(String json, String moeda) {
        String busca = "\"" + moeda + "\":";
        int inicio = json.indexOf(busca) + busca.length();
        int fim = json.indexOf(",", inicio);

        return Double.parseDouble(json.substring(inicio, fim));
    }
}