create or replace view siga.vw_documentos_int_sem_sicop
as
select 
SUBSTR(siga.ex_documento.descr_documento_ai,0,30) DESCRICAO,
siga.ex_documento.dt_primeiraassinatura,
siga.ex_documento.id_orgao_usu,
siga.ex_documento.NUM_EXT_DOC,
siga.ex_documento.ANO_EMISSAO,
siga.ex_documento.ID_DOC,
siga.ex_documento.NUM_EXPEDIENTE,
corporativo.cp_orgao_usuario.sigla_orgao_usu,
corporativo.cp_orgao_usuario.sigla_orgao_usu||'-'||'INT'||'-2022/'||siga.ex_documento.NUM_EXPEDIENTE SIGLA_EXPEDIENTE
from 
    siga.ex_documento,
    siga.ex_modelo,
    corporativo.cp_orgao_usuario
where 
    corporativo.cp_orgao_usuario.id_orgao_usu=siga.ex_documento.id_orgao_usu
    AND siga.ex_documento.ID_MOD=siga.ex_modelo.ID_MOD
    AND siga.ex_documento.ano_emissao='2022' 
    AND siga.ex_documento.ID_FORMA_DOC=380
    AND siga.ex_modelo.DESC_MOD='Gerao de nmero de processo para o FINCON' 
    AND siga.ex_modelo.his_dt_fim is null
    AND siga.ex_documento.NUM_EXT_DOC NOT IN(
            SELECT
                DISTINCT '99'||i22002_seq_nproc2||'2022' PROCESSO_SICOP
            FROM
               DB022.dc22002@DBLK_SIGADOC_CP01DB022.PCRJ
            WHERE
                i22002_ano_proc4='2022'
                AND i22002_ra_nproc2='99'
                and i22002_cod_assu_p=05990
                and i22002_org_transc='99000000'
                and i22002_org_origem='99000000'
                and i22002_req_parte='PROCESSORIO-INTEGRACAO'
                and i22002_mat_receb='99999911'
                and i22002_mat_transc='99999911'    
    )
ORDER BY 
    siga.ex_documento.ID_DOC;
