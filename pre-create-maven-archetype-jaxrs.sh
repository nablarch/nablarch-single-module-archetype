#!/bin/bash

# archetypeのOSSRHにデプロイで必要となるbuild処理を定義した親pomをコピーする。
cp ./archetype-build-parent.xml ./nablarch-jaxrs/target/generated-sources/archetype/
 
# コピーした親pomをarchetypのデプロイで利用されるpom.xml(archetype:create-from-projectで生成される)の親pomとなるように書き換える。
 sed -iorig 's|/modelVersion>|/modelVersion>\n <parent>\n<groupId>com.nablarch.archetype</groupId>\n<artifactId>archetype-build-parent</artifactId>\n<version>1.0.0</version>\n<relativePath>archetype-build-parent.xml</relativePath>\n</parent>|' ./nablarch-jaxrs/target/generated-sources/archetype/pom.xml

# .gitignoreを配置する
cp ./gitignore/.gitignore ./nablarch-jaxrs/target/generated-sources/archetype/src/main/resources/archetype-resources

# .gitignoreをアーキタイプに含めるため、maven-resources-pluginのコンフィグを明示的に設定する
sed -i -e "s|</extensions>|</extensions>\n<plugins><plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-resources-plugin</artifactId><configuration><addDefaultExcludes>false</addDefaultExcludes></configuration></plugin></plugins>|" ./nablarch-jaxrs/target/generated-sources/archetype/pom.xml

# configファイルの置換文字列が機能するようにした設定をコピーする
cp archetype-metadata-jaxrs.xml ./nablarch-jaxrs/target/generated-sources/archetype/src/main/resources/META-INF/maven/archetype-metadata.xml

pushd ./nablarch-jaxrs/target/generated-sources/archetype/src/main/resources/archetype-resources
# pom.xmlファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\/nablarch\/archetype/\${packageInPathFormat}/g" pom.xml
# pom.xmlファイル中のprofile名を修正する。
sed -i -e "s/      <artifactId>\${rootArtifactId}<\/artifactId>/      <artifactId>nablarch-jaxrs<\/artifactId>/g" pom.xml
# configファイル中のパッケージを置換文字列にする。
sed -i -e "s/com\.nablarch\.archetype/\${package}/g" src/main/resources/*.properties
# 想定箇所以外が${version}に置換されている可能性があるため、pom.xml以外の置換は元に戻す
# `archetype_version` にはnablarch-archetype-parentのバージョンが入っているが、アーキタイプのバージョンと一致しているため問題ない。
archetype_version=`grep -m 1 "<version>" pom.xml | awk -F '[><]' '{print $3}'`
grep -rl '${version}' --exclude=pom.xml | xargs --no-run-if-empty sed -i -e "s/\${version}/${archetype_version}/g"
popd

# このあと、nablarch-jaxrs/target/generated-sources/archetypeで「mvn install」を実行するとアーキタイプをインストールできる。
