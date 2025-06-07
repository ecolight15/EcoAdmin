# EcoAdmin

EcoAdminは、Minecraftサーバー管理者向けの包括的な管理プラグインです。プレイヤー管理、サーバー保護、ワールド設定、ユーティリティ機能など、サーバー運営に必要な多くの機能を提供します。

## 概要

- **バージョン**: 1.30
- **対応Minecraft**: 1.20.4+
- **作者**: ecolight
- **依存プラグイン**: EcoFramework (必須)
- **オプション依存**: EcoGate

## 主な機能

### 🎮 プレイヤー管理コマンド
- **テレポート系**: `/tp`, `/tpp`, `/tphere`, `/back`, `/spawn`, `/setspawn`
- **プレイヤー状態**: `/heal`, `/feed`, `/gm` (ゲームモード), `/fly`, `/clear`
- **位置情報**: `/loc`, `/plook`, `/zlook`, `/xlook`
- **特殊能力**: `/t` (トールハンマーモード - 見つめた場所に雷を落とす)

### 🔨 管理・制裁コマンド  
- **制裁**: `/freeze`, `/kill`, `/pkill`, `/strike` (雷), `/hide`, `/show`
- **サーバー制御**: `/stop`, `/fstop` (強制停止), `/lock`, `/unlock`
- **その他**: `/pvp`, `/login`, `/wrule` (ワールドルール)

### 📊 モニタリング
- **TPS監視**: `/tpsv` (TPS表示), `/tpsee` (TPS常時表示切替)
- **サーバー状態**: リアルタイムでのサーバーパフォーマンス監視

### 🛡️ 保護機能
- **ブロック操作保護**: ベッド、リスポーンアンカー、エンダークリスタル、モブエッグ等の操作制限
- **ブロック設置保護**: 濡れたスポンジ、エンドシティパス、マップの設置制限
- **移動制限**: 水上歩行、コーラスフルーツテレポート制限
- **爆発制御**: エンダークリスタル爆発の制御
- **インベントリ保護**: クリエイティブモードでのインベントリ操作制御

### 🌍 ワールド管理
- **難易度設定**: ワールド毎の難易度制御 (ピースフル、イージー、ノーマル、ハード)
- **PvP設定**: ワールド毎のPvP有効/無効制御
- **ゲームルール**: `disableRaids`等のゲームルール制御
- **モブスポーン制御**: ドラウンド、ファントム、スケルトンホース等の特定モブスポーン制御

### 🔧 ユーティリティ機能
- **自動セーブ**: 設定可能な間隔での自動ワールドセーブ
- **AFK検出**: 非アクティブプレイヤーの自動キック
- **看板編集**: 高度な看板編集機能（カラーコード対応、ワックス保護）
- **ログインメッセージ**: カスタムログインメッセージシステム
- **握手アイテム交換**: プレイヤー間のアイテム交換機能

### 📝 ログシステム
- 各種保護機能の拒否ログ
- キック情報ログ
- 操作拒否ログ

## インストール

1. **前提条件**:
   - Spigot/Paper 1.20.4以上
   - EcoFramework プラグイン (必須)

2. **インストール手順**:
   ```bash
   # プラグインフォルダにjarファイルを配置
   plugins/
   ├── EcoFramework.jar
   └── EcoAdmin.jar
   ```

3. **サーバー再起動** または `/eca reload` コマンドで設定をリロード

## 設定

メイン設定ファイル: `plugins/EcoAdmin/config.yml`

### 基本設定例

```yaml
# コマンド設定
cmd:
  lockdown:
    allow_op: false
  kill:
    disable_kill_message: true

# ユーティリティ機能
util:
  auto_save:
    enable: true
    interval_tick: 6000
  afk:
    enable: true
    limit_milliseconds: 300000

# 楽しい機能
fun:
  handshake_item_exchange: true
  sign_colorcode_convert: true
  login_message: true

# 保護機能
protection:
  water_walking:
    disable: true
    world_list:
      - world
      - world_nether
      - world_the_end
```

## コマンド一覧

### 管理コマンド
| コマンド | 説明 | 権限 |
|---------|------|------|
| `/eca reload` | 設定リロード | `ecoadmin.reload` |
| `/eca test` | テストコマンド | `ecoadmin.test` |

