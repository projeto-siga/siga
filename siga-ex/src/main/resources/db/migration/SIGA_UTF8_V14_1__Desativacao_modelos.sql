-------------------------------------------------------------
-- MANTEM O MINIMO DE MODELOS APENAS PARA SERVIR DE EXEMPLO--
-------------------------------------------------------------

--SET DEFINE OFF;
update siga.ex_modelo set his_ativo = 0, his_dt_fim = sysdate where NM_MOD not in ('Ofício',
'Despacho',
'Memorando',
'Informação',
'Documento Externo',
'Remessa de Documentos Físicos',
'Texto Livre',
'Parecer',
'Processo de Pessoal',
'SGP: Licença Médica',
'SGP: Comunicações de Alterações de Frequência',
'Certidão',
'Processo de Execução Orçamentária e Financeira',
'Contratos: Atesto de Serviços com Frequência',
'Declaração - Modelo livre',
'Portaria',
'SGP: Marcação ou Alteração de férias fora do prazo pela chefia',
'SGP: Férias (servidor p/ superior hierárquico)',
'Carta',
'Atesto',
'Termo - Modelo Livre',
'Interno Importado',
'Certidão de encerramendo de volume',
'Certidão de desentranhamento',
'Solicitação Eletrônica de Contratação',
'Boletim Interno',
'Despacho Automático');

update siga.ex_modelo set conteudo_tp_blob = 'template/freemarker' where  his_ativo = 1 and nm_mod in(
'Boletim Interno','Termo - Modelo Livre','Carta','Certidão de desentranhamento'
);

DECLARE
  dest_blob_ex_mod BLOB;
  src_blob_ex_mod BLOB;
  
BEGIN

update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 241;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 241 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
ELABORE SEU MODELO DE BOLETIM INTERNO NO MENU FERRAMENTAS/CADASTRO DE MODELOS
','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);


update SIGA.EX_MODELO set conteudo_blob_mod = utl_raw.cast_to_raw(' ') where id_mod = 529;
select conteudo_blob_mod into dest_blob_ex_mod from SIGA.EX_MODELO where id_mod = 529 for update;
src_blob_ex_mod := utl_raw.cast_to_raw(convert('
Certidão de desentranhamento
','AL32UTF8'));
dbms_lob.append(dest_blob_ex_mod, src_blob_ex_mod);



END;
/