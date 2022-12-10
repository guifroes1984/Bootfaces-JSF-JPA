package br.com.repository;

import java.util.Date;
import java.util.List;

import br.com.entidades.Lancamento;

public interface IDaoLancamento {
	
	/*Metodo que vai retornar o usuario logado*/
	List<Lancamento> consultar(Long codUser);

	List<Lancamento> consultarLimit10(Long codUser);
	
	List<Lancamento> relatorioLacamento(String numNota, Date dataIni, Date dataFim);

}
