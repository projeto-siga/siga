alter table sigatp.requisicaotransporte add ( NUMERODEPASSAGEIROS number(5,0) );

alter table sigatp.requisicaotransporte_AUD add ( NUMERODEPASSAGEIROS number(5,0) );

alter table sigatp.requisicaotransporte add ( origemExterna number(1,0) default 0 not null );

alter table sigatp.requisicaotransporte_AUD add ( origemExterna number(1,0) );
