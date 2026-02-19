---
name: pr-creation
description: ghコマンドを利用してPRの作成を行うスキル
license: Proprietary
compatibility: Skills-compatible agents. gh is required
metadata:
  author: umino
  version: "0.1"
---

# 目的

このスキルの目的は、ghコマンドを利用して現在のブランチのPull Request (PR)をgithubに作成することです。
PRのタイトルには実装した機能を簡潔に記載し、PRの本文には以下の内容を記載します。
- 実装した機能についての概要
- 変更を施したファイルのリスト
- 動作確認結果
  - 単体テストの実行結果

# 動作手順

以下のステップでPRを作成します。

1. 現在のブランチをgithubへpush
2. 現在のブランチで行われた実装内容の把握
3. PRのタイトルの作成
4. 作成したPRタイトルを環境変数PR_TITLEに設定
5. PRの本文に記載する内容の作成
6. 作成したPRの本文を一時ファイルtmp.txtに書き出す
7. ghコマンドを用いてPRを作成

ghコマンドはコマンドの章に記載されている内容に基づいて利用します。

# コマンド

PRを作成する際は以下のコマンドを実行します。
```
gh pr create --title $PR_TITLE --body-file tmp.txt
```
