-- // criar tabela sigapp.usuario

 create table sigapmp.administradores (sesb_pessoa char(2) NOT NULL, matric_adm varchar2(6) NOT NULL, nome_adm varchar2(50) default '' , constraint administradorespk primary key(sesb_pessoa,matric_adm));

 COMMENT ON COLUMN sigapmp.administradores.sesb_pessoa IS 'Exemplo: T2,RJ ou ES';

 COMMENT ON COLUMN sigapmp.administradores.matric_adm IS 'Matricula do servidor; funcionario do primeiro atendimento';

 COMMENT ON COLUMN sigapmp.administradores.nome_adm IS 'Nome do servidor;funcionario do primeiro atendimento ';

 comment on table sigapmp.administradores is 'Administradores. Lista de quem pode dar permissão ou tirar';
