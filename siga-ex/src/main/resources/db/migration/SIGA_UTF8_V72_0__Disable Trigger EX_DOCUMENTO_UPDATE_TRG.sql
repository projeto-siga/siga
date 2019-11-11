--------------------------------------------------------
--  DESABILITA CONTROLE DE NUMERACAO NO BANCO DE DADOS
--------------------------------------------------------	          
alter trigger "SIGA"."EX_DOCUMENTO_UPDATE_TRG" disable;

create or replace TRIGGER "SIGA"."EX_DOCUMENTO_INSERT_TRG" BEFORE INSERT ON "EX_DOCUMENTO" FOR EACH ROW 
begin
    if :new.ID_DOC is null then
        select EX_DOCUMENTO_SEQ.nextval into :new.ID_DOC from dual;
    end if;


   /* if (  (:new.ID_ORGAO_USU is not null) and (:new.ID_FORMA_DOC is not null) and (:new.ANO_EMISSAO is not null) ) then
        DECLARE
          PID_ORGAO_USU NUMBER;
          PID_FORMA_DOC NUMBER;
          PANO_EMISSAO NUMBER;
          v_Return NUMBER;
        BEGIN
          PID_ORGAO_USU := :new.ID_ORGAO_USU;
          PID_FORMA_DOC := :new.ID_FORMA_DOC;
          PANO_EMISSAO := :new.ANO_EMISSAO;

          v_Return := FUN_NUMERACAO_DOCUMENTO(
                        PID_ORGAO_USU => PID_ORGAO_USU,
                        PID_FORMA_DOC => PID_FORMA_DOC,
                        PANO_EMISSAO => PANO_EMISSAO
                      );

          :new.NUM_EXPEDIENTE := v_Return;

        END;
    end if;*/
end;
/