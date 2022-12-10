package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

@Named/*Toda a classe que vai usar a injeção de dependência, tem que ser anotado @Named*/
public class DaoGeneric<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	@Inject
	private JPAUtil jPAUtil;
	
	/*Método de salvar*/
	public void salvar(E entidade) {
		
		//EntityManager entityManager = JPAUtil.getEntityManager();/*Invoca o metodo entity, na classe JPAUtil*/
		
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, 
																			  * salvar, deletar etc...*/
		
		entityTransaction.begin();/*Inicia uma transação*/
		
		entityManager.persist(entidade);/*Está pronto para ser salvo no banco de dados*/
		
		entityTransaction.commit();/*Depois de salvar, pega e comita*/
		
		//entityManager.close();/*Fecha a transação*/
	}
	
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método de salvar ou atualizar*/
	public E merge(E entidade) {
		
		//EntityManager entityManager = JPAUtil.getEntityManager();/*Invoca o metodo entity, na classe JPAUtil*/
		
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, 
																			  * salvar, deletar etc...*/
		
		entityTransaction.begin();/*Inicia uma transação*/
		
		E retorno = entityManager.merge(entidade);/*Está pronto para ser salvo ou atualizado no banco de dados*/
		
		entityTransaction.commit();/*Depois de salvar, pega e comita*/
		
		//entityManager.close();/*Fecha a transação*/
		
		return retorno;
	}
	
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método de delete*/
	public void delete(E entidade) {
		
		//EntityManager entityManager = JPAUtil.getEntityManager();/*Invoca o metodo entity, na classe JPAUtil*/
		
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, 
																			  * salvar, deletar etc...*/
		
		entityTransaction.begin();/*Inicia uma transação*/
		
		entityManager.remove(entidade);/*Está pronto para deletar no banco de dados*/
		
		entityTransaction.commit();/*Depois de salvar, pega e comita*/
		
		//entityManager.close();/*Fecha a transação*/
	}
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	/*Método de delete*/
	public void deletePorId(E entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, salvar, deletar etc...*/
		entityTransaction.begin();/*Inicia uma transação*/
		
		Object id = jPAUtil.getPrimaryKey(entidade);/*Intancia um objeto, passando o id dele, e retorna o id*/
		
		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id).executeUpdate();/*Fazendo o delete passando os parametros como SQL*/
		
		entityTransaction.commit();/*Depois de salvar, pega e comita*/
		
	}
	
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	@SuppressWarnings("unchecked")
	public List<E> getListEntity(Class<E> entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, salvar, deletar etc...*/
		entityTransaction.begin();/* Inicia uma transação */
		
		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();/*Vai fazer a consulta generica*/
		
		entityTransaction.commit();/*Depois de salvar, pega e comita*/
			
		return retorno;
		
	}
	
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	@SuppressWarnings("unchecked")
	public List<E> getListEntityLimit10(Class<E> entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, salvar, deletar etc...*/
		entityTransaction.begin();/* Inicia uma transação */
		
		List<E> retorno = entityManager.createQuery("from " + entidade.getName() + " order by id desc ")
				.setMaxResults(10).getResultList();/*Vai fazer a consulta generica, e limitar em 10 consultar*/
		
		entityTransaction.commit();/*Depois de salvar, pega e comita*/
			
		return retorno;
		
	}
	
	
	/*----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	public E consultar(Class<E> entidade, String codigo) {
		
		EntityTransaction entityTransaction = entityManager.getTransaction();/*Vai iniciar uma transação com o banco de dados, para fazer transação de, salvar, deletar etc...*/
		entityTransaction.begin();/* Inicia uma transação */
		
		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigo));
		entityTransaction.commit();
		return objeto;
		
	}

}
