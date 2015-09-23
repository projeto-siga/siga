Projeto Siga
============
## Versões

 * Página com o histórico de [versões](https://github.com/projeto-siga/siga/wiki/Vers%C3%B5esSIGA) do siga
 * Rode o [siga no docker](https://github.com/projeto-siga/docker)
 
## Documentação

 * [Javadoc](http://projeto-siga.github.io/artifacts/javadoc/)
 * [Wiki](https://github.com/projeto-siga/siga/wiki)

## Dependências do Projeto
* [Git](https://windows.github.com/)
* Maven ftp://mirror.reverse.net/pub/apache/maven/maven-3/3.2.3/binaries/apache-maven-3.2.3-bin.zip
* [Java JDK 1.7](http://download.oracle.com/otn-pub/java/jdk/7u67-b01/jdk-7u67-windows-x64.exe)
* [JBoss EAP 6.3](http://www.redhat.com/j/elqNow/elqRedir.htm?ref=https://www.jboss.org/download-manager/content/origin/files/sha256/62/627773f1798623eb599bbf7d39567f60941a706dc971c17f5232ffad028bc6f4/jboss-eap-6.2.0.zip)  
* [Play! Framework 1.3.x]

### Variáveis de ambiente
* **MAVEN_HOME**=C:\Desenvolvimento\apache-maven-3.2.1  
* **JAVA_HOME**=C:\Program Files\Java\jdk1.7  
* **JBOSS_HOME**=C:\Desenvolvimento\jboss-eap-6.2
* O trecho abaixo deverá ser **ACRESCENTADO** no final da variável **PATH**:
* **PATH**=;%JAVA_HOME%\bin;%MAVEN_HOME%\bin;

#### Testando variáveis de ambiente
Abra um ***novo*** prompt de comando do Windows (cmd) e execute:
```
java -version
mvn -version
```

Ambos deverão mostrar a versão dos respectivos programas, caso mostre erro, algum desses passos foi configurado incorretamente.

## Deploy
Para gerar o pacote para deploy do siga, é necessário entrar no diretório projeto-siga e executa a task package do maven.  
PS: (A primeira vez isso pode demorar bastante).
```
   mvn clean package
```

## Versionamento
Nomenclatura de versionamento:
a.b.c.d

* a = grandes alterações (Mudança do EAP 5 para o 6 por exemplo)
* b = alterações médias (esquema de banco, propriedades obrigatórias, etc.). Versão com essa alteração é incompatível com a anterior do mesmo nível
* c = pequenas alterações ou correções
* d= hotfix

1) Ao iniciar um trabalho, crie um branch baseado no develop (ou outro se for o caso)
```
git fetch
git checkout develop
git pull delvelop
git checkout –b meuProblema
```

2) Trabalhe e faça seus commits em seu branch
```
(git add, cherry-pick, etc.)
git commit –m “meu commit”
```

3) Faça uma atualização do branch original

```
git checkout develop
git pull develop
```

4) Faça um rebase do develop no branch de trabalho

```
git checkout meuProblema
git rebase develop
```

5) Ao concluir o trabalho faça o merge do seu branch com o original

```
git checkout develop
git merge meuProblema
```

6) Exclua seu branch e faça um push

```
git branch –d meuProblema
git push origin develop
```

Todos os deploys em produção devem partir do ** branch master ** com sua respectiva tag (Obedecendo a nomenclatura acima).

Qualquer alteração em uma tag gerada previamente é denominada como **hotfix**.

## JBoss
### Configuração
* Deploy do drive oracle JDBC. Copiar o arquivo ***projeto-siga/configuracao/ojdbc6.jar*** para ***%JBOSS_HOME%/standalone/deployments/***
* Substituir o arquivo ***%JBOSS_HOME%/standalone/configuracao/standalone.xml*** pelo arquivo ***projeto-siga/configuration/standalone.xml***.
* Colocar o arquivo ***projeto-siga/configuracao/siga.properties*** em ***%JBOSS_HOME%/standalone/configuration/***

## Play
* JBoss -> Criar o módulo do Play! Framework, copiando a pasta ***projeto-siga/configuracao/Play/modulo/sigadoc*** em ***%JBOSS_HOME%/modules/***  
* Maven -> Instalar a biblioteca no repositório local do maven executando o arquivo ***projeto-siga/configuracao/Play/maven/configure.bat***

## Fonte do Play
### Compilação
Na pasta framework:
```
 executar ant package
```

### Administração
[Interface de administração](http://localhost:9990)
usuário: admin
password: admin@123

* Start:
Dentro do diretório ***%JBOSS_HOME%/bin executar:
```
standalone.bat
```

* Stop:
```
Ctrl + C
```

## Ferramentas utilizadas no projeto SIGA

Nós utilizamos o ![](http://structure101.com/images/s101_170.png "Structure 101") (http://structure101.com) para organizar a arquitetura do projeto.
