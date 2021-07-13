package br.gov.jfrj.siga.model.dao.extended;

public class PageRequest {

	private int number;
	private int size;

	private PageRequest(int number, int size) {
		this.number = number;
		this.size = size;
	}

	/**
	 * Zero-based page request builder.
	 * 
	 * Ex.:<br/>
	 * - PageRequest.of(0, 20) (first page of 20 items)<br/>
	 * - PageRequest.of(1,20) (second page of 20 items)<br/>
	 * - PageRequest.of(2,20) (third page of 20 items)<br/>
	 * 
	 * @param number
	 * @param size
	 * @return
	 */
	public static PageRequest of(int number, int size) {
		return new PageRequest(number, size);
	}

	public int getNumber() {
		return number;
	}

	public int getSize() {
		return size;
	}

	public int getOffset() {
		return number * size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PageRequest other = (PageRequest) obj;
		if (number != other.number) {
			return false;
		}
		if (size != other.size) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PageRequest [number=" + number + ", size=" + size + "]";
	}

}
