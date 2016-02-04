
--Código agora considera que número da solicitação filha é igual ao da
--solicitação mãe vem vez de null
update sigasr.sr_solicitacao filha set num_solicitacao = (
  select num_solicitacao from sigasr.sr_solicitacao 
  where id_solicitacao = filha.id_solicitacao_pai
) where num_solicitacao is null and num_sequencia is not null;
commit;