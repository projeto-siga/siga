Manual do Siga
==============

Para compilar o manual do Siga é necessário:

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
