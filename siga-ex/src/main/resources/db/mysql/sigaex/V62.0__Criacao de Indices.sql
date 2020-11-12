CREATE INDEX iacs_ex_movimentacao_00001 ON
    siga.ex_movimentacao (
        id_mobil,
        id_tp_mov,
        id_mov_canceladora
    );

CREATE INDEX iacs_ex_mobil_00001 ON 
	siga.ex_mobil
             ( 
                          id_doc, 
                          id_mobil 
             );
