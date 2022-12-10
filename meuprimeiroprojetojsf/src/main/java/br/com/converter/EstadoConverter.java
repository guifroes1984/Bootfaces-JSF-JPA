package br.com.converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override /* Retorna objeto inteiro */
	public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {

		EntityManager entityManager = CDI.current().select(EntityManager.class).get();

		Estados estados = (Estados) entityManager.find(Estados.class, Long.parseLong(codigoEstado));

		return estados;
	}

	@Override /* Retorna apenas o código em String */
	public String getAsString(FacesContext context, UIComponent component,
			Object estado) {/* Método é executado qdo o objeto vem do servidor para a tela */
		
		if (estado == null) {
			return null;
		}
		
		if (estado instanceof Estados) {
			return ((Estados) estado).getId().toString(); /* Retorna apenas o código do objeto, a primary key */
			
		}else {
			return estado.toString();
		}

		
	}

}
