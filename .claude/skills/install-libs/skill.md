---
description: Install dt-core-lib and dt-queue-lib to local Maven repo. Use when the user needs to build DTCore after changing internal libraries.
---

## Install Internal Libraries

Set JAVA_HOME before running Maven:

```
$env:JAVA_HOME = "C:\NOOM_PRO\program_noom2\program_noom2\microsoft-jdk-21.0.10-windows-x64\jdk-21.0.10+7"
```

Run the following in order using PowerShell:

1. `cd "D:\VS_CODE_code_claude\DTCore-Lib"; $env:JAVA_HOME = "C:\NOOM_PRO\program_noom2\program_noom2\microsoft-jdk-21.0.10-windows-x64\jdk-21.0.10+7"; .\mvnw clean install -DskipTests`
2. `cd "D:\VS_CODE_code_claude\DTCore-Queue-Lib"; $env:JAVA_HOME = "C:\NOOM_PRO\program_noom2\program_noom2\microsoft-jdk-21.0.10-windows-x64\jdk-21.0.10+7"; .\mvnw clean install -DskipTests`

Report any build errors clearly.
