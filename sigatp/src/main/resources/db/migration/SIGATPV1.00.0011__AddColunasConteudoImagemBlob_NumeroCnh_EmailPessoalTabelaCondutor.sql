ALTER TABLE 
   Condutor
ADD 
   (
      emailpessoal VARCHAR2(255 char),
      conteudoimagemblob BLOB default EMPTY_BLOB(),
      numerocnh VARCHAR2(11 char),
      endereco VARCHAR2(255 char)
   );

ALTER TABLE 
   Condutor_AUD
ADD 
   (
      emailpessoal VARCHAR2(255 char),
      conteudoimagemblob BLOB default EMPTY_BLOB(),
      numerocnh VARCHAR2(11 char),
      endereco VARCHAR2(255 char)
   );  