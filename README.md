Siga - Sistema de Gestão Administrativa
=======================================

O Siga é composto de diversos módulos:

- Siga-Doc: Gestão Documental
- Siga-WF: Workflow
- Siga-GI: Gestão de Identidade
- Siga-GC: Gestão de Conhecimento
- Siga-SR: Serviços e Tickets
- Siga-tp: Solicitação de transportes

O módulo mas popular, o Siga-Doc, é capaz de controlar o trâmite de documentos digitais ou físico, processos administrativos ou expedientes, usando assinatura com certificado digital ou simplesmente assinatura com login e senha. Este módulo foi desenvolvido seguindo os requisitos do e-Arq, portanto, tem um elevado grau de aderência.

Para saber mais sobre o Siga-Doc, clique [aqui](https://github.com/projeto-siga/siga/wiki/Sobre-o-Siga-Doc).

Não deixe de conferir o novo [Manual do Usuário](https://sway.com/6tcLGC0jYE7zUSBX), uma contribuição do Governo do Estado de São Paulo!

Veja também a documentação para o desenvolvedor: [Javadoc](http://projeto-siga.github.io/artifacts/javadoc/).

Para instalar uma versão de testes do Siga-Doc, utilizando o Docker, clique [aqui](https://github.com/projeto-siga/docker).

Para tirar dúvidas, entre em contato através do [forum](https://groups.google.com/forum/#!forum/siga-doc).


# SIGADOC da Infraero

Instalação e atualização do sigadoc.

## Arquitetura da aplicação é composta pelos seguintes serviços:

- Aplicativo SIGADOC
- Bluc Server
- SMTP Inframail
- DB Oracle
- LoadBalancer F5 BigIP

## Requisitos

Para realizar as ações a seguir entende-se que os seguintes requisitos estão sendo cumpridos:

- Você possui um cluster Kubernetes, mais informações [aqui](http://kubernetes.io);
- Você possui o `kubectl` instalado e configurado para acessar o cluster Kubernetes;
- Você possui algum conhecimento sobre YAML.

## Instalação do SIGADOC

1. Iniciar o Bluc Server
2. Configurar o standalone.xml
3. Iniciar o SIGADOC

#### 1. Iniciar o Bluc Server

1. Inicie o bluc-server utilizando a imagem disponiblizada no repositório do Docker HUB:

    ````bash
    $  kubectl run bluc-server --image=siga/bluc.server --port=8080
    ````

2. Habilite acesso externo ao deploy do bluc-server:

    ````bash
    $  kubectl expose deployment bluc-server --type=NodePort --port=50010 --nodePort=31060 --target-port=8080
    ````

#### 2. Configurar o standalone.xml

1. Defina os parâmetros de ambiente no arquivos standalone.xml

    | Parameter               | Description                                     | Default                |
    | ----------------------- | ----------------------------------------------- | ---------------------- |
    | `servidor.smtp` | Endereço de servidor SMTP  | `inframail.infraero.gov.br`    |
    | `servidor.smtp.porta`| Descrição 2 | `25`|
    | `servidor.smtp.usuario.remetente`| Descrição 3 | `valor3`|
    | `siga.cabecalho.titulo`| Descrição 4 | `INFRAERO`|
    | `siga.cp.sinc.xml.servidor.usuario.remetente`| Descrição 5 | `valor5`|
    | `parametro3`| Descrição 3 | `valor3`|
    | `parametro4`| Descrição 4 | `valor4`|
    | `parametro5`| Descrição 5 | `valor5`|

2. Carregue o standalone.xml como um ConfigMap sob o nome sigadoc-app-config-prd

    ````bash
    $  kubectl create configmap sigadoc-app-config-prd --from-file=./standalone.xml
    ````

#### 3. Iniciar o SIGADOC

1. Inicie o sigadoc-app utilizando a imagem disponiblizada no repositório da Infraero:

    ````bash
    $ kubectl apply -f sigadoc-deployment.yaml
    ````

2. Habilite acesso externo ao deploy do sigadoc-app:

    ````bash
    $ kubectl apply -f sigadoc-service.yaml
    ````
