[ ![Download](https://api.bintray.com/packages/misyobun/maven/MSBVersionUpdater/images/download.svg) ](https://bintray.com/misyobun/maven/MSBVersionUpdater/_latestVersion)

### This library is based on kazu0620’s SRGVersionUpdater which is iOS library. (https://github.com/kazu0620/SRGVersionUpdater)

### Difference between SRGVersionUpdater
* if user cancel latest update, `MSBVersionUpdater` doesn't show again alert dialog unless required_version becomes latest.(When type is `optional`)
* `MSBVersionUpdater` deals with the problem which user skips force update,

## Usage

### 1. Upload a JSON file below
```
{"last_force_required_version":"1.0.0","required_version":"1.1.0","type":"optional","update_url":"https://play.google.com/store/apps/details?id=misyobun.com.regardstoblackjack"}
```

| key | define |
| --: | --- | 
| last_force_required_version | Last force required_version. |
| required_version | The version of latest client application. | 
| type | The type of update, “force” or “optional”. |
| update_url | Google Play  URL of your app. |

### 2. Download MSBVersionUpdater

#### build.gradle

```
repositories {
    jcenter()
}
```

```
dependencies {
    compile 'jp.co.misyobun.lib.versionupdater:versionupdater:1.4.0'
}
```

### 3. implement below

#### default
```
    public override fun onResume() {
        super.onResume()
        val updater = MSBVersionUpdater(this)
        updater.endpoint = "[json url]"
        updater.executeVersionCheck()
    }
```

#### You can set Title and Message
```
    public override fun onResume() {
        super.onResume()
        val updater = MSBVersionUpdater(this)
        updater.endpoint = "[json url]"
        updater.title = "[title]"
        updater.message = "[message]"
        updater.executeVersionCheck()
    }
```

#### You can set forceTitle and forceMessage
```
    public override fun onResume() {
        super.onResume()
        val updater = MSBVersionUpdater(this)
        updater.endpoint = "[json url]"
        updater.title = "[title]"
        updater.message = "[message]"
        updater.forceTitle = "[forceTitle]"
        updater.forceMessage = "[forceMessage]"
        updater.executeVersionCheck()
    }

```

#### You can set text of button positive and negative
```
    public override fun onResume() {
        super.onResume()
        val updater = MSBVersionUpdater(this)
        updater.endpoint = "[json url]"
        updater.title = "[title]"
        updater.message = "[message]"
        updater.forceTitle = "[forceTitle]"
        updater.forceMessage = "[forceMessage]"
        updater.positiveButtonText = "[positiveButtonText]"
        updater.negativeButtonText = "[negativeButtonText]"
        updater.executeVersionCheck()
    }
```
