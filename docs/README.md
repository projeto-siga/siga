Manual do Siga
==============

Para compilar o manual do Siga é necessário:

#. Instalar a linguagem [Python](https://www.python.org/)
#. Instalar o Sphinx: 
```
$ pip install sphinx sphinx-autobuild
```
#. Ir para o diretório "siga/docs"
#. Executar o comando
```
$ make html
```
#. Alternativamente, pode ser iniciado o sphinx-autobuild, com o comando:
```
$ sphinx-autobuild . _build/html
``` 
