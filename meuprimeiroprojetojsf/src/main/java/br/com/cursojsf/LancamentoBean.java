package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;


@ViewScoped
@Named(value = "lacamentoBean")
public class LancamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Lancamento lancamento = new Lancamento();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	
	@Inject
	private DaoGeneric<Lancamento> daoGeneric;
	
	@Inject
	private IDaoLancamento daoLancamento;

	public String salvar() {

		/* Adicionar o usuário na sessão o usuarioLogado */
		FacesContext context = FacesContext.getCurrentInstance();/*Quando queremos qulaquer informação do ambiente de execução do JSF, tem que obter esse objeto */
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		lancamento.setUsuario(pessoaUser);
		lancamento = daoGeneric.merge(lancamento);
		FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Cadastrado com sucesso!"));
		
		carregarLacamentos(); /*Vai carregar os lançamentos do usuario logado*/

		return "";
	}
	
	@PostConstruct/*Quando abrir a tela pela primeira vez já vai ser invocado. Vai deixar carregado na tela os lançamentos de acordo com usuário que está logado*/
	private void carregarLacamentos() {
		
		/* Adicionar o usuário na sessão o usuarioLogado */
		FacesContext context = FacesContext.getCurrentInstance();/*Quando queremos qulaquer informação do ambiente de execução do JSF, tem que obter esse objeto */
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		lancamentos = daoLancamento.consultarLimit10(pessoaUser.getId());
		
	}

	public String novo() {
		lancamento = new Lancamento();/*Vai instanciar um novo lancamento*/
		return "";
	}

	public String remove() {
		daoGeneric.deletePorId(lancamento);/**/
		lancamento = new Lancamento();/*Limpa o objeto*/
		carregarLacamentos();/*Carrega os laçamento de novo*/
		FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Excluido com sucesso!"));
		return "";
	}
	

	public Lancamento getLacamento() {
		
		return lancamento;
	}

	public void setLacamento(Lancamento lacamento) {
		this.lancamento = lacamento;
	}

	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

}
