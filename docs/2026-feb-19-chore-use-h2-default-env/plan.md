# Feature Plan: Use H2 In Default Environment

## Goal
通常環境（テスト以外）でも H2 を利用するように Spring Boot のデータソース設定を変更する。

## Scope
- `application.yaml` のデフォルト datasource を H2 に変更
- `pom.xml` の H2 依存を通常実行時に利用できるスコープへ変更
- 起動テストでデフォルト設定が有効であることを確認
- PR 作成

## Out Of Scope
- DB スキーマ設計変更
- PostgreSQL 向けの移行対応
- アプリ機能追加

## TDD Steps
1. デフォルト設定で `contextLoads` が通ることを前提にテストを調整
2. 設定と依存関係を変更
3. `mvn test` で回帰確認

## PR Draft Title
`chore(config): use h2 datasource in default environment`
