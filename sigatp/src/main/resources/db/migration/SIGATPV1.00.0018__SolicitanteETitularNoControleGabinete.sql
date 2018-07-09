alter table SIGATP.ControleGabinete rename column responsavel_id_pessoa TO ID_SOLICITANTE;
alter table SIGATP.ControleGabinete_AUD rename column responsavel_id_pessoa TO ID_SOLICITANTE;

alter table SIGATP.ControleGabinete add ID_TITULAR number(19,0);
alter table SIGATP.ControleGabinete_AUD add ID_TITULAR number(19,0);
