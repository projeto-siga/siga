package br.gov.jfrj.siga.model.dao.extended;

import java.util.List;

public class Page<R> {

	private int number;
	private int size;
	private List<R> items;
	private long totalItemsCount;

	private Page(int number, int size, List<R> items, long totalItemsCount) {
		this.number = number;
		this.size = size;
		this.items = items;
		this.totalItemsCount = totalItemsCount;
	}

	public static <R> Page<R> of(int number, int size, List<R> items, long totalItemsCount) {
		return new Page<>(number, size, items, totalItemsCount);
	}

	public static <R> Page<R> of(PageRequest request, List<R> items, long totalItemsCount) {
		return new Page<>(request.getNumber(), request.getSize(), items, totalItemsCount);
	}

	public int getNumber() {
		return number;
	}

	public int getSize() {
		return size;
	}

	public List<R> getItems() {
		return items;
	}

	public long getTotalItemsCount() {
		return totalItemsCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + size;
		result = prime * result + (int) (totalItemsCount ^ (totalItemsCount >>> 32));
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
		Page<?> other = (Page<?>) obj;
		if (number != other.number) {
			return false;
		}
		if (size != other.size) {
			return false;
		}
		if (totalItemsCount != other.totalItemsCount) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Page [number=" + number + ", size=" + size + ", totalItemsCount=" + totalItemsCount + "]";
	}

}
