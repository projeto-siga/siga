-- -------------------------------------------------------------------------    
--  Remove as colunas de blobs
-- -------------------------------------------------------------------------


-- Primeiro precisamos mover os blobs das tabelas EX_MODELO, EX_DOCUMENTO, EX_MOVIMENTACAO e EX_PREENCHIMENTO para as tabelas CP_ARQUIVO e CP_ARQUIVO_BLOB

-- EX_MODELO
select 'processando ex_modelo';
drop procedure if exists tmpproc;
delimiter //
create procedure tmpproc ()
begin 
    declare l_rows_processed integer default 0; 
    declare l_chunk          integer default 1000; 
    declare id int 			 unsigned;
    declare tp_blob 		 varchar(128);
    declare conteudo_blob 	 blob;
    declare done int default false;
    declare c1 cursor for select id_mod, conteudo_tp_blob, conteudo_blob_mod from ex_modelo where id_arq is null and conteudo_blob_mod is not null;
    declare continue handler for not found set done = true;
    
    open c1;
    process_all:  loop 
        set l_rows_processed = 0; 

        read_loop: loop 
			fetch c1 into id, tp_blob, conteudo_blob;
            if done then
				select 'leaving read_loop';
                leave read_loop;
            end if;
            select id;

			-- incluir na cp_arquivo
            insert into corporativo.cp_arquivo 
                        (id_orgao_usu,conteudo_tp_arq, tamanho_arq) 
            values      (null, tp_blob, octet_length(conteudo_blob));

			-- incluir na cp_arquivo_blob
            insert into corporativo.cp_arquivo_blob
                        (id_arq_blob, conteudo_arq_blob) 
            values      (last_insert_id(), conteudo_blob);

            update ex_modelo set id_arq = last_insert_id(), conteudo_blob_mod = null where id_mod = id; 

            set l_rows_processed = l_rows_processed + 1; 
        end loop read_loop; 

        commit; 
		select concat( 'processed: ' , l_rows_processed);
        
        if done then
		    select 'exiting loop...';
            leave process_all;
        end if;
    end loop process_all; 
end  //

delimiter ;
call tmpproc();
drop procedure tmpproc;

-- EX_DOCUMENTO
select 'processando ex_documento';
drop procedure if exists tmpproc;
delimiter //
create procedure tmpproc ()
begin 
    declare l_rows_processed integer default 0; 
    declare l_chunk          integer default 1000; 
    declare id int 			 unsigned;
    declare tp_blob 		 varchar(128);
    declare conteudo_blob 	 blob;
    declare done int default false;
    declare c1 cursor for select id_doc, conteudo_tp_doc, conteudo_blob_doc from ex_documento where id_arq is null and conteudo_blob_doc is not null;
    declare continue handler for not found set done = true;
    
    open c1;
    process_all:  loop 
        set l_rows_processed = 0; 

        read_loop: loop 
			fetch c1 into id, tp_blob, conteudo_blob;
            if done then
				select 'leaving read_loop';
                leave read_loop;
            end if;
            select id;

			-- incluir na cp_arquivo
            insert into corporativo.cp_arquivo 
                        (id_orgao_usu,conteudo_tp_arq, tamanho_arq) 
            values      (null, tp_blob, octet_length(conteudo_blob));

			-- incluir na cp_arquivo_blob
            insert into corporativo.cp_arquivo_blob
                        (id_arq_blob, conteudo_arq_blob) 
            values      (last_insert_id(), conteudo_blob);

            update ex_documento set id_arq = last_insert_id(), conteudo_blob_doc = null where id_doc = id; 

            set l_rows_processed = l_rows_processed + 1; 
        end loop read_loop; 

        commit; 
		select concat( 'processed: ' , l_rows_processed);
        
        if done then
		    select 'exiting loop...';
            leave process_all;
        end if;
    end loop process_all; 
end  //

delimiter ;
call tmpproc();
drop procedure tmpproc;


