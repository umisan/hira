# feature-task-edit-delete 計画

## 目的

AGENTS.md の機能要件 1.1 にある「タスクの編集・削除」を実装する。

## 対象

- タスク編集画面表示 (`GET /tasks/{id}/edit`)
- タスク更新 (`POST /tasks/{id}`)
- タスク削除 (`POST /tasks/{id}/delete`)
- 画面導線（詳細画面から編集・削除）

## 実装方針

1. 先にテストを追加する（TaskServiceTest, TaskControllerTest）
2. 最小限のフォームオブジェクトを追加する
3. Service に update/delete を追加する
4. Controller とテンプレートを追加・更新する
5. テストを通し、手動確認可能な状態にする

## 完了条件

- 編集・削除に関するテストが追加され、すべて成功する
- 編集画面で既存値を更新できる
- 削除後に一覧へリダイレクトされる