### プレイヤー管理
| コマンド | 説明 | 権限 |
|---------|------|------|
| `/tp <player>` | プレイヤーにテレポート | `ecoadmin.tp` |
| `/tpp <player>` | プレイヤーを自分の位置にテレポート | `ecoadmin.tpp` |
| `/tphere <player>` | プレイヤーを呼び寄せ | `ecoadmin.tphere` |
| `/back` | 前の位置に戻る | `ecoadmin.back` |
| `/spawn` | スポーン地点にテレポート | `ecoadmin.spawn` |
| `/setspawn` | スポーン地点を設定 | `ecoadmin.setspawn` |
| `/heal [player] [amount]` | 体力回復 | `ecoadmin.heal` |
| `/feed [player]` | 満腹度回復 | `ecoadmin.feed` |
| `/clear [player]` | インベントリクリア | `ecoadmin.clear` |
| `/gm <mode> [player]` | ゲームモード変更 | `ecoadmin.gm` |
| `/fly [player]` | 飛行モード切替 | `ecoadmin.fly` |
| `/t [real/dumy]` | トールハンマーモード切替 | `ecoadmin.thor` |

### 制裁・制御
| コマンド | 説明 | 権限 |
|---------|------|------|
| `/freeze <player>` | プレイヤーの動きを止める | `ecoadmin.freeze` |
| `/kill <player>` | プレイヤーを倒す | `ecoadmin.kill` |
| `/strike <player>` | プレイヤーに雷を落とす | `ecoadmin.strike` |
| `/hide [player]` | プレイヤーを透明化 | `ecoadmin.hide` |
| `/show [player]` | 透明化解除 | `ecoadmin.show` |

### 情報・監視
| コマンド | 説明 | 権限 |
|---------|------|------|
| `/loc [player]` | 位置情報表示 | `ecoadmin.loc` |
| `/tpsv` | TPS情報表示 | `ecoadmin.tpsv` |
| `/tpsee` | TPS常時表示切替 | `ecoadmin.tpsee` |

### サーバー制御
| コマンド | 説明 | 権限 |
|---------|------|------|
| `/stop` | サーバー停止 | `ecoadmin.stop` |
| `/fstop` | 強制サーバー停止 | `ecoadmin.fstop` |
| `/lock` | サーバーロック | `ecoadmin.lock` |
| `/unlock` | サーバーロック解除 | `ecoadmin.unlock` |
| `/pvp [enable/disable]` | PvP切替 | `ecoadmin.pvp` |
| `/wrule <rule> <value>` | ワールドルール設定 | `ecoadmin.worldrule` |

## 権限システム

### 基本権限
- `ecoadmin.*` - 全ての権限
- `ecoadmin.reload` - 設定リロード
- `ecoadmin.test` - テストコマンド

### プレイヤー管理権限
- `ecoadmin.tp` - テレポート
- `ecoadmin.heal` - 体力回復
- `ecoadmin.feed` - 満腹度回復
- `ecoadmin.gm` - ゲームモード変更
- `ecoadmin.fly` - 飛行モード
- `ecoadmin.thor` - トールハンマーモード

### 制裁権限
- `ecoadmin.freeze` - プレイヤー凍結
- `ecoadmin.kill` - プレイヤー殺害
- `ecoadmin.strike` - 雷攻撃

### サーバー制御権限
- `ecoadmin.stop` - サーバー停止
- `ecoadmin.lock` - サーバーロック
- `ecoadmin.worldrule` - ワールドルール変更

## 依存関係

### 必須依存
- **EcoFramework**: 基盤フレームワーク

### オプション依存
- **EcoGate**: ゲート機能との連携

## 対応バージョン

- **Minecraft**: 1.20.4+
- **Java**: 17+
- **Spigot/Paper**: 1.20.4+

## ライセンス

このプラグインのライセンスについては、[LICENSE](LICENSE) ファイルを参照してください。

## サポート・貢献

バグ報告や機能要望は、GitHubのIssuesまでお願いします。
プルリクエストも歓迎します。

## 更新履歴

- **v1.30**: 現在の安定版
  - Minecraft 1.20.4対応
  - 各種機能の安定化
  - バグ修正と最適化

---

**作者**: ecolight  
**GitHub**: [ecolight15/EcoAdmin](https://github.com/ecolight15/EcoAdmin)