#!/bin/bash

SCRIPT_DIR=$(dirname $0)

BUILD_PARENT_GROUP_ID=com.nablarch.archetype
BUILD_PARENT_ARTIFACT_ID=nablarch-archetype-build-parent
BUILD_PARENT_VERSION=$(grep -E '^  <version>(.+)</version>$' ${SCRIPT_DIR}/nablarch-archetype-build-parent/pom.xml | sed -r 's|^  <version>(.+)</version>$|\1|')

# archetypeのデプロイで利用されるpom.xml(archetype:create-from-projectで生成される)の親pomをnablarch-archetype-build-parentとなるように書き換え、developers、licenses、scmを消去する。
sed -iorig -e 's|/modelVersion>|/modelVersion>\n\n  <parent>\n    <groupId>'${BUILD_PARENT_GROUP_ID}'</groupId>\n    <artifactId>'${BUILD_PARENT_ARTIFACT_ID}'</artifactId>\n    <version>'${BUILD_PARENT_VERSION}'</version>\n  </parent>|' -r -e 's! *</?(developers?|licenses?|scm).*!!' ./nablarch-batch/target/generated-sources/archetype/pom.xml

# .gitignoreを配置する
cp ./gitignore/.gitignore ./nablarch-batch/target/generated-sources/archetype/src/main/resources/archetype-resources

# configファイルの置換文字列が機能するようにした設定をコピーする
cp archetype-metadata-batch.xml ./nablarch-batch/target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml

pushd ./nablarch-batch/target/generated-sources/archetype/src/main/resources/archetype-resources
# pom.xmlファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\/nablarch\/archetype/\${packageInPathFormat}/g" pom.xml
# pom.xmlファイル中のprofile名を修正する。
sed -i -e "s/      <artifactId>\${rootArtifactId}<\/artifactId>/      <artifactId>nablarch-batch<\/artifactId>/g" pom.xml
# configファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\.nablarch\.archetype/\${package}/g" src/main/resources/*.properties
popd

# このあと、nablarch-batch/target/generated-sources/archetypeで「mvn install」を実行するとアーキタイプをインストールできる。
