# ProjectDemo
## 一、使用教程

本APP可以用在华为设备与非华为设备上。

1. 打开应用商店，搜索==HMS Core==并下载安装(HMS Core是一个插件，并没有应用图标，但可以在应用管理中看到他)；
2. 为HMS Core打开定位权限，切记选择==始终允许==；
3. 安装APP，同意对应的权限允许，并在应用管理中将其定位权限手动改为==始终允许==，并开启==通知权限==；
4. 打开APP即可正常使用日记、待办、账单等功能，其中日记部分添加的视频与音频一旦添加后，如果音频与视频在本地移动了位置或者删除了，APP内边不会再日记中访问到。

## 二、关键类

### 1. 页面构成

Android程序中构成页面的相关类如下：

1. MainActivity：主页面，其主要负责展示以下四个Fragment
   * JournalFragment：负责*“心情日记“*相关功能
     * JourFragment：用来展示所有发表过的日记的列表
     * AlbumFragment：用来展示日记图片所生成的相册
     * AlbumPictureFragment：用来展示某个日记相册中所有图片的页面，**通过点击AlbumFragment某个相册跳转过来**
   * TodoFragment：负责待办事项相关的功能
     * AddDialogFragment：弹窗，负责完成待办事项的添加，**通过点击TodoFragment上的添加按钮显示**。
     * UpdateDialogFragment：弹窗，负责完成待办事项的修改，**通过点击TodoFragment某个待办项弹出**。
     * AnalyseFragment：分析页面，负责直观展示待办事项的完成度，**通过点击TodoFragment右上角的按钮跳转**。
   * AccountFragment：负责账单相关功能
   * InfoFragment：负责个人信息相关功能
2. JournalEditActivity：负责“写日记”所涉及到的功能，**通过点击JournalFragment右上角的添加按钮跳转过来**
   * AddFragment：*“写日记”*的页面，负责添加日记；**通过点击JournalFragment右上角的添加按钮跳转过来**。
   * ImagePreviewFragment：在编辑日记时，点击某个所选中的图片来进行预览或者删除本张图片，**通过点击AddFragment中某张添加的图片跳转过来**。
   * VideoPreviewFragment：同上，只不过是对所选择的视频进行预览。
   * FilterFragment：为所添加的图片进行滤镜处理，**由ImagePreviewFragment中点击滤镜按钮跳转过来**。
3. JournalItemDetailActivity：负责展示某个日记的详情所涉及到的功能，**通过点击某个日记跳转过来**
   * JournalItemDetailFragment：负责展示某个日记的详情所涉及到的功能，**通过点击某个日记跳转过来**。
   * ImageShowFragment：如果日记中添加了图片的话，该Fragment负责查看图片大图，**通过点击JournalItemDetailFragment中的某张图片跳转过来**。
4. JournalVideoActivity：负责播放视频的页面，**通过点击JournalItemDetailFragment中的视频跳转过来**。
