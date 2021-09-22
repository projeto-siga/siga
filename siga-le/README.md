# Siga-Le

Siga-Le (siga leve) é um site que permite acessar documentos do Siga-Doc em celulares e outros dispositivos móveis. O Siga-Le não possui todas as funcionalidades do Siga-Doc, apenas as mais comuns para o usuário mobile.

As operações disponíveis são:
- Consulta;
- Assinatura com Senha;
- Trâmite.

## Arquitetura

Completamente baseado em micro-serviços, o Siga-Le é composto dos seguintes componentes:
- Site do Siga-Le, desenvolvido em VueJS e JavaScript;
- Webservice REST para obter informações dos documentos do Siga-Doc e realizar algumas operações, desenvolvido em Java utilizando [SwaggerServlet](https://github.com/crivano/swaggerservlet);
- Componente de modelo e regras de negócio do Siga-Doc, o [siga-ex.jar](https://github.com/projeto-siga/siga/tree/master/siga-ex).

Como qualquer API desenvolvida com o SwaggerServlets, o Siga-Le possui documentação de seus webservices e URL de teste para monitoramento. Em nosso ambiente de homologação, que é público, essas URLs são:
- Documentação da API: http://sigat.jfrj.jus.br/siga-le/api/v1/swagger-ui
- Monitoramento da API: http://sigat.jfrj.jus.br/siga-le/api/v1/test

## Ambiente

Para executar o Siga-Le, é necessário que algumas propriedades sejam definidas.

Utiliza JWT como mecanismo de autenticação e autorização, portanto é necessário informar uma senha para assinar o token JWT. Sugerimos que a senha senha um GUID aleatório:

```xml
<property name="siga.ex.api.jwt.secret" value="***GUID***"/>
 ```
 
Precisa que algumas propriedades sejam configuradas para que possa enviar o email de sugestão:

```xml
<property name="siga.ex.api.smtp.remetente" value="balcaovirtual@trf2.jus.br"/>
<property name="siga.ex.api.smtp.host" value="smtp.trf2.jus.br"/>
<property name="siga.ex.api.smtp.host.alt" value="smtp2.trf2.jus.br"/>
<property name="siga.ex.api.smtp.auth" value="true"/>
<property name="siga.ex.api.smtp.auth.usuario" value="intelijus"/>
<property name="siga.ex.api.smtp.auth.senha" value="senha_secreta"/>
<property name="siga.ex.api.smtp.porta" value="25"/>
<property name="siga.ex.api.smtp.destinatario" value="equipe_responsavel@trf2.jus.br"/>
<property name="siga.ex.api.smtp.assunto" value="Siga-Le: Sugestão"/>
```

A barra de títulos do Siga-Le assume cores diferenciadas nos ambientes de produção, homologação e desenvolvimento. A configuração abaixo deve ser utilizada no ambiente de produção. Em homologação usar "homolo" e em desenvolvimento, "desenv".

```xml
<property name="siga.ex.api.env" value="prod"/>
 ```
