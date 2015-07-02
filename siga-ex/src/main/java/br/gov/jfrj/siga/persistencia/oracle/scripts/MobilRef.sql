/*
 * Script que :
 * 1) Copia todos os ExMobilPai das movimentações dos tipos JUNTADA e 
 * CANCELAMENTO_DE_JUNTADA para ExMobilRef
 * 
 * 2) Copia todos os ExMobilMestre das movimentações dos tipos APENSACAO
 * e DESAPENSACAO para ExMobilRef
 * 
 * 3) Elimina os campos ExMobilPai e ExMobilMestre.
 * */


DECLARE

 CURSOR MOV_JUNTADA IS
    SELECT mov.* FROM ex_movimentacao mov,
    (SELECT tpMov.* FROM ex_tipo_movimentacao tpMov WHERE descr_tipo_movimentacao = 'Juntada' OR descr_tipo_movimentacao = 'Desentranhamento') tpMov
    WHERE mov.id_tp_mov = tpMov.id_tp_mov AND mov.id_mob_pai IS NOT NULL;

 CURSOR MOV_APENSACAO IS
    SELECT mov.* FROM ex_movimentacao mov,
    (SELECT tpMov.* FROM ex_tipo_movimentacao tpMov WHERE descr_tipo_movimentacao = 'Apensação' OR descr_tipo_movimentacao = 'Desapensação') tpMov
    WHERE mov.id_tp_mov = tpMov.id_tp_mov AND mov.id_mob_mestre IS not NULL;

BEGIN

  FOR MOV_J IN MOV_JUNTADA LOOP
    UPDATE EX_MOVIMENTACAO MOV SET MOV.ID_MOB_REF=MOV_J.ID_MOB_PAI WHERE MOV.ID_MOV=MOV_J.ID_MOV;
    UPDATE EX_MOVIMENTACAO MOV SET MOV.ID_MOB_PAI=NULL WHERE MOV.ID_MOV=MOV_J.ID_MOV;
  END LOOP;

  FOR MOV_AP IN MOV_APENSACAO LOOP
    UPDATE EX_MOVIMENTACAO MOV SET MOV.ID_MOB_REF=MOV_AP.ID_MOB_MESTRE WHERE MOV.ID_MOV=MOV_AP.ID_MOV;
    UPDATE EX_MOVIMENTACAO MOV SET MOV.ID_MOB_MESTRE=NULL WHERE MOV.ID_MOV=MOV_AP.ID_MOV;
  END LOOP;

  COMMIT;

/*
ALTER TABLE EX_MOVIMENTACAO DROP COLUMN ID_MOB_PAI;
ALTER TABLE EX_MOVIMENTACAO DROP COLUMN ID_MOB_MESTRE;
*/

END;
