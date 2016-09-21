#!/bin/sh

# archetypeのOSSRHにデプロイで必要となるbuild処理を定義した親pomをコピーする。
cp ./archetype-build-parent.xml ./nablarch-batch-ee/target/generated-sources/archetype/

# コピーした親pomをarchetypのデプロイで利用されるpom.xml(archetype:create-from-projectで生成される)の親pomとなるように書き換える。
sed -iorig 's|/modelVersion>|/modelVersion>\n <parent>\n<groupId>com.nablarch.archetype</groupId>\n<artifactId>archetype-build-parent</artifactId>\n<version>1.0.0</version>\n<relativePath>archetype-build-parent.xml</relativePath>\n</parent>|' ./nablarch-batch-ee/target/generated-sources/archetype/pom.xml

# configファイルの置換文字列が機能するようにした設定をコピーする
cp archetype-metadata-batch-ee.xml ./nablarch-batch-ee/target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml

pushd ./nablarch-batch-ee/target/generated-sources/archetype/src/main/resources/archetype-resources
# pom.xmlファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\/nablarch\/archetype/\${packageInPathFormat}/g" pom.xml
# pom.xmlファイル中のprofile名を修正する。
sed -i -e "s/      <artifactId>\${rootArtifactId}<\/artifactId>/      <artifactId>nablarch-batch-ee<\/artifactId>/g" pom.xml
# configファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\.nablarch\.archetype/\${package}/g" src/main/resources/*.config
# etl.json中のパッケージを置換文字列にする。
sed -i -e "s/com\.nablarch\.archetype/\${package}/g" src/main/resources/META-INF/etl.json
popd

# このあと、nablarch-batch-ee/target/generated-sources/archetypeで「mvn install」を実行するとアーキタイプをインストールできる。
