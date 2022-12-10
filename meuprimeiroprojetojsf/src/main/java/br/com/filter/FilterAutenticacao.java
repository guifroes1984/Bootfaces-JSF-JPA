package br.com.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

@WebFilter(urlPatterns = { "/*" }) /* Vai interceptar todas as paginas "/*" */
public class FilterAutenticacao implements Filter {
	
	@Inject
	private JPAUtil jpaUtil;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;/*Pega o objeto da requisição, que tem todos os dados da requisição, fazendo uma requisição*/
		HttpSession session = req.getSession();/*Pega a sessão do usuário*/
		
		Pessoa usuarioLogado = (Pessoa) session.getAttribute("usuarioLogado");/*Dentro da sessão, carrega o atributo do usuárioLogado, que estáa dentro do objeto Pessoa*/
		
		String url = req.getServletPath();/*É o endereço da URL, para saber o que o usuário está acessando*/
		
		if (!url.equalsIgnoreCase("index.jsf") && usuarioLogado == null) {/*Condição. Se a URL é diferente de index.jsf, e o usuario for nulo. Pega o request.getRequestDispatcher e redireciona para index.jsf */
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsf");
			dispatcher.forward(request, response);/*Pega o dispacher e executa. Tem que ter o return senão ele não faz o redirecionamento*/
			return;
		} else {
			/*Executa as ações do request e do response*/
			chain.doFilter(request, response);/* Toda requisiçao e toda a resposta vai passar aqui dentro. */
		}

	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		jpaUtil.getEntityManager();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
