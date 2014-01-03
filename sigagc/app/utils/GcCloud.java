package utils;

import java.util.HashMap;
import models.GcTag;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import play.mvc.Router;

public class GcCloud extends Cloud{

	private double maxWeight;
	private double minWeight;
	
	
	public GcCloud(double maxWeight, double minWeight) {
		this.maxWeight = maxWeight;
		this.minWeight = minWeight;
	}
	
	public double getMaxWeight() {
		return maxWeight;
	}
	public double getMinWeight() {
		return minWeight;
	}
	
	public void criarCloud(Object[] tag){
		HashMap<String, Object> map = new HashMap<String, Object>();
		//for (GcTag t : (List<GcTag>) (Object) listaPrincipaisTags) 

		map.clear();
		map.put("filtro.tag.sigla", tag[0].toString());//tag[0] - titulo
		map.put("filtro.situacao", 36);
		map.put("filtro.pesquisa", true);
		String link = Router.reverse("Application.listar", map).url;
		Tag tagCloud =  new Tag(tag[0].toString(), link, Double.parseDouble(tag[1].toString())); //tag[1] - contador 
		this.addTag(tagCloud);
	}
	
	public void criarCloud(Object[] tag, Long idLotacao){
		HashMap<String, Object> map = new HashMap<String, Object>();
		//for (GcTag t : (List<GcTag>) (Object) listaPrincipaisTags) 

		map.clear();
		map.put("filtro.tag.sigla", tag[0].toString());//tag[0] - titulo
		map.put("filtro.lotacao", idLotacao);
		map.put("filtro.situacao", 36);
		map.put("filtro.pesquisa", true);
		String link = Router.reverse("Application.listar", map).url;
		Tag tagCloud =  new Tag(tag[0].toString(), link, Double.parseDouble(tag[1].toString())); //tag[1] - contador 
		this.addTag(tagCloud);
	}
}
