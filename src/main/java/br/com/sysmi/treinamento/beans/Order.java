package br.com.sysmi.treinamento.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "idCompra", "NomeComprador", "identidade", "items" })
public class Order {
	
	public static final Integer LIMIT_ITEMS = 7;

	@JsonProperty("idCompra")
	private Integer idCompra;
	@JsonProperty("NomeComprador")
	private String nomeComprador;
	@JsonProperty("identidade")
	private Integer identidade;
	@JsonProperty("items")
	private List<Item> items = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("idCompra")
	public Integer getIdCompra() {
		return idCompra;
	}

	@JsonProperty("idCompra")
	public void setIdCompra(Integer idCompra) {
		this.idCompra = idCompra;
	}

	@JsonProperty("NomeComprador")
	public String getNomeComprador() {
		return nomeComprador;
	}

	@JsonProperty("NomeComprador")
	public void setNomeComprador(String nomeComprador) {
		this.nomeComprador = nomeComprador;
	}

	@JsonProperty("identidade")
	public Integer getIdentidade() {
		return identidade;
	}

	@JsonProperty("identidade")
	public void setIdentidade(Integer identidade) {
		this.identidade = identidade;
	}

	@JsonProperty("items")
	public List<Item> getItems() {
		return items;
	}

	@JsonProperty("items")
	public void setItems(List<Item> items) {
		this.items = items;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idCompra", idCompra).append("nomeComprador", nomeComprador)
				.append("identidade", identidade).append("items", items)
				.append("additionalProperties", additionalProperties).toString();
	}
	
	@JsonIgnore
    public boolean checkLimitItems(Integer totalItems) {
		Logger.getLogger(getClass()).info("Checando limite de itens. Total de Itens: " + totalItems);

        return totalItems >= LIMIT_ITEMS;
    }

}