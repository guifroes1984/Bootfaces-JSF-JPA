package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoLacamentoImpl;
import br.com.repository.IDaoPessoa;
import net.bootsfaces.component.selectOneMenu.SelectOneMenu;

@ViewScoped /*Enquanto estiver na mesma tela ele controla os objetos*/
@Named(value = "pessoaBean")
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	@SuppressWarnings("unused")
	private IDaoLacamentoImpl iDaoLacamentoImpl = new IDaoLacamentoImpl();
	
	@Inject
	private DaoGeneric<Pessoa> daoGeneric;
	
	@Inject
	private IDaoPessoa iDaoPessoa;
	
	@Inject
	private JPAUtil jpaUtil;
	
	private List<SelectItem> estados;
	
	private List<SelectItem> cidades;
	
	private Part arquivofoto;

	public String salvar() throws IOException {
		
		try {
		
			if (arquivofoto != null && arquivofoto.getInputStream() != null) {
				/* Processar imagem */
				byte[] imagemByte = getByte(arquivofoto.getInputStream());
	
			/* Transformar em um bufferimage */
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
			
			if (bufferedImage != null) {
				pessoa.setFotoIconBase64Original(imagemByte);/* Salvar imagem original */
	
				/* Pega o tipo da imagem */
				int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
	
				/* Mostra na tela a miniatura */
				int largura = 200;
				int altura = 200;
	
				/* Criar a miniatura */
				BufferedImage resizedImage = new BufferedImage(altura, largura, type);/* passando a imagem */
				Graphics2D g = resizedImage.createGraphics();/* Cria a parte gráfica da imagem */
				g.drawImage(bufferedImage, 0, 0, largura, altura, null);/* Vai transformar o buffer */
				g.dispose();/* Vai terminar a execução de criação da imagem. Finalmente gravar a imagem */
	
				/* Escrever novamente a imagem em tamanho menor */
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String extensao = arquivofoto.getContentType().split("\\/")[1];/* "/image/png".Saber a extensão desse arquivo */
				ImageIO.write(resizedImage, extensao, baos);/*Vai escrever nesse objeto, pegando o objeto miniatura resizedImage, a extensao dele e o objeto que vai receber os dados "baos".*/
	
				String miniImagem = "data:" + arquivofoto.getContentType() + ";base64,"+ 
				DatatypeConverter.printBase64Binary(baos.toByteArray());/* Vai retorna tudo concatenado tudo em base64, mais a miniImagem */
	
				/* Processar imagem */
				pessoa.setFotoIconbase64(miniImagem);/* Seta a miniatuara */
				pessoa.setExtensao(extensao);/* Tendo a extensão */
		
				}
			
			}
			pessoa = daoGeneric.merge(pessoa);
			if (pessoa != null) {
				carregarPessoas();
				mostrarMsg("Cadastrado com sucesso!");
			} else {
				mostrarMsg("Não foi possível incluir o registro!");
			}
		} catch (Exception e) {
			mostrarMsg("Erro ao adicionar registro!");
			e.printStackTrace();
				
		}

		return "";

	}
		
		
		//pessoa = daoGeneric.merge(pessoa);/*Salva e atualiza na tela mesmo, atraves do merge*/
		//carregarPessoas();/*Carrega a lista para saber que foi salvo*/
		//mostrarMsg("Cadastrado com sucesso!");/*Método para mostrar a mensagem de cadastrado com sucesso*/
		//return "";/*Fica na mesma tela*/
		
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();/*Acessar a parte de contexto do Java Server Faces. O ambiente que está rodando*/
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
		
	}

	public String novo() {/*Executa algum processo antes de novo*/
		pessoa = new Pessoa();/*Clicando no botão novo na tela, limpa a tela e trás um novo objeto*/
		return "";/*Fica na mesma tela*/
	}
	
	public String limpar() {/*Executa algum processo antes de limpar*/
		pessoa = new Pessoa();/*Clicando no botão novo na tela, limpa a tela e trás um novo objeto*/
		return "";/*Fica na mesma tela*/
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();/*Carrega a lista para saber que foi removido*/
		mostrarMsg("Removido com sucesso!");
		return "";/*Fica na mesma tela*/
	}
	
	@PostConstruct/*Sempre que abrir a tela, e o manegebean for instanciado, apos ser criado em memória, ele vai carregar o método que está com "@PostConstruct"*/
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntityLimit10(Pessoa.class);/*Atribuir no banco de dados a lista de pessoas*/
	}
	
	/*Método pesquisaCep*/
	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		try {
			
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");/*URL sempre vai pegar o cep que for digitado na tela*/
			URLConnection connection = url.openConnection();/*Vai abrir a conexão, vai no servidor no webservice e vai consumir*/
			InputStream is = connection.getInputStream();/*Vai executar do lado do servidor, e retornar os dados*/
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));/*Buffered são classes para fazer leitura de dados amazenar.*/
			
			String cep = "";/*Fazer a leitura do cep, e inicia como vazio.*/
			StringBuilder jsonCep = new StringBuilder();/*coloca o json numa String*/
			
			while ((cep = br.readLine()) != null) {/*Vai varrer todas a linhas, e a condição é enquanto as linhas forem diferentes de nulo vai mostrar*/
				jsonCep.append(cep);/*Montagem do json*/
			}
			
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);/*Transformando o resultado para dentro de um objeto para auxiliar para colocar o dados em tela*/
			
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setUnidade(gsonAux.getUnidade());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());
			
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o cep");
		}
		
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que vai deslogar o usuário*/
	public String deslogar() {
		
		FacesContext context = FacesContext.getCurrentInstance();/*Quando queremos qulaquer informação do ambiente de execução do JSF, tem que obter esse objeto*/
		ExternalContext externalContext = context.getExternalContext();/*Pega o contexto*/
		externalContext.getSessionMap().remove("usuarioLogado");/*Remove o usuário na sessão, com o método "remove"*/
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().
		getExternalContext().getRequest();/*É onde tem o controle da sessão do usuário*/
		
		httpServletRequest.getSession().invalidate();/*Invalidando a sessão*/
		
		return "index.jsf";
		
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que consulta para ver se o usuário está logado*/
	public String logar() {
		
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());/*Atribuir a um objeto de pessoa que vai ser consltado no banco de dados. Passando o que veio de tela: login e senha*/
		
		if (pessoaUser !=null) {/*Achou o usuário*/
			
			/*Adicionar o usuário na sessão o usuarioLogado*/
			FacesContext context = FacesContext.getCurrentInstance();/*Quando queremos qulaquer informação do ambiente de execução do JSF, tem que obter esse objeto*/
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);
			
			return "primeirapagina.jsf";
		}else {
			FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Usuário não encontrado"));
		}
		
		return "index.jsf";
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que retorna boolean*/
	public boolean permiteAcesso(String acesso)  {
		
		/*Adicionar o usuário na sessão o usuarioLogado*/
		FacesContext context = FacesContext.getCurrentInstance();/*Quando queremos qulaquer informação do ambiente de execução do JSF, tem que obter esse objeto*/
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return pessoaUser.getPerfilUser().equals(acesso);
		
	}
	
	
	
	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();/*Vai consultar no banco de dados todos os estados*/
		return estados;
	}
	
	@SuppressWarnings("unchecked")
	public void carregaCidades(AjaxBehaviorEvent event) {
		
		Estados estado = (Estados) ((SelectOneMenu)event.getSource()).getValue();
			
			if (estado != null) {
				pessoa.setEstados(estado);
				
				List<Cidades> cidades = jpaUtil.getEntityManager()
										.createQuery("from Cidades where estados.id = "
										+ estado.getId()).getResultList();
				
				List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();
				
				for (Cidades cidade : cidades) {
					selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
				}
				
				setCidades(selectItemsCidade);
				
			}
		
		}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	@SuppressWarnings("unchecked")
	public String editar() {
		
		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados(); /* Já tem na mão o estados que a pessoa pertence */
			pessoa.setEstados(estado); /* Fica com estado selecionado na tela */
			
			/*Tráz a lista de cidades de acordo com o estado selecionado.*/
			List<Cidades> cidades = jpaUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemsCidade);
		}
		
		return "";

	}
	
	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
	
	public List<SelectItem> getCidades() {
		return cidades;
	}
	
	public void setArquivofoto(Part arquivofoto) {
		this.arquivofoto = arquivofoto;
	}
	
	public Part getArquivofoto() {
		return arquivofoto;
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	/*Método que converte InputStream para um array de bytes*/
	private byte[] getByte(InputStream is) throws IOException {/*InputStream=> É como se fosse um texto, aí vai percorrer linha por linha, gravando essas linhas em um arquivo de saida para dps ter o retorno em bytes*/
		
		int len;
		int size = 1024;
		byte[] buf = null;
		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();/*Saída de midia em forma de bytes*/
			buf = new byte[size];
			
			while ((len = is.read(buf, 0, size)) != -1) {/*Passo o valor para a variavle len, e verifico se é diferente de -1 para escrever na variavel bos*/
				bos.write(buf, 0, len);/*passando o buf de saida, de tamaho 0, até variavel len que é o tamanho do dados que está sendo escrito*/
			}
			
			buf = bos.toByteArray();/*Hora que acabar pega a variavel buf atribui ela ao bos.toByteArray e transforma em bytes*/
		}
		
		return buf;
		
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();/*Dentro do JSF tem o FacesContext que conseguimos saber tudo que vem da requisição do JSF. Tem o mapeamento dos parâmetros que está sendo enviado*/
		String fileDownloadId = params.get("fileDownloadId");/*fileDownloadId pega o parâmetro que está sendo enviado, pega pelo pessoa.id, para dps pega a imagem no banco de dados*/
		
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);/*Pega o objeto do banco de dados, passando Pessoa, e o código que veio da tela fileDownload*/
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();/*Resposta do navegador*/
		
		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());/*Setar cabeçalho, tipo de dado, tamanho do dado. Só memorizar ou pegar como colinha */
		response.setContentType("application/octet-stream");/*Seta o formato(tipo) de mídia, dados, arquivos, fotos, imagens...*/
		response.setContentLength(pessoa.getFotoIconBase64Original().length);/*Seta o tamanho do arquivo de resposta*/
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());/*Da uma resposta para escrever a resposta*/
		response.getOutputStream().flush();/*Escreve essa resposta. Confirma o fluxo de dados, confirma a resposta de dados*/
		FacesContext.getCurrentInstance().responseComplete();/*Fala para o JSF que a resposta está completa*/
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	public void mudançaDeValor(ValueChangeEvent evento) {/*Na hora que for salvar, me mostra o nome antigo e o nome novo*/
		System.out.println("valor antigo: " + evento.getOldValue());
		System.out.println("valor novo: " + evento.getNewValue());
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------*/
	
	public void mudançaDeValorSobrenome(ValueChangeEvent evento) {/*Na hora que for salvar, me mostra o sobrenome antigo e o sobrenome novo*/
		System.out.println("valor antigo: " + evento.getOldValue());
		System.out.println("valor novo: " + evento.getNewValue());
	}
	
}
