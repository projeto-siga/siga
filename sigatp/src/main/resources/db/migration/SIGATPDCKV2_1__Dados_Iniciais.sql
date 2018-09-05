-- cores
Insert into COR (ID,NOME) values (1,'AMARELA');
Insert into COR (ID,NOME) values (2,'AZUL');
Insert into COR (ID,NOME) values (3,'BEGE');
Insert into COR (ID,NOME) values (4,'BRANCA');
Insert into COR (ID,NOME) values (5,'CINZA');
Insert into COR (ID,NOME) values (6,'DOURADA');
Insert into COR (ID,NOME) values (7,'GRENA');
Insert into COR (ID,NOME) values (8,'LARANJA');
Insert into COR (ID,NOME) values (9,'MARROM');
Insert into COR (ID,NOME) values (10,'PRATA');
Insert into COR (ID,NOME) values (11,'PRETA');
Insert into COR (ID,NOME) values (12,'ROSA');
Insert into COR (ID,NOME) values (13,'ROXA');
Insert into COR (ID,NOME) values (14,'VERDE');
Insert into COR (ID,NOME) values (15,'VERMELHA');
Insert into COR (ID,NOME) values (16,'FANTASIA');

-- grupos de veiculos

Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (1,'VEÍCULOS DE MÉDIO PORTE, TIPO SEDAN, COR PRETA, COM  
CAPACIDADE DE TRANSPORTE DE ATÉ 5 (CINCO) PASSAGEIROS, MOTOR DE POTÊNCIA MÍNIMA  
DE 116 CV E MÁXIMA DE 159 CV (GASOLINA) E ITENS DE SEGURANÇA CONDIZENTES COM  
O SERVIÇO','TRANSPORTE DOS PRESIDENTES, DOS VICE-PRESIDENTES E DOS  
CORREGEDORES DOS TRIBUNAIS REGIONAIS FEDERAIS','A','REPRESENTAÇÃO');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (2,'VEÍCULOS DE MÉDIO PORTE, TIPO SEDAN, COR PRETA, COM
CAPACIDADE DE TRANSPORTE DE ATÉ 5 (CINCO) PASSAGEIROS, MOTOR DE POTÊNCIA MÍNIMA
DE 116 CV E MÁXIMA DE 159 CV (GASOLINA) E ITENS DE SEGURANÇA CONDIZENTES COM
O SERVIÇO','TRANSPORTE, EM OBJETO DE SERVIÇO, DOS JUÍZES DE SEGUNDO GRAU E
DOS JUÍZES DIRETORES DE FORO E DIRETORES DE SUBSEÇÕES JUDICIÁRIAS','B','TRANSPORTE INSTITUCIONAL');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (3,'VEÍCULOS DE PEQUENO PORTE, COM CAPACIDADE DE ATÉ 5 (CINCO)
OCUPANTES, MOTOR COM POTÊNCIA MÍNIMA DE 80 CV E MÁXIMA DE 112 CV (GASOLINA)
E ITENS DE SEGURANÇA CONDIZENTES COM O SERVIÇO','TRANSPORTE, EM OBJETO DE SERVIÇO, DE JUÍZES DE PRIMEIRO GRAU E
SERVIDORES NO DESEMPENHO DE ATIVIDADES EXTERNAS DE INTERESSE DA ADMINISTRAÇÃO','C','SERVIÇO COMUM');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (4,'PICK-UPS CABINE DUPLA, VANS COM CAPACIDADE MÍNIMA DE 12  
(DOZE) OCUPANTES, MICROÔNIBUS E ÔNIBUS, MOTOR COM POTÊNCIA CONDIZENTE COM O  
SERVIÇO','TRANSPORTE, EM OBJETO DE SERVIÇO, DE MAGISTRADOS E SERVIDORES NO','D','TRANSPORTE COLETIVO');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (5,'FURGÕES, PICK-UPS DE CABINE SIMPLES, REBOQUES E SEMIREBOQUES,  
MOTOR DE POTÊNCIA CONDIZENTE COM O SERVIÇO','TRANSPORTE DE CARGAS LEVES NO DESEMPENHO DE ATIVIDADES EXTERNAS  
DE INTERESSE DA ADMINISTRAÇÃO','E','CARGA LEVE');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (6,'VEÍCULOS TIPO CAMINHÃO, MOTOR DE POTÊNCIA CONDIZENTE COM O','TRANSPORTE DE CARGAS PESADAS','F','CARGA PESADA');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (7,'AMBULÂNCIAS E VEÍCULOS COM DISPOSITIVO DE ALARME E LUZ','ATENDIMENTO, EM CARÁTER DE SOCORRO MÉDICO OU DE APOIO ÀS','G','APOIO ESPECIAL');
Insert into GRUPOVEICULO (ID,CARACTERISTICAS,FINALIDADE,LETRA,NOME) values (8,'TRANSPORTE DO TIPO BLINDADO','ATENDIMENTO DE AUTORIDADES','H','BLINDADO');

-- finalidade 'OUTRA'

insert into SIGATP.FinalidadeRequisicao (id, descricao, id_orgao_ori) values (-1, 'OUTRA', 9999999999);

-- parametro: mostrar tipo de passageiro 'CARGA'?

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) values (1, 'mostrarTipoPassageiroCarga', 'true', current_timestamp, 'Mostrar ou nao o tipo de passageiro "CARGA". Possivel bloquear (ou liberar) para um orgao especifico usando o campo id_orgao_usu');

