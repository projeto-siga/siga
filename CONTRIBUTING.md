Siga - Sistema de Gestão Administrativa
=======================================

Este roteiro tem como objetivo explicar como deve ser realiadas as contribuições para o projeto-siga


* Desenvolvimento
	Pegar o branch develop, fazer um branch "feature/[órgão]/[descrição]".
	Fazer o pull-request deste branch para o "develop" e aguardar a reunião de merge.

	Correções no Desenvolvimento
	Quando é uma correção, deve commitar direto no "develop".

* Homologação
	Após a reunião de merge(semanal) será criado um novo branch "release/10.x.y" a partir do "develop".
	Se o órgão que quiser homologar esta versão, esse branch vai para homologação com um número de versão gerado pela concatenação de 10.x.y e o número com 6 posições do último commit, exemplo: "10.x.y-1234567".

	Correções na Homologação
	Correções serão commitadas diretamente no brach "release/10.x.y".
	Depois deve ser realizado o merge do "release/10.x.y" no "develop" e em qualquer outra release posterior.

* Produção
	Antes de colocar uma nova release em produção, será registrado o Release do GitHub, que ganhará uma tag do tipo 10.x.(y-1) e conterá a última versão estabilizada no master.
	Depois, será feito o merge do tipo "fast-forward" do branch "release/10.x.y" no "master" (sempre de uma release posterior a que já está no "master").
	E será feito o deploy em produção.

	Correções em Produção
	Correções serão commitadas diretamente no branch "master", sempre prefixando o commit com "HOTFIX:".
	Depois que é feito um hotfix, deve ser realizado o merge do "master" no "develop" e em qualquer outra release posterior.

* Observações
	Branches de "release" e "master" só podem receber correções!
	Sempre a pessoa que faz o commit de correção na master ou em uma release é responsável por fazer os merges em develop, ou nas releases posteriores
	SP vai trabalhar com uma master própria, chamada "master-govsp"
	Limitar commits na "master" para TRF2 e na "master-govsp" para Prodesp	
