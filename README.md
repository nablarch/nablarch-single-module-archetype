nablarch-single-module-archetype
================================

# 概要

ブランクプロジェクトを作成するためのアーキタイプの雛形および親プロジェクトが含まれています。

アーキタイプは、以下の手順で作成します。

1. 雛形プロジェクトからアーキタイプの作成
1. 作成されたアーキタイプのカスタマイズ
1. アーキタイプのインストールまたはデプロイ

本リポジトリにはアーキタイプ自体は含まれておらず、本リポジトリ内のファイルからアーキタイプを作成します。  
また、single module構成のアーキタイプ向けのプロジェクトが複数格納されています。


# 存在するモジュール

| モジュール                           | 説明                                                              |
|:--------------------------------|:----------------------------------------------------------------|
| nablarch-archetype-parent       | 各ひな形プロジェクトおよび各アーキタイプから生成されるプロジェクトの共通的な設定を記述したpom.xml。ブランクプロジェクトの親pom.xmlとなる。 |
| nablarch-archetype-build-parent | 各アーキタイプの共通的な設定を記述したpom.xml。                                     |
| nablarch-web                    | ウェブアプリケーション用アーキタイプの雛形プロジェクト。                                    |
| nablarch-jaxrs                  | RESTfulウェブサービス用アーキタイプの雛形プロジェクト。                                 |
| nablarch-batch                  | Nablarchバッチアプリケーション用アーキタイプの雛形プロジェクト。                            |
| nablarch-batch-ee               | JSR352に準拠したバッチアプリケーション用アーキタイプの雛形プロジェクト。                         |
| nablarch-batch-dbelss           | Nablarchバッチ（DB接続無し）アプリケーション用アーキタイプの雛形プロジェクト。                    |
| nablarch-container-web          | ウェブアプリケーションのDockerコンテナ用アーキタイプの雛形プロジェクト。                         |
| nablarch-container-jaxrs        | RESTfulウェブサービスのDockerコンテナ用アーキタイプの雛形プロジェクト。                      |
| nablarch-container-batch        | NablarchバッチアプリケーションのDockerコンテナ用アーキタイプの雛形プロジェクト。                 |
| nablarch-container-batch-dbless | Nablarchバッチ（DB接続無し）アプリケーションのDockerコンテナ用アーキタイプの雛形プロジェクト。         |


# ビルド方法

※GPGを使用しない環境で下記を実行する場合は、mvnコマンドに ``-Dgpg.skip=true`` を付加して実行すること。

## nablarch-archetype-parent

```
cd nablarch-archetype-parent
mvn install
```

## nablarch-archetype-build-parent

```
cd nablarch-archetype-build-parent
mvn install
```

## nablarch-web

```
# nablarch-webプロジェクトをベースにアーキタイプを生成
pushd nablarch-web
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-web.sh

cd nablarch-web/target/generated-sources/archetype/
mvn install
```

## nablarch-jaxrs

```
# nablarch-jaxrsプロジェクトをベースにアーキタイプを生成
pushd nablarch-jaxrs
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-jaxrs.sh

cd nablarch-jaxrs/target/generated-sources/archetype/
mvn install
```

## nablarch-batch

```
# nablarch-batchプロジェクトをベースにアーキタイプを生成
pushd nablarch-batch
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-batch.sh

cd nablarch-batch/target/generated-sources/archetype/
mvn install
```

## nablarch-batch-ee

```
# nablarch-batch-eeプロジェクトをベースにアーキタイプを生成
pushd nablarch-batch-ee
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-batch-ee.sh

cd nablarch-batch-ee/target/generated-sources/archetype/
mvn install
```

## nablarch-batch-dbless

```
# nablarch-batch-dblessプロジェクトをベースにアーキタイプを生成
pushd nablarch-batch-dbless
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-batch-dbless.sh

cd nablarch-batch-dbless/target/generated-sources/archetype/
mvn install
```

## nablarch-container-web

```
# nablarch-container-webプロジェクトをベースにアーキタイプを生成
pushd nablarch-container-web
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-container-web.sh

cd nablarch-container-web/target/generated-sources/archetype/
mvn install
```

## nablarch-container-jaxrs

```
# nablarch-container-jaxrsプロジェクトをベースにアーキタイプを生成
pushd nablarch-container-jaxrs
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-container-jaxrs.sh

cd nablarch-container-jaxrs/target/generated-sources/archetype/
mvn install
```

## nablarch-container-batch

```
# nablarch-container-batchプロジェクトをベースにアーキタイプを生成
pushd nablarch-container-batch
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-container-batch.sh

cd nablarch-container-batch/target/generated-sources/archetype/
mvn install
```

## nablarch-container-batch-dbless

```
# nablarch-container-batch-dblessプロジェクトをベースにアーキタイプを生成
pushd nablarch-container-batch-dbless
mvn clean archetype:create-from-project
popd

# 独自のカスタマイズを加える
./pre-create-maven-archetype-container-batch-dbless.sh

cd nablarch-container-batch-dbless/target/generated-sources/archetype/
mvn install
```

# ビルドしたアーキタイプからプロジェクトを生成する方法

## nablarch-archetype-parent

アーキタイプでは無いため省略。


## nablarch-web

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-web-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)


## nablarch-jaxrs

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-jaxrs-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)


## nablarch-batch

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-batch-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)


## nablarch-batch-ee

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-batch-ee-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)

## nablarch-batch-dbless

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-batch-dbless-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)

## nablarch-container-web

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-container-web-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)

## nablarch-container-jaxrs

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-container-jaxrs-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)

## nablarch-container-batch

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-container-batch-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)

## nablarch-container-batch-dbless

```
mvn org.apache.maven.plugins:maven-archetype-plugin:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-container-batch-dbless-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)
