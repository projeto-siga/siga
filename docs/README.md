Manual do Siga
==============

Utilizamos o site ReadTheDocs.io para compilar automaticamente o manual do Siga-Doc a partir das informações contidas nesse diretório.

A última versão do manual pode ser acessada em HTML clicando [aqui](https://siga-doc.readthedocs.io/pt/latest/) ou em PDF clicando [aqui](https://buildmedia.readthedocs.org/media/pdf/siga-doc/latest/siga-doc.pdf).

##### Para compilar o manual do Siga é necessário:

1. Instalar a linguagem [Python](https://www.python.org/)
2. Instalar o Sphinx:
```
$ pip install sphinx sphinx-autobuild
```
3. Ir para o diretório "siga/docs"
4. Executar o comando
```
$ make html
```
5. Alternativamente, pode ser iniciado o sphinx-autobuild, com o comando:
```
$ sphinx-autobuild . _build/html
``` 
