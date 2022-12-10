package br.com.jpautil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped/*Uma isntância só no projeto inteiro*/
public class JPAUtil {/*Essa classe que vai retornar o entity responsável pela persistência, usamos para fazer os metodos de: SALVAR, DELETAR...
						Toda a parte relacionada com Banco de Dados, junto com a aplicação.*/
	
	private EntityManagerFactory factory;
	
	public JPAUtil() {
		if (factory == null) {/*Condição se o factory = null vai criar uma unica vez*/
			factory = Persistence.createEntityManagerFactory("meuprimeiroprojetojsf");
		}
	}
	
	@Produces/*Produzir as EntityManeger, vai injetar automaticamente*/
	@RequestScoped/*A cada clique no salvar, ou no excluir ou editar, vai criar uma nova EntityManager pra nós fazermos as operações para o banco de dados*/
	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
	/*Aqui é como se fosse um Query*/
	public Object getPrimaryKey(Object entity) {
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
	}

}
