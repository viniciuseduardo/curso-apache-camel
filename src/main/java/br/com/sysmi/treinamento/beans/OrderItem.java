package br.com.sysmi.treinamento.beans;

import java.util.HashMap;
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
@JsonPropertyOrder({ "itemId", "sigla", "descricao", "preco", "quantidade" })
public class OrderItem {

	@JsonIgnore
	private Integer orderId;
	@JsonProperty("itemId")
	private Integer itemId;
	@JsonProperty("sigla")
	private String sigla;
	@JsonProperty("descricao")
	private String descricao;
	@JsonProperty("preco")
	private Double preco;
	@JsonProperty("quantidade")
	private Integer quantidade;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}	
	
	@JsonProperty("itemId")
	public Integer getItemId() {
		return itemId;
	}

	@JsonProperty("itemId")
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@JsonProperty("sigla")
	public String getSigla() {
		return sigla;
	}

	@JsonProperty("sigla")
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@JsonProperty("descricao")
	public String getDescricao() {
		return descricao;
	}

	@JsonProperty("descricao")
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@JsonProperty("preco")
	public Double getPreco() {
		return preco;
	}

	@JsonProperty("preco")
	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@JsonProperty("quantidade")
	public Integer getQuantidade() {
		return quantidade;
	}

	@JsonProperty("quantidade")
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
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
		return new ToStringBuilder(this).append("itemId", itemId).append("sigla", sigla).append("descricao", descricao)
				.append("preco", preco).append("quantidade", quantidade)
				.append("additionalProperties", additionalProperties).toString();
	}
	
	public boolean hasStock() {
		if( this.sigla.equals("TDK") ) {
			Logger.getLogger(OrderItem.class).info("Item do Pedido sem estoque: \n " + this.toString());
			return false;
		}
		return true;
	}

}
