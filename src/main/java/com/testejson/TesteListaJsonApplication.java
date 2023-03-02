package com.testejson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.testejson.financeiro.FaturamentoDia;

@SpringBootApplication
public class TesteListaJsonApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(TesteListaJsonApplication.class, args);
		
		String json = String.join(" ", Files.readAllLines(Paths.get("./dados.json"), StandardCharsets.UTF_8));
		
		List<FaturamentoDia> faturamentoDia = new ArrayList<>();
		
		JsonArray jsonArray = new JsonArray();
		JsonParser parser = new JsonParser();
		
		jsonArray = (JsonArray) parser.parse(new InputStreamReader(new FileInputStream("./dados.json"), "UTF-8"));
		
		Integer dia = 0;
		Double valor = 0.0;
		for(Object object : jsonArray) {
			
			JsonObject faturamento = (JsonObject) object;
			
			dia = Integer.parseInt(faturamento.get("dia").toString());
			valor = Double.parseDouble(faturamento.get("valor").toString());
			
			
			faturamentoDia.add(new FaturamentoDia(dia, valor));      
			
			
		}
		
		Integer quantidadeDiasMedia = 0;
		Double somaValores = 0.0;
		for(FaturamentoDia faturamento : faturamentoDia) {
			if(faturamento.getValor() > 0) {
				quantidadeDiasMedia += 1;
				somaValores += faturamento.getValor();
			}
			
		}
		
		Double menorValor = 0.0;
		for(FaturamentoDia faturamento : faturamentoDia) {
			if(menorValor > 0.0) {
				if(faturamento.getValor() > 0.0 && faturamento.getValor() < menorValor) {
					menorValor = faturamento.getValor();
				}
			}else {
				menorValor = faturamento.getValor();
			}
		}
		
		Double maiorValor = 0.0;
		for(FaturamentoDia faturamento : faturamentoDia) {
			if(maiorValor > 0) {
				if(faturamento.getValor() > maiorValor) {
					maiorValor = faturamento.getValor();
				}
			}else {
				maiorValor = faturamento.getValor();
			}
		}
		
		Double mediaFaturamento = somaValores / quantidadeDiasMedia;
		Integer quantidadeDeDiasMaioresQueMedia = 0;
		
		for(FaturamentoDia faturamento : faturamentoDia) {
			if(faturamento.getValor() > mediaFaturamento) {
				quantidadeDeDiasMaioresQueMedia += 1;
			}
		}
		
		System.out.println(String.format("O menor valor de faturamento em um dia no mês: R$%.2f", menorValor));
		System.out.println(String.format("O maior valor de faturamento em um dia no mês: R$%.2f", maiorValor));
		System.out.println("Número de dias no mês em que o valor de faturamento diário foi superior à media mensal: " + quantidadeDeDiasMaioresQueMedia);
		
		
	}
}
