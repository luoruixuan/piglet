# piglet
建立符号表：
typecheck的所有内容
每个类的每个成员函数、成员变量的偏移地址
每个局部变量、函数参数的寄存器编号
每个函数用到的所有寄存器编号


Main 初始化：
申请栈空间
栈顶指针归0
Temp 21,22 保存Expression答案寄存器
Temp 23 栈首地址
Temp 24 栈顶指针
为每个类建立指令地址表

数组：
前4位记录长度，从第4位开始记录数据

TODO:
成员变量赋值
