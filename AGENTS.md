# 家庭内タスク管理ツール 設計仕様書

## 概要

本システムは家庭内で利用するタスク管理ツールである。  
Jira のようなタスク管理の仕組みを、家庭向けに簡潔な UI と運用しやすさを重視して再構築する。

- 外部ネットワークへ公開しない（LAN 内のみで利用）
- 認証ログインは不要（家庭内でのみ運用するため）
- 必要なときに **出先で閲覧できるようオフライン閲覧機能を提供**
- UI は極力シンプルにし SPA は採用しない（フル SSR）
- 実装技術は **Spring Boot + Thymeleaf** を中心とする  
  ※後から必要であれば htmx を局所的に導入可能（v1 では不要）

---

## 1. 機能要件

### 1.1 タスク管理

- タスクを登録できる
- タスクに以下を設定可能
  - タイトル
  - 説明
  - 優先度（3段階）
  - 重さ（1〜5）
  - 期限（任意）
  - 状態（TODO / IN_PROGRESS / DONE / CANCELLED）
  - 担当者
- タスクの編集・削除
- タスク一覧の閲覧（フィルタ：担当者・ステータス）

### 1.2 週次管理（Weeks）

- 週ごとに「今週やるタスク」を設定できる
- 「今週のタスク」画面（ダッシュボード表示）
- 未完了タスクの翌週への持ち越し（手動）

### 1.3 担当者（家族メンバー）

- 家族メンバーを登録（名前・色）
- 非アクティブ化（利用しなくなったメンバーを非表示にできる）

### 1.4 オフライン閲覧機能

- `/offline/export.json` により最新データを一括エクスポート
- `/offline` ページで JSON をキャッシュし、オフライン閲覧可能
  - Service Worker により `/offline` ページと静的リソースをキャッシュ
  - `localStorage` に同期した JSON を保存し、出先で閲覧可能
- オフラインでの編集は不可（閲覧専用）

---

## 2. 非機能要件

- 外部ネットワークには公開しない  
  → 家庭内 LAN のみでホストする想定
- UI はシンプルな SSR（Thymeleaf）
- 認証ログインは不要
- 低い学習コスト・運用容易性を優先
- データは数百件規模を想定（軽量 DB で十分）

---

## 3. 技術スタック

| 層 | 技術 |
|----|------|
| Backend | Spring Boot |
| Web Framework | Spring MVC + Thymeleaf |
| DB | H2 / SQLite / PostgreSQL（家庭内なら H2 でも可） |
| テンプレート | Thymeleaf |
| フロント最小JS | Service Worker + localStorage（オフライン用） |
| オプション | htmx（部分更新を後から追加可能。v1 では不要） |

---

## 4. ドメインモデル（エンティティ設計）

### 4.1 Member（家族メンバー）

| 項目 | 型 | 説明 |
|------|-----|--------|
| id | Long | PK |
| name | String | メンバー名 |
| color | String | UI 表示色 |
| active | boolean | アクティブ/非アクティブ |
| createdAt | LocalDateTime | 作成日時 |
| updatedAt | LocalDateTime | 更新日時 |

### 4.2 Task（タスク）

| 項目 | 型 | 説明 |
|------|-----|--------|
| id | Long | PK |
| title | String | タイトル |
| description | String | 詳細 |
| priority | enum | LOW / MEDIUM / HIGH |
| weight | Integer | 重さ（1〜5） |
| status | enum | TODO / IN_PROGRESS / DONE / CANCELLED |
| dueDate | LocalDate | 期限 |
| assignee | Member | 担当者（任意） |
| createdAt | LocalDateTime | 作成日時 |
| updatedAt | LocalDateTime | 更新日時 |

### 4.3 Week（週管理）

| 項目 | 型 | 説明 |
|------|-----|--------|
| id | Long | PK |
| startDate | LocalDate | 週の開始日 |
| endDate | LocalDate | 週の終了日 |
| label | String | "2025-W49" のような表示用 |

### 4.4 WeekTask（週とタスクの紐付け）

| 項目 | 型 | 説明 |
|------|-----|--------|
| id | Long | PK |
| week | Week | 紐づく週 |
| task | Task | 紐づくタスク |
| carriedOver | boolean | 前週からの持ち越しか |
| plannedWeight | Integer | その週に割く予定ポイント（任意） |

---

## 5. URL / 画面設計

### 5.1 ダッシュボード（今週のタスク）

- `GET /`
  - 今週の Week を取得（なければ作成）
  - `week_tasks` を一覧表示
  - 担当者ごとの合計重さなども表示
  - 軽いフィルタ（担当者切り替えなど）

### 5.2 タスク一覧 / 登録 / 編集

- `GET /tasks`  
  タスク一覧（フィルタ付き）
- `GET /tasks/new`  
  タスク登録フォーム
- `POST /tasks`  
  タスク作成
- `GET /tasks/{id}`  
  タスク詳細
- `GET /tasks/{id}/edit`  
  タスク編集
- `POST /tasks/{id}/status`  
  ステータス更新（POST → redirect）

### 5.3 週次プランニング

- `GET /weeks/current`  
  現在週を取得して表示
- `GET /weeks/{id}`  
  任意の週を表示
- `POST /weeks/{id}/add-task`  
  週にタスクを追加
- `POST /weeks/{id}/carryover`  
  未完了タスクを次週へ持ち越し（手動）

### 5.4 オフライン閲覧

- `GET /offline`  
  オフライン専用ビュー（Service Worker + localStorage を使用）
- `GET /offline/export.json`  
  全データの JSON（ブラウザキャッシュ用）
- `sw.js`  
  Service Worker（`/offline` と静的ファイルをキャッシュ）

---

## 6. オフライン閲覧の詳細設計

### 6.1 同期の流れ（オンライン時）

1. `/offline` を開く
2. 「同期」ボタンを押すと `/offline/export.json` を fetch する
3. 得られた JSON を `localStorage` に保存
4. 保存した JSON を元に「今週のタスク一覧」を描画

### 6.2 オフライン時

- Service Worker が `/offline` をキャッシュしているためページは開ける
- JS が `localStorage` の JSON を読み込み表示
- ネットワークアクセスは失敗するが問題なし（閲覧専用）

### 6.3 保存されるデータ構造

```json
{
  "generated_at": "2025-12-06T10:00:00+09:00",
  "members": [...],
  "tasks": [...],
  "weeks": [...],
  "week_tasks": [...]
}
