package br.gov.jfrj.siga.libs.design;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class Menu {
	private String icone;
	private String titulo;
	private String url;
	private boolean ativo;
	private List<Menu> itens = new ArrayList<Menu>();

	public Menu(String icone, String titulo, String url, boolean ativo) {
		super();
		this.icone = icone;
		this.titulo = titulo;
		this.url = url;
		this.ativo = ativo;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Menu> getItens() {
		return itens;
	}

	public void setItens(List<Menu> itens) {
		this.itens = itens;
	}

	public static class MenuBuilder {
		private List<Menu> menus = new ArrayList<Menu>();

		public MenuBuilder() {
		}

		public boolean pode(String servicoPath) {
			return true;
		}

		public MenuBuilder menu(String icone, String titulo, String url,
				boolean ativo) {
			menus.add(new Menu(icone, titulo, url, ativo));
			return this;
		}

		public MenuBuilder item(String icone, String titulo, String url,
				boolean ativo) {
			if (menus.size() == 0)
				throw new RuntimeException(
						"Não é permitido criar item de menu sem antes criar um menu.");
			menus.get(menus.size() - 1).getItens()
					.add(new Menu(icone, titulo, url, ativo));
			return this;
		}

		public MenuBuilder subitem(String icone, String titulo, String url,
				boolean ativo) {
			if (menus.size() == 0)
				throw new RuntimeException(
						"Não é permitido criar item de menu sem antes criar um menu.");
			if (menus.get(menus.size() - 1).getItens().size() == 0)
				throw new RuntimeException(
						"Não é permitido criar subitem de menu sem antes criar um submenu.");
			menus.get(menus.size() - 1).getItens()
					.get(menus.get(menus.size() - 1).getItens().size() - 1)
					.getItens().add(new Menu(icone, titulo, url, ativo));
			return this;
		}

		public MenuBuilder menu(String icone, String titulo, String url,
				String servicoPath) {
			return menu(icone, titulo, url, pode(servicoPath));

		}

		public MenuBuilder item(String icone, String titulo, String url,
				String servicoPath) {
			return item(icone, titulo, url, pode(servicoPath));
		}

		public MenuBuilder subitem(String icone, String titulo, String url,
				String servicoPath) {
			return subitem(icone, titulo, url, pode(servicoPath));
		}

		public List<Menu> build() {
			return menus;
		}
	}
}
