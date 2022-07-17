# AppSkin

## 声明

> 该代码仓库及其中的应用资源仅作交流学习用。
> 其中用到的部分图片资源来自《英雄联盟》官网原画下载，如有侵权，请告知删除！

## 使用
> 皮肤插件
> classpath "com.github.Zhupff.AppSkin:plugin:dev-SNAPSHOT"
> apply plugin: zhupff.appskin.SkinPlugin (项目主工程中使用)
> apply plugin: zhupff.appskin.SkinPackagePlugin (皮肤包工程中使用)
> 功能核心 (需要开发者后续自行扩展)
> implementation "com.github.Zhupff.AppSkin:core:dev-SNAPSHOT"
> 功能实现 (包含功能核心，稍微实现并扩展了一点)
> implementation "com.github.Zhupff.AppSkin:impl:dev-SNAPSHOT"
> 更多内容可参考样例工程，后续会逐步完善文档。

## LICENSE

```markdown
Copyright 2021 Zhupff

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```