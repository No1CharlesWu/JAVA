ReadMe
代码托管：https://github.com/No1CharlesWu/notify
服务器端已经部署在公网IP了。可以访问
notify
python（服务器端）: notify.py 执行程序 	 notify_client.py 自写测试用客户端
说明：
功能实现简单。绑定本地IP和端口（9999），循环接收一条信息、解析、存储信息并回发客户端。
接收的数据类型是JSON格式的 {'msg':'debug','keyword':6666} 解析keyword，如果不是6666就置之不理，不会保存信息的。。
发送的数据类型是JSON格式的 {‘msg’:msg, ‘level’:msg} 把接收到的msg信息再发回去。

Android（客户端）：
说明：
输入口令：6666，如果不是的话，发出去服务器也会解析抛弃掉信息的。
输入信息：如果输入 debug/info/warning/error/crtical 中的一种，会接收到服务器对应的指令，执行不同的提示，如果是其他信息，则返回debug指令，就无提示反应。
输入好之后，点击button，发送信息。等待接收指令。（多次点击会多次发送信息）
指令响应：
debug: 静默，无反应。
info： 短震动 1秒
warning: 短响铃 2秒
error： 短震动 1秒 + 短响铃 2秒
critical: 长震动 5秒 + 长响铃 10秒 

bug: 口令输入过长会闪退，问题不大。待解决。
