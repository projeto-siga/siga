package br.gov.jfrj.siga.arquivo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadStatusRepository extends CrudRepository<UploadStatus, String> {}