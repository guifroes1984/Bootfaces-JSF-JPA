package br.com.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//@NotEmpty/*O nome tem que ser sempre informado*/
	//@Size(min = 10, max = 50, message = "Nome deve ter entre 10 e 50 letras")/*Validação de minimo e máximo de letras do lado do backend*/
	private String nome;
	
	//@NotEmpty(message = "Sobre nome deve ser informado")/*Validadção do sobrenome no back-end.*/
	//@NotNull(message = "Sobrenome deve ser informado")/*Validadção do sobrenome no back-end. Não pode ser nulo*/
	private String sobrenome;
	
	//@DecimalMax(value = "50", message = "Idade deve ser maior que 50")/*Validadção idade máxima*/
	//@DecimalMin(value = "10", message = "Idade dever ser maior que 10")/*Validadção idade minima*/
	private Integer idade;

	@Temporal(TemporalType.DATE)
	private Date dataNascimento = new Date();/* Data de hoje */
	
	private String sexo;

	private String[] frameworks;
	
	@CPF(message = "Cpf Inválido")
	private String cpf;
	
	//@TituloEleitoral(message = "Titulo eleitoral inválido")
	private String titEleitoral;

	private Boolean ativo;/* Sempre usar os objetos, nunca os de tipos primitivos */
	
	
	private String login;
	private String senha;

	private String perfilUser;

	private String nivelProgramador;

	private Integer[] linguagens;

	private String cep;

	private String logradouro;

	private String complemento;

	private String bairro;

	private String localidade;

	private String uf;

	private String unidade;

	private String ibge;

	private String gia;
	
	@ManyToOne/*Muitas pesssoas numa cidade*/
	private Cidades cidades;
	
	@Transient/*Não fica persistente ou não grava no banco de dados*/
	private Estados estados;
	
	@Column(columnDefinition = "text")/*Tipo text grava arquivos em base 64. Campo text aceita um tamanho gigantesco*/
	private String fotoIconbase64;
	
	private String extensao;/*Extensão jpg, png, jpeg*/
	
	@Lob/*Grava arquivos no banco de dados*/
	@Basic(fetch = FetchType.LAZY)/*Não precisa carregar sempre essa foto, só é carregada quando for chamada o atributo para retornar a base 64*/
	private byte[] fotoIconBase64Original;
	
	public String getFotoIconbase64() {
		return fotoIconbase64;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setTitEleitoral(String titEleitoral) {
		this.titEleitoral = titEleitoral;
	}
	
	public String getTitEleitoral() {
		return titEleitoral;
	}

	public void setFotoIconbase64(String fotoIconbase64) {
		this.fotoIconbase64 = fotoIconbase64;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public byte[] getFotoIconBase64Original() {
		return fotoIconBase64Original;
	}

	public void setFotoIconBase64Original(byte[] fotoIconBase64Original) {
		this.fotoIconBase64Original = fotoIconBase64Original;
	}

	public Cidades getCidades() {
		return cidades;
	}
	
	public void setCidades(Cidades cidades) {
		this.cidades = cidades;
	}
	
	public Estados getEstados() {
		return estados;
	}
	
	public void setEstados(Estados estados) {
		this.estados = estados;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCep() {
		return cep;
	}

	public Integer[] getLinguagens() {
		return linguagens;
	}

	public void setLinguagens(Integer[] linguagens) {
		this.linguagens = linguagens;
	}

	public String getNivelProgramador() {
		return nivelProgramador;
	}

	public void setNivelProgramador(String nivelProgramador) {
		this.nivelProgramador = nivelProgramador;
	}

	public String getPerfilUser() {
		return perfilUser;
	}

	public void setPerfilUser(String perfilUser) {
		this.perfilUser = perfilUser;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String[] getFrameworks() {
		return frameworks;
	}

	public void setFrameworks(String[] frameworks) {
		this.frameworks = frameworks;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Pessoa() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}

}
