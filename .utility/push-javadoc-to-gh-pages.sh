#!/bin/bash

echo -e “some vars…\n”
echo -e “$TRAVIS_REPO_SLUG”
echo -e “\n”
echo -e “$TRAVIS_JDK_VERSION”
echo -e “\n”
echo -e “$TRAVIS_PULL_REQUEST”
echo -e “\n”
echo -e “$TRAVIS_BRANCH”
echo -e “\n”
pwd
echo -e “\n”
ls
echo -e “\n”

if [ "$TRAVIS_REPO_SLUG" == “projeto-siga/siga” ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == “vraptor” ]; then

  echo -e "Publishing javadoc...\n"

  cp -R target/site/apidocs $HOME/javadoc-latest

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/projeto-siga/siga gh-pages > /dev/null

  cd gh-pages
  git rm -rf ./javadoc
  pwd
  cp -Rf $HOME/javadoc-latest ./javadoc
  git add -f .
  git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
  
fi