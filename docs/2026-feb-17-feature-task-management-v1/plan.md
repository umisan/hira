# Feature Plan: Task Management v1

## Goal
タスク管理の最小機能として、以下をSSRで提供する。

- タスク一覧表示（担当者・ステータスでフィルタ）
- タスク登録（タイトル、説明、優先度、重さ、期限、担当者）
- タスク詳細表示
- タスクステータス更新（POST + redirect）

## Scope
- Spring MVC Controller
- Service
- Repository（Task, Member）
- Thymeleaf テンプレート（`/tasks`, `/tasks/new`, `/tasks/{id}`）
- 単体テスト（TDD）

## Out Of Scope
- タスク編集フォーム（`GET /tasks/{id}/edit`）
- タスク削除
- 週次機能
- オフライン機能

## TDD Steps
1. `TaskService` の振る舞いテストを追加
2. `TaskController` のHTTP振る舞いテストを追加
3. 最小実装でテストを通す
4. リファクタリング

## PR Draft Title
`feat(tasks): add task list/create/status-update with SSR pages`
