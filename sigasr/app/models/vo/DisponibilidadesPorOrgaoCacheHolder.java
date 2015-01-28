package models.vo;

public class DisponibilidadesPorOrgaoCacheHolder {
	private static final ThreadLocal<DisponibilidadesPorOrgaoCache> THREAD_LOCAL = new ThreadLocal<DisponibilidadesPorOrgaoCache>();

	public static void put(DisponibilidadesPorOrgaoCache disponibilidadesPorOrgao) {
		THREAD_LOCAL.set(disponibilidadesPorOrgao);
	}

	public static DisponibilidadesPorOrgaoCache get() {
		return THREAD_LOCAL.get();
	}
	
	public static void remove() {
		THREAD_LOCAL.remove();
	}
}
