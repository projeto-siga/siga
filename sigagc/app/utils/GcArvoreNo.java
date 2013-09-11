package utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import models.GcInformacao;
import models.GcTag;

public class GcArvoreNo implements Comparable<GcArvoreNo> {
	protected GcTag tag;
	protected GcArvoreNo raiz;
	protected HashMap<GcTag, GcArvoreNo> nos = new HashMap<GcTag, GcArvoreNo>();
	protected List<GcInformacao> infs = new ArrayList<GcInformacao>();
	protected TreeSet<GcArvoreNo> nosOrdenados;
	protected GcArvore arvore;
	protected List<GcTag> tagsIrmaos = new ArrayList<GcTag>();

	public GcArvoreNo(GcArvore arvore, GcTag tag, GcInformacao inf) {
		this.arvore = arvore;
		this.tag = tag;
		this.infs.add(inf);
	}

	public GcArvoreNo() {
		// TODO Auto-generated constructor stub
	}

	public void add(GcInformacao inf) {
		this.infs.add(inf);
	}

	public void add(GcArvoreNo no) {
		assert (!this.nos.containsKey(no.tag));
		this.nos.put(tag, no);
	}

	@Override
	public int compareTo(GcArvoreNo o) {
		final int c1 = infs.size();
		final int c2 = o.infs.size();
		if (c1 != c2)
			return (c1 < c2 ? -1 : (c1 == c2 ? 0 : 1));
		final int f1 = (int) (long) arvore.mapFreq.get(this.tag);
		final int f2 = (int) (long) arvore.mapFreq.get(o.tag);
		if (f1 != f2)
			return (f1 < f2 ? -1 : (f1 == f2 ? 0 : 1));
		if (!tag.titulo.equals(o.tag.titulo))
			return tag.titulo.compareTo(o.tag.titulo);
		return (tag.id < o.tag.id ? -1 : (tag.id == o.tag.id ? 0 : 1));
	}

	public void build() {
		nosOrdenados = new TreeSet<GcArvoreNo>(new Comparator<GcArvoreNo>() {
			@Override
			public int compare(GcArvoreNo o1, GcArvoreNo o2) {
				return o2.compareTo(o1);
			}
		});
		nosOrdenados.addAll(nos.values());

		while (nosOrdenados.size() == 1) {
			GcArvoreNo noFilho = nosOrdenados.first();
			tagsIrmaos.add(noFilho.tag);
			nosOrdenados.remove(noFilho);
			nosOrdenados.addAll(noFilho.nos.values());
		}

		for (GcArvoreNo noFilho : nosOrdenados) {
			noFilho.build();
		}
	}

}
