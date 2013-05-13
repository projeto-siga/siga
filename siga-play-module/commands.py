import sys
import os
import getopt
import shutil

import play.commands.precompile
from play.utils import *

# Here you can create play commands that are specific to the module, and extend existing commands

MODULE = 'siga-play-module'

# Commands that are specific to your module

COMMANDS = ['siga-play-module:sigawar']

def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

# This will be executed before any command (new, run...)
def before(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")
    print "siga-play-module before"
    if command == "war":
        enhance_siga_play_module(app=app)


# This will be executed after any command (new, run...)
def after(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")
    print "siga-play-module after"
    if command == "war":
        diminish_siga_play_module(app=app)
        war_path = None
        optlist, args = getopt.getopt(args, 'o:', ['output=', 'zip','exclude='])
        for o, a in optlist:
            if o in ('-o', '--output'):
                war_path = os.path.normpath(os.path.abspath(a))
        delete_jta(war_path);
        move_servlet(war_path);
    if command == "new":
        pass

def enhance_siga_play_module(app):
    app.check()
    modules = app.modules()
    # modules
    for module in modules:
        if os.path.basename(module) == "siga-play-module":
            print "~ Enhancing siga-play-module %s ..." % (module)
            
            src = os.path.join(module, 'bin')
            dest = os.path.join(module, 'lib')
            jar_zip_path = os.path.join(dest, 'siga-play-module.jar')
            #copy_directory(src, dest)
        	
            print "~ Creating jar archive to %s ..." % (os.path.normpath(jar_zip_path))
            if os.path.exists(jar_zip_path):
                os.remove(jar_zip_path)
            zip = zipfile.ZipFile(jar_zip_path, 'w', zipfile.ZIP_STORED)
            for (dirpath, dirnames, filenames) in os.walk(src):
                if dirpath.find('br\gov\jfrj') == -1:
                    continue
                if dirpath.find('/.') > -1:
                    continue
                for file in filenames:
                    if dirpath.endswith('br\gov\jfrj\siga\cp') or dirpath.endswith('br\gov\jfrj\siga\dp'):
                        continue
                    if file.find('~') > -1 or file.startswith('.'):
                        continue
                    zip.write(os.path.join(dirpath, file), os.path.join(dirpath[len(src):], file))
            zip.close()

#            #src = os.path.join(module, '../../siga-cp/src')
#            src = "c:\Trabalhos\Repositorios\_git_google\siga-cp\src"
#            dest = os.path.join(module, 'app')
#            jar_zip_path = os.path.join(dest, 'siga-play-module.jar')
#            print "~ Copying corporative java files to app folder ..."
#            app_directory(src, dest, 1)

def diminish_siga_play_module(app):
    app.check()
    modules = app.modules()
    # modules
    for module in modules:
        if os.path.basename(module) == "siga-play-module":
            print "~ Diminishing siga-play-module %s ..." % (module)
            src = os.path.join(module, 'bin')
            dest = os.path.join(module, 'lib')
            jar_zip_path = os.path.join(dest, 'siga-play-module.jar')
            #copy_directory(src, dest)
        	
            print "~ Deleting jar archive at %s ..." % (os.path.normpath(jar_zip_path))
            if os.path.exists(jar_zip_path):
                os.remove(jar_zip_path)
                
#            src = "c:\Trabalhos\Repositorios\_git_google\siga-cp\src"
#            dest = os.path.join(module, 'app')
#            print "~ Deleting corporative java files from app folder ..."
#            app_directory(src, dest, 0)

def delete_jta(war_path):
   jta_jar_path = os.path.join(war_path, 'WEB-INF\lib\jta-1.1.jar')
   print "Apagando %s" % jta_jar_path
   if os.path.exists(jta_jar_path):
       os.remove(jta_jar_path)

def move_servlet(war_path):
   print "Movendo o arquivo LoginServlet.class para WEB-INF\classes\util..."
   arqOrigem = os.path.join(war_path, 'WEB-INF\\application\precompiled\java\util\LoginServlet.class')
   dirDestino = os.path.join(war_path, 'WEB-INF\classes\util')
   arqDestino = os.path.join(dirDestino, 'LoginServlet.class')
   if not os.path.exists(dirDestino):
       os.mkdir(dirDestino)
   elif os.path.exists(arqDestino):
       os.remove(arqDestino)
   shutil.move(arqOrigem, dirDestino)
   print "Arquivo LoginServlet.class movido"
