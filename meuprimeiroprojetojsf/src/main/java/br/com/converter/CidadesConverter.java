package br.com.converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.entidades.Cidades;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Cidades.class, value = "cidadeConverter")
public class CidadesConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override /* Retorna objeto inteiro */
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {

		EntityManager entityManager = CDI.current().select(EntityManager.class).get();

		Cidades cidades = (Cidades) entityManager.find(Cidades.class, Long.parseLong(codigoCidade));

		return cidades;
	}

	@Override /* Retorna apenas o código em String */
	public String getAsString(FacesContext context, UIComponent component,
			Object cidade) {/* Método é executado qdo o objeto vem do servidor para a tela */
		
		if (cidade == null) {
			return null;
		}
		
		if (cidade instanceof Cidades) {
			return ((Cidades) cidade).getId().toString(); /* Retorna apenas o código do objeto, a primary key */
			
		} else {
			return cidade.toString();
		}

		
	}

}
