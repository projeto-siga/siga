package br.gov.jfrj.siga.arquivo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("UploadStatus")
public class UploadStatus implements Serializable {
	private static final long serialVersionUID = -4325911468021962481L;

	@Id
	private String id;
    private Long bytesTransferidos;

    public String getId() {
		return id;
	}
	
    public void setId(String id) {
		this.id = id;
	}

    public Long getBytesTransferidos() {
		return bytesTransferidos;
	}

	public void setBytesTransferidos(Long bytesTransferidos) {
		this.bytesTransferidos = bytesTransferidos;
	}
}