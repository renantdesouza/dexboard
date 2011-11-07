package br.com.dextra.dexboard;

import java.io.Serializable;

public class CacheEntry implements Serializable {

	private static final long serialVersionUID = 1303989920804270792L;

	private final String json;

	private final Long lastModified;

	public CacheEntry(String json, Long lastModified) {
		this.json = json;
		this.lastModified = lastModified;
	}

	public String getJson() {
		return json;
	}

	public Long getLastModified() {
		return lastModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((json == null) ? 0 : json.hashCode());
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheEntry other = (CacheEntry) obj;
		if (json == null) {
			if (other.json != null)
				return false;
		} else if (!json.equals(other.json))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CacheEntry [json=" + json + ", lastModified=" + lastModified + "]";
	}

}
