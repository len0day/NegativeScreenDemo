# NegativeScreenDemo

Android 负一屏 WebView 演示 Demo

---

## 一、项目结构

```
NegativeScreenDemo/
├── app/
│   ├── build.gradle                         ← 模块构建配置
│   └── src/main/
│       ├── AndroidManifest.xml              ← 应用清单
│       ├── assets/
│       │   └── index.html                   ← H5 演示页面
│       ├── java/com/demo/negative/
│       │   ├── MainActivity.kt              ← 主界面
│       │   ├── NegativeScreenActivity.kt    ← 负一屏（全屏 WebView）
│       │   ├── provider/
│       │   │   └── NegativeScreenWidgetProvider.kt
│       │   └── service/
│       │       └── NegativeScreenService.kt
│       └── res/
│           ├── layout/
│           │   ├── activity_main.xml
│           │   ├── activity_negative_screen.xml
│           │   └── widget_negative_screen.xml
│           ├── xml/
│           │   └── negative_screen_widget_info.xml
│           └── values/
│               └── styles.xml
├── build.gradle
├── settings.gradle
├── gradle.properties
├── README.md
└── 技术文档.md                               ← 详细技术文档（含问题解决）
```

---

## 二、快速开始

### 1. 打开项目

```
Android Studio → File → Open → /Users/len/Downloads/NegativeScreenDemo
```

### 2. 设置 Gradle JDK

```
Android Studio → Preferences → Build, Execution, Deployment → Gradle
→ Gradle JDK: 选择 17
```

### 3. Sync & Run

```
File → Sync Project with Gradle Files
Run → Run 'app'
```

### 4. 体验

- 点击主界面「**打开负一屏**」按钮
- 全屏展示 H5 页面（实时时钟 + 快捷功能）
- 点击右上角「**关闭**」按钮退出

---

## 三、遇到的问题及解决

| 问题 | 原因 | 解决方法 |
|------|------|---------|
| Manifest 组件报错 | 组件放在 `<application>` 外部 | 将 receiver/service/activity 移到 application 内部 |
| JVM 版本不兼容 | Java 1.8 vs Kotlin 17 | 在 build.gradle 添加 compileOptions 和 kotlinOptions |
| AppCompat 主题缺失 | 未指定 AppCompat 主题 | 创建 styles.xml 并指定主题 |
| ClassNotFoundException | APK 未正确编译 | `./gradlew clean` 后重新构建 |
| ViewBinding 报错 | 未启用 viewBinding | 在 build.gradle 添加 `buildFeatures { viewBinding true }` |

详细解决过程请参考 `技术文档.md`。

---

## 四、实现原理

```
用户点击按钮
    │
    ▼
MainActivity.openNegativeScreen()
    │
    ▼
Intent(Intent(this, NegativeScreenActivity::class.java))
    │
    ▼
startActivity(intent)
    │
    ▼
NegativeScreenActivity.onCreate()
    │
    ▼
WebView.loadUrl("file:///android_asset/index.html")
    │
    ▼
显示 H5 页面（实时时钟 + 快捷功能）
```

---

## 五、关于 ZTE 负一屏接入

桌面左滑进入负一屏是**系统级功能**，普通 App 无法直接实现。

### 当前 Demo 能力

| 功能 | 支持 |
|------|------|
| WebView 加载 H5 | ✅ 支持 |
| 全屏展示 | ✅ 支持 |
| 点击按钮触发 | ✅ 支持 |
| 桌面左滑触发 | ❌ 不支持（需要 ZTE SDK） |

### 接入 ZTE 负一屏

需要联系 ZTE 获取：
1. **智慧屏 SDK**
2. **签名授权**（特殊签名才能注册负一屏入口）
3. **开发文档**

具体步骤请参考 `技术文档.md` 第 **五** 节。

---

## 六、核心代码

### NegativeScreenActivity（WebView 全屏展示）

```kotlin
class NegativeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNegativeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWebView()
    }

    private fun setupWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(false)
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        binding.webView.loadUrl("file:///android_asset/index.html")
    }
}
```

### H5 页面（assets/index.html）

- 实时时钟（每秒更新）
- 快捷功能卡片（天气、日程、快捷开关）
- 紫色渐变背景

---

## 版本

- v1.0（2026-04-29）初始版本