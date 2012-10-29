package models;

public interface Persistivel {
	
	public Persistivel salvar() throws Exception;
	
	public void finalizar() throws Exception;
	
	public void salvarSimples() throws Exception;
	
	public void darRefresh() throws Exception;
	
	public void flushSeNecessario() throws Exception;

	public void destacar();
	
	public Persistivel buscarPorId(Long id);
	
}
