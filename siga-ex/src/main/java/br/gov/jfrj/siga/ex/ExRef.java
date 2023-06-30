package br.gov.jfrj.siga.ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.base.util.Texto;

public class ExRef {
	private ExDocumento doc;
	private ExMobil mob;
	private List<ExArquivoNumerado> ans;

	public ExRef() {
	}

	public ExRef(ExDocumento doc) {
		this();
		this.doc = doc;
	}

	public ExRef(ExMobil mob) {
		this(mob.doc());
		this.mob = mob;
	}

	public ExRef(ExDocumento doc, ExMobil mob) {
		this(doc);
		this.mob = mob;
	}

	public ExRef(ExDocumento doc, ExMobil mob, List<ExArquivoNumerado> ans) {
		this(doc, mob);
		this.ans = ans;
	}

	private void loadAns() {
		if (ans == null && doc != null) {
			ExMobil pai = doc.getExMobilPai();
			if (pai != null) {
				if (pai.isGeral())
					pai = pai.doc().getMobilDefaultParaReceberJuntada();
				ans = pai.getArquivosNumerados();
			}
			else if (mob != null)
				ans = mob.getArquivosNumerados();
			else {
				ExMobil mobil = doc.getMobilDefaultParaReceberJuntada();
				if (mobil != null)
					ans = mobil.getArquivosNumerados();
			}
		}
	}

	public ExRef modelo(String... mod) {
		if (mod == null || getAns() == null)
			return new ExRef();
		for (int i = 0; i < mod.length; i++)
			mod[i] = Texto.slugify(mod[i], true, true);
		List<ExArquivoNumerado> res = new ArrayList<>();
		for (ExArquivoNumerado an : getAns()) {
			if (an.getMobil() != null && an.getMobil().isJuntadoInterno() && !an.getMobil().doc().isCancelado() && !an.getMobil().doc().isSemEfeito()) {
				String descMod = Texto.slugify(an.getMobil().doc().getExModelo().getNmMod(), true, true);
				for (int i = 0; i < mod.length; i++)
					if (descMod.contains(mod[i]))
						res.add(an);
				if (this.equals(an.getMobil().doc()))
					break;
			}
		}
		return new ExRef(doc, mob, res);
	}

	public ExRef anterior() {
		if (getAns() == null)
			return new ExRef();
		List<ExArquivoNumerado> res = new ArrayList<>();
		for (ExArquivoNumerado an : getAns()) {
			if (an.getMobil() != null) {
				res.add(an);
				if (this.equals(an.getMobil().doc()))
					break;
			}
		}
		return new ExRef(doc, mob, res);
	}

	public ExRef getPrimeiro() {
		if (getAns() == null || getAns().size() == 0)
			return new ExRef();
		return new ExRef(getAns().get(0).getMobil());
	}

	public ExRef getUltimo() {
		if (getAns() == null || getAns().size() == 0)
			return new ExRef();
		return new ExRef(getAns().get(getAns().size() - 1).getMobil());
	}

	public ExRef getPai() {
		if (doc == null || doc.getExMobilPai() == null)
			return new ExRef();
		return new ExRef(doc.getExMobilPai());
	}

	public ExRef getAutuado() {
		if (doc == null || doc.getExMobilAutuado() == null)
			return new ExRef();
		return new ExRef(doc.getExMobilAutuado());
	}

	public Map<String, String> getForm() {
		Map<String, String> map = new HashMap<>();
		if (getAns() != null)
			for (ExArquivoNumerado an : getAns()) {
				if (an.getMobil() != null) {
					Map<String, String> form = an.getMobil().getDoc().getForm();
					if (form != null)
						map.putAll(form);
				}
			}
		return map;
	}

    public String getSigla() {
        if (getAns() == null || getAns().size() == 0)
            return "";
        List<String> l = new ArrayList<>();
        for (ExArquivoNumerado an : getAns())
            if (an.getMobil() != null)
                l.add(an.getMobil().getSigla());
        return Texto.stringsSeparadarComVirgulaEE(l);
    }
    
    public String getFolha() {
        if (getAns() == null || getAns().size() == 0)
            return "";
        List<String> l = new ArrayList<>();
        for (ExArquivoNumerado an : getAns())
            if (an.getMobil() != null) {
                String folha = an.getFolha();
                if (folha != null)
                    l.add(folha);
            }
        return Texto.stringsSeparadarComVirgulaEE(l);
    }

    public String getSiglaEFolha() {
        if (getAns() == null || getAns().size() == 0)
            return "";
        List<String> l = new ArrayList<>();
        for (ExArquivoNumerado an : getAns())
            if (an.getMobil() != null) {
                String folha = an.getFolha();
                l.add(an.getMobil().getSigla() + (folha != null ? " (" + folha + ")" : ""));
            }
        return Texto.stringsSeparadarComVirgulaEE(l);
    }

    public String toString() {
        return getSigla();
    }

	public ExDocumento getDoc() {
		return doc;
	}

	public ExMobil getMob() {
		return mob;
	}

	public List<ExArquivoNumerado> getAns() {
		loadAns();
		return ans;
	}
}
