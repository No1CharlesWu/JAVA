#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# @Time    : 19/12/2017 20:58
# @Author  : Charles
# @File    : notify.py
# @Software: PyCharm Community Edition

import json
import time
import threading
import socket
from collections import deque


class NotifyAgent(object):
    app_mes_que = deque()
    level_list = ['debug','info','warning','error','critical']
    msg_list = ['debug','info','warning','error','critical']
    socket = None
    KEYWORDS = 6666

    def __init__(self):
        self.socket_init()

    def socket_init(self):
        try:
            # 创建一个socket:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # 监听端口:
            # self.socket.bind(('127.0.0.1', 9999))
            # self.socket.bind(('10.70.3.30', 9999))
            
            self.socket.bind(('0.0.0.0', 9999))
            self.socket.listen(10)
            print('Waiting for connection...')
            while True:
                # 接受一个新连接:
                sock, address = self.socket.accept()
                # 创建新线程来处理TCP连接:
                t = threading.Thread(target=self.task, args=(sock, address))
                t.start()
        except Exception as e:
            print('socket_init' + str(e))
        finally:
            self.socket.close()

    def task(self, sock, address):
        try:
            print('Accept new connection from %s:%s...' % address)
            while True:
                print('get_msg')
                data = self.get_msg(sock, address)
                if data['msg'] == 'exit' or data['keyword'] != self.KEYWORDS:
                    break
                print('send_msg')
                time.sleep(1)
                if data['msg'] not in self.level_list:
                    self.send_msg(sock, address, 'debug', data['msg'])
                else:
                    self.send_msg(sock, address, data['msg'], data['msg'])
            sock.close()
            print('Connection from %s:%s closed.' % address)
        except Exception as e:
            print(e)
        finally:
            sock.close()

    def send_msg(self, sock, address, level, msg):
        data = json.dumps({"level": level, "msg": msg})
        sock.send(data.encode('utf-8'))
        print('send_msg: ' + data)

    # 接收data为json的dict{'msg':'debug','keyword':6666}
    # msg 内容必须为 debug/info/warning/error/critical
    # keyword 内容必须为 6666
    def get_msg(self, sock, address):
        # 接收数据:
        buffer = []
        # 每次最多接收1k字节:
        data = sock.recv(1024)

        print('get_msg: ' + data.decode('utf-8'))
        time.sleep(1)
        data = json.loads(data.decode('utf-8'))
        if (data['msg'] == 'exit') or (data['keyword'] != self.KEYWORDS):
            return data
        print(data)
        self.save_msg(data['msg'])
        return data

    def save_msg(self, msg):
        print('save_msg:  ' + msg)
        self.app_mes_que.append(msg)


if __name__ == '__main__':
    notify_agent = NotifyAgent()

