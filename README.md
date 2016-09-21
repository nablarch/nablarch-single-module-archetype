nablarch-single-module-archetype
================================

# 概要

アーキタイプ及び、アーキタイプが参照するparentです。

本リポジトリには、single module構成のアーキタイプが複数格納されています。


# 存在するモジュール

| モジュール              | 説明                                                  |
|:------------------------|:------------------------------------------------------|
|nablarch-archetype-parent|各アーキタイプの共通的な設定を記述したpom.xml          |
|nablarch-web             |ウェブアプリケーション用アーキタイプ。                 |
|nablarch-jaxrs           |RESTfulウェブサービス用アーキタイプ。                  |
|nablarch-batch           |Nablarchバッチアプリケーション用アーキタイプ。         |
|nablarch-batch-ee        |JSR352に準拠したバッチアプリケーション用アーキタイプ。 |


# ビルド方法

## nablarch-archetype-parent

```
cd nablarch-archetype-parent
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


# ビルドしたアーキタイプからプロジェクトを生成する方法

## nablarch-archetype-parent

アーキタイプでは無いため省略。


## nablarch-web

```
mvn archetype:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-web-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)


## nablarch-jaxrs

```
mvn archetype:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-jaxrs-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)


## nablarch-batch

```
mvn archetype:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-batch-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)


## nablarch-batch-ee

```
mvn archetype:generate -DarchetypeGroupId=com.nablarch.archetype -DarchetypeArtifactId=nablarch-batch-ee-archetype -DarchetypeVersion=xxx
```
(xxxの箇所は、適切なバージョンを指定してください)
