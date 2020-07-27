CREATE INDEX siga.iacs_ex_movimentacao_00001 ON
    siga.ex_movimentacao (
        id_mobil,
        id_tp_mov,
        id_mov_canceladora
    )
        PCTFREE 10 INITRANS 20 MAXTRANS 255 COMPUTE STATISTICS COMPRESS 2 NOLOGGING 
            PARALLEL 16;

ALTER INDEX siga.iacs_ex_movimentacao_00001 PARALLEL ( DEGREE 1);
CREATE INDEX siga.iacs_ex_mobil_00001 ON 
	siga.ex_mobil
             ( 
                          id_doc, 
                          id_mobil 
             ) 
         pctfree 10 INITRANS 20 MAXTRANS 255 compute STATISTICS COMPRESS 2 nologging 
             PARALLEL 16;
ALTER INDEX siga.iacs_ex_movimentacao_00001 PARALLEL ( degree 1 );
