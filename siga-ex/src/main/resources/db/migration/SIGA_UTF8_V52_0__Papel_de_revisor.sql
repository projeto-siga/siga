-- Inclui dois papéis novos para utilziação no modelo - eof pagamento (https://github.com/projeto-siga/siga/issues/1210)
insert into SIGA.EX_PAPEL (ID_PAPEL,DESC_PAPEL) values ('7','Revisor');
insert into CORPORATIVO.CP_MARCADOR values(72, 'Como Revisor', 2, 25);