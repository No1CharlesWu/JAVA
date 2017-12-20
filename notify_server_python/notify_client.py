#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# @Time    : 19/12/2017 22:16
# @Author  : Charles
# @File    : notify_client.py
# @Software: PyCharm Community Edition

import socket
import json
if __name__ == '__main__':
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 建立连接:
    s.connect(('127.0.0.1', 9999))
    for d in range(100):
        # 发送数据:
        data = json.dumps({"msg": 'debug', 'keyword':6666})
        s.send(data.encode('utf-8'))
        print(s.recv(1024).decode('utf-8'))
    s.send(b'exit')
    s.close()