-- parametros: envio automatico de e-mail e tamanho limite de imagem para upload

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (10, 'cron.executa', 'false', current_timestamp, 'EmailNotificacoes : Executar ou nao o cron que envia emails');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (11, 'cron.enviarParaLista', 'false', current_timestamp, 'EmailNotificacoes : Enviar email para a lista (caso "false": enviar para o usuario)');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (12, 'cron.listaEmail', 'administrador@sigatp.teste.docker', current_timestamp, 'EmailNotificacoes: Lista de emails a enviar');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (13, 'cron.inicio', '0 0 9 ? 1/1 2#1 *', current_timestamp, 'EmailNotificacoes: Configuracoes de inicializacao do servico.');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (14, 'cron.executa.notificarMissoesProgramadas', 'false', current_timestamp, 'Enviar ou nao e-mails aos condutores reclamando das missoes programadas e nao iniciadas.');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (20, 'cron.executaw', 'false', current_timestamp, 'Workflow: Executar ou nao o cron que envia emails');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (21, 'cron.enviarParaListaW', 'false', current_timestamp, 'Workflow: Enviar email para a lista (caso "false": enviar para o usuario)');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (22, 'cron.listaEmailw', 'administrador@sigatp.teste.docker', current_timestamp, 'Workflow: Lista de emails a enviar');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (23, 'cron.iniciow', '0 0/10 9-19 1/1 * ? *', current_timestamp, 'Workflow: Configuracoes de inicializacao do servico.');

insert into SIGATP.PARAMETRO (ID, NOMEPARAMETRO, VALORPARAMETRO, DATAINICIO, DESCRICAO) 
VALUES (24, 'cron.dataInicioPesquisaw', '01/04/2014', current_timestamp, 'Workflow: Data de inicio das requisicoes transporte para notificacao');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (25, 'caminhoHostnameStandalone', 'sigaidp.crossdomain.url', current_timestamp, 'Workflow: nome do parametro no arquivo standalone.xml que contem o hostname do servidor');

insert into sigatp.parametro (id, nomeparametro, valorparametro, datainicio, descricao) 
values (30, 'imagem.filesize', '1', current_timestamp, 'Tamanho maximo em MB da imagem para upload');

insert into sigatp.parametro(id, nomeparametro, valorparametro, datainicio, descricao) 
values (40, 'mostrarTipoFinalidadeOutra', 'true', current_timestamp, 'Mostrar ou nao o tipo de finalidade OUTRA e permitir detalhar finalidade. Possivel bloquear (ou liberar) para um orgao especifico usando o campo id_orgao_usu');

insert into sigatp.parametro(id, nomeparametro, valorparametro, datainicio, descricao) 
values (50, 'cron.nodeQueExecutaEnvioEmail', 'coloque_aqui_o_node_name_que_envia_email', current_timestamp, 'Conteudo do parametro jboss.node.name do node que sera responsavel por enviar os e-mails das tarefas agendadas.');

