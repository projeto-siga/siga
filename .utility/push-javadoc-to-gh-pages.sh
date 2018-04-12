#!/bin/bash

pwd
ls

if [ "$TRAVIS_REPO_SLUG" == "projeto-siga/siga" ]; then echo “TRAVIS_REPO_SLUG - OK”; fi
if [ "$TRAVIS_JDK_VERSION" == "openjdk7" ]; then echo “TRAVIS_JDK_VERSION - OK”; fi
if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then echo “TRAVIS_PULL_REQUEST - OK”; fi
if [ "$TRAVIS_BRANCH" == "master" ]; then echo “TRAVIS_BRANCH - OK”; fi

if [ "$TRAVIS_REPO_SLUG" == "projeto-siga/siga" ] && [ "$TRAVIS_JDK_VERSION" == "openjdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing javadoc...\n"

  cp -R target/site/apidocs $HOME/javadoc-latest

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/projeto-siga/artifacts gh-pages > /dev/null

  cd gh-pages
  git rm -rf ./javadoc
  pwd
  cp -Rf $HOME/javadoc-latest ./javadoc
  git add -f .
  git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
  
fi
