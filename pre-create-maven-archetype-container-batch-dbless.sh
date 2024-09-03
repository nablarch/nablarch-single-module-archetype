#!/bin/bash

SCRIPT_DIR=$(dirname $0)

BUILD_PARENT_GROUP_ID=com.nablarch.archetype
BUILD_PARENT_ARTIFACT_ID=nablarch-archetype-build-parent
BUILD_PARENT_VERSION=$(grep -E '^  <version>(.+)</version>$' ${SCRIPT_DIR}/nablarch-archetype-build-parent/pom.xml | sed -r 's|^  <version>(.+)</version>$|\1|')

# コピーした親pomをarchetypのデプロイで利用されるpom.xml(archetype:create-from-projectで生成される)の親pomとなるように書き換える。
sed -iorig 's|/modelVersion>|/modelVersion>\n\n <parent>\n <groupId>'${BUILD_PARENT_GROUP_ID}'</groupId>\n <artifactId>'${BUILD_PARENT_ARTIFACT_ID}'</artifactId>\n <version>'${BUILD_PARENT_VERSION}'</version>\n </parent>|' ./nablarch-container-batch-dbless/target/generated-sources/archetype/pom.xml

# .gitignoreを配置する
cp ./gitignore/.gitignore ./nablarch-container-batch-dbless/target/generated-sources/archetype/src/main/resources/archetype-resources

# configファイルの置換文字列が機能するようにした設定をコピーする
cp archetype-metadata-container-batch-dbless.xml ./nablarch-container-batch-dbless/target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml

pushd ./nablarch-container-batch-dbless/target/generated-sources/archetype/src/main/resources/archetype-resources
# pom.xmlファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\/nablarch\/archetype/\${packageInPathFormat}/g" pom.xml
# pom.xmlファイル中のprofile名を修正する。
sed -i -e "s/      <artifactId>\${rootArtifactId}<\/artifactId>/      <artifactId>nablarch-container-batch-dbless<\/artifactId>/g" pom.xml
# configファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\.nablarch\.archetype/\${package}/g" src/main/resources/*.properties
popd

# このあと、nablarch-container-batch-dbless/target/generated-sources/archetypeで「mvn install」を実行するとアーキタイプをインストールできる。
