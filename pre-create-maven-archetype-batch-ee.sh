#!/bin/bash

# archetypeのOSSRHにデプロイで必要となるbuild処理を定義した親pomをコピーする。
SCRIPT_DIR=$(dirname $0)

BUILD_PARENT_GROUP_ID=com.nablarch.archetype
BUILD_PARENT_ARTIFACT_ID=nablarch-archetype-build-parent
BUILD_PARENT_VERSION=$(grep -E '^  <version>(.+)</version>$' ${SCRIPT_DIR}/nablarch-archetype-build-parent/pom.xml | sed -r 's|^  <version>(.+)</version>$|\1|')

# コピーした親pomをarchetypのデプロイで利用されるpom.xml(archetype:create-from-projectで生成される)の親pomとなるように書き換える。
sed -iorig 's|/modelVersion>|/modelVersion>\n\n  <parent>\n    <groupId>'${BUILD_PARENT_GROUP_ID}'</groupId>\n    <artifactId>'${BUILD_PARENT_ARTIFACT_ID}'</artifactId>\n    <version>'${BUILD_PARENT_VERSION}'</version>\n  </parent>|' ./nablarch-batch-ee/target/generated-sources/archetype/pom.xml

# .gitignoreを配置する
cp ./gitignore/.gitignore ./nablarch-batch-ee/target/generated-sources/archetype/src/main/resources/archetype-resources

# configファイルの置換文字列が機能するようにした設定をコピーする
cp archetype-metadata-batch-ee.xml ./nablarch-batch-ee/target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml

pushd ./nablarch-batch-ee/target/generated-sources/archetype/src/main/resources/archetype-resources
# pom.xmlファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\/nablarch\/archetype/\${packageInPathFormat}/g" pom.xml
# pom.xmlファイル中のprofile名を修正する。
sed -i -e "s/      <artifactId>\${rootArtifactId}<\/artifactId>/      <artifactId>nablarch-batch-ee<\/artifactId>/g" pom.xml
# configファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\.nablarch\.archetype/\${package}/g" src/main/resources/*.properties
# SQLファイルを移動。
mv src/main/resources/com/nablarch/archetype/entity/*.sql src/main/resources/entity/
# 想定箇所以外が${version}に置換されている可能性があるため、pom.xml以外の置換は元に戻す
# `archetype_version` にはnablarch-archetype-parentのバージョンが入っているが、アーキタイプのバージョンと一致しているため問題ない。
archetype_version=`grep -m 1 "<version>" pom.xml | awk -F '[><]' '{print $3}'`
find . ! -name pom.xml -type f | xargs grep -l '${version}' | xargs --no-run-if-empty sed -i -e "s/\${version}/${archetype_version}/g"
popd

# このあと、nablarch-batch-ee/target/generated-sources/archetypeで「mvn install」を実行するとアーキタイプをインストールできる。
