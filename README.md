# 简介

# 组件列表

## JButton
可以设置颜色,边框,字体大小,图标的按钮,避免使用drawable资源文件

|属性|描述|备注|
|---|---|---|
|jButtonTitle| 标题|
|jButtonIcon|图标|和TextView不同，图标会紧邻字体居中
|jButtonRadius|圆角|
|jButtonTextSize|字体大小
|jButtonColor|颜色
|jButtonTextColor|字体颜色
|jButtonStyle|内置样式|fill(背景着色, 字体白色)<br>stroke(边框和字体着色)
|jButtonSize|内置大小|normal(用于底部大按钮)<br>little(用于二级按钮)

`jButtonStyle`, `jButtonSize`的是可以被覆盖重定义的

## Gallery
* 用于图片展示
* 可以添加删除图片
* 网格排列图片,网格列数可以改变
* 动态改变图片编辑\浏览模式

|属性|描述|备注|
|---|---|---|
|galleryPadding|图片边距|
|galleryRowNumber|网格列数|
|galleryAddDrawable|添加按钮的图片
|galleryAddView|用布局作为添加按钮
|galleryDeleteDrawable|删除按钮的图片
|galleryDeleteButtonMargin|删除按钮距离图片的位置
|galleryPlaceholderDrawable|预加载图片
|galleryAddMode|添加模式|启动后显示添加图片按钮
|galleryDeleteMode|删除模式|启动后所有图片的左上角出现删除按钮
|galleryPictureMax|最大图片数目|在编辑模式可以限制用户添加图片数量

## GridRadioGroup

# 工具类

## FragmentUtils
