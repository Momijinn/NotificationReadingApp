NotificationReadingApp
====
Lineから通知された文章を読み上げるアプリ

## Description
Lineの通知を受けるとその通知したメッセージを内容を読み上げるAndroidアプリ

## Demo
[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/Uvw2_F6ggaw/0.jpg)](https://www.youtube.com/watch?v=Uvw2_F6ggaw)

## Usage
* 起動後にこのアプリの「通知へのアクセス」を許可すれば、通知バーに常駐します.
* Lineから通知がくると、女性の声に合成された音声で文章を読み上げてくれます.(音声切り替えは未実装)
* マナーモードやサイレントマナーに設定中は読み上げをしないようにしました.
* 注意
    * 読み上げを停止するときは、このアプリの「通知へのアクセス」をOFFにしてください.OFFにしないと常駐し続けます.
    * NotificationListenerServiceを利用していることから、Android4.3以上でないと動作しません.
    * Lineの仕様が変更させると動作しない可能性があります.
    * 動作確認はしましたが、強制終了する場合があります.
    * 端末によっては読み上げてくれない場合があります. 対処法: 「設定」→「音と通知」→「メディアの音量」を右にスライドすると読み上げてくれます.

## Install
deloyGateにてアップロードしています

https://deploygate.com/distributions/591bdd435a665c47c59f7cb2c1b177c6c32a956f

## Licence
This software is released under the MIT License, see LICENSE.

## Author
[Twitter](https://twitter.com/momijinn_aka)

[Blog](http://www.autumn-color.com/)