-- EX_MOVIMENTACAO
select 'processando ex_movimentacao';
drop procedure if exists tmpproc;
delimiter //
create procedure tmpproc ()
begin 
    declare l_rows_processed integer default 0; 
    declare l_chunk          integer default 1000; 
    declare id int 			 unsigned;
    declare tp_blob 		 varchar(128);
    declare conteudo_blob 	 blob;
    declare done int default false;
    declare c1 cursor for select id_mov, conteudo_tp_mov, conteudo_blob_mov from ex_movimentacao where id_arq is null and conteudo_blob_mov is not null;
    declare continue handler for not found set done = true;
    
    open c1;
    process_all:  loop 
        set l_rows_processed = 0; 

        read_loop: loop 
			fetch c1 into id, tp_blob, conteudo_blob;
            if done then
				select 'leaving read_loop';
                leave read_loop;
            end if;
            select id;

			-- incluir na cp_arquivo
            insert into corporativo.cp_arquivo 
                        (id_orgao_usu,conteudo_tp_arq, tamanho_arq) 
            values      (null, tp_blob, octet_length(conteudo_blob));

			-- incluir na cp_arquivo_blob
            insert into corporativo.cp_arquivo_blob
                        (id_arq_blob, conteudo_arq_blob) 
            values      (last_insert_id(), conteudo_blob);

            update ex_movimentacao set id_arq = last_insert_id(), conteudo_blob_mov = null where id_mov = id; 

            set l_rows_processed = l_rows_processed + 1; 
        end loop read_loop; 

        commit; 
		select concat( 'processed: ' , l_rows_processed);
        
        if done then
		    select 'exiting loop...';
            leave process_all;
        end if;
    end loop process_all; 
end  //

delimiter ;
call tmpproc();
drop procedure tmpproc;


-- EX_PREENCHIMENTO
select 'processando ex_preenchimento';
drop procedure if exists tmpproc;
delimiter //
create procedure tmpproc ()
begin 
    declare l_rows_processed integer default 0; 
    declare l_chunk          integer default 1000; 
    declare id int 			 unsigned;
    declare tp_blob 		 varchar(128);
    declare conteudo_blob 	 blob;
    declare done int default false;
    declare c1 cursor for select id_preenchimento, preenchimento_blob from ex_preenchimento where id_arq is null and preenchimento_blob is not null;
    declare continue handler for not found set done = true;
    
    open c1;
    process_all:  loop 
        set l_rows_processed = 0; 

        read_loop: loop 
			fetch c1 into id, conteudo_blob;
            if done then
				select 'leaving read_loop';
                leave read_loop;
            end if;
            select id;

			-- incluir na cp_arquivo
            insert into corporativo.cp_arquivo 
                        (id_orgao_usu,conteudo_tp_arq, tamanho_arq) 
            values      (null, tp_blob, octet_length(conteudo_blob));

			-- incluir na cp_arquivo_blob
            insert into corporativo.cp_arquivo_blob
                        (id_arq_blob, conteudo_arq_blob) 
            values      (last_insert_id(), conteudo_blob);

            update ex_preenchimento set id_arq = last_insert_id(), preenchimento_blob = null where id_preenchimento = id; 

            set l_rows_processed = l_rows_processed + 1; 
        end loop read_loop; 

        commit; 
		select concat( 'processed: ' , l_rows_processed);
        
        if done then
		    select 'exiting loop...';
            leave process_all;
        end if;
    end loop process_all; 
end  //

delimiter ;
call tmpproc();
drop procedure tmpproc;

-- Depois, vamos confirmar que nao restou nenhum blob nas tabelas

DROP PROCEDURE IF EXISTS tmpproc;
DELIMITER //
CREATE PROCEDURE tmpproc ()
BEGIN
	IF (select count(*) from `siga`.`ex_documento` where CONTEUDO_BLOB_DOC is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_DOCUMENTO';
	END IF;
	IF (select count(*) from `siga`.`ex_movimentacao` where CONTEUDO_BLOB_MOV is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_MOVIMENTACAO';
	END IF;
	IF (select count(*) from `siga`.`ex_modelo` where CONTEUDO_BLOB_MOD is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_MODELO';
	END IF;
	IF (select count(*) from `siga`.`ex_preenchimento` where PREENCHIMENTO_BLOB is not null) > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Existem Blobs em EX_PREENCHIMENTO';
	END IF;
END  //
DELIMITER ;
call tmpproc();
DROP PROCEDURE tmpproc;

-- Por fim, vamos remover os campos de de tp_blob e blob

ALTER TABLE `siga`.`ex_documento` 
DROP COLUMN `CONTEUDO_TP_DOC`,
DROP COLUMN `CONTEUDO_BLOB_DOC`;

ALTER TABLE `siga`.`ex_movimentacao` 
DROP COLUMN `CONTEUDO_TP_MOV`,
DROP COLUMN `CONTEUDO_BLOB_MOV`;

ALTER TABLE `siga`.`ex_modelo` 
DROP COLUMN `CONTEUDO_TP_BLOB`,
DROP COLUMN `CONTEUDO_BLOB_MOD`;

ALTER TABLE `siga`.`ex_preenchimento` 
DROP COLUMN `PREENCHIMENTO_BLOB`;