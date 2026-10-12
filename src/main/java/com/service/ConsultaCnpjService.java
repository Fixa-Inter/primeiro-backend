package com.service;

import com.DTO.EnderecoViaCnpjDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class ConsultaCnpjService {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public EnderecoViaCnpjDTO consultar(String cnpj){

        String cnpjFormatado = cnpj.replaceAll("\\D", ""); // \\D siginifica para tirar qualquer caractere que nao seja numero

        if (!cnpjValido(cnpjFormatado)){
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://brasilapi.com.br/api/cnpj/v1/" + cnpjFormatado))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200){
                return null;
            }

            JsonNode jsonNode = mapper.readTree(response.body());

            EnderecoViaCnpjDTO dto = new EnderecoViaCnpjDTO();

            dto.setRazaoSocial(textoOuNull(jsonNode, "razao_social"));
            dto.setSituacaoCadastral(textoOuNull(jsonNode, "descricao_situacao_cadastral"));
            dto.setRua(textoOuNull(jsonNode, "logradouro"));
            dto.setNumero(textoOuNull(jsonNode, "numero"));
            dto.setComplemento(textoOuNull(jsonNode, "complemento"));
            dto.setBairro(textoOuNull(jsonNode, "bairro"));
            dto.setCidade(textoOuNull(jsonNode, "municipio"));
            dto.setEstado(textoOuNull(jsonNode, "uf"));
            dto.setCep(textoOuNull(jsonNode, "cep"));

            return dto;

        } catch (IOException e) {
            System.out.print("Erro ao acessar Brasil API: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.print("Erro ao acessar Brasil API: " + e.getMessage());
        }
        return null;
    }

    private String textoOuNull(JsonNode jsonNode, String campo){
        JsonNode node = jsonNode.get(campo);
        return (node == null || node.isNull() ? null : node.asText());
    }

    public static  boolean cnpjValido(String cnpj){

        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1){
            return false;
        }

        int[] peso1 = {5,4,3,2,9,8,7,6,5,4,3,2};
        int[] peso2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};

        int d1 = calcularDigito(cnpj.substring(0, 12), peso1);
        int d2 = calcularDigito(cnpj.substring(0, 12) + d1, peso2);

        return cnpj.equals(cnpj.substring(0, 12) + d1 + d2);

    }

    private static int calcularDigito(String base, int[] pesos){
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += (base.charAt(i) - '0') * pesos[i];
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

}
