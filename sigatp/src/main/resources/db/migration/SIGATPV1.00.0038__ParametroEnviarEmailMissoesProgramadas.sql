insert into sigatp.parametro 
	(id, nomeparametro, valorparametro, datainicio, descricao) 
values 
	(14, 'cron.executa.notificarMissoesProgramadas', 'false', current_timestamp, 'Enviar ou nao e-mails aos condutores reclamando das missoes programadas e nao iniciadas.');