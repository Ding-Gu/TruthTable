# TruthTable
计算命题公式的算法

使用方法：
1.代码里面的Stack一起在仓库里，是根据algs4写的泛型栈，没用系统自带的是因为当时没用过
2.命题公式用字符串表示，如"(!A*B)+(A*!B)"，计算结果用一个一维boolean数组表示
3.命题公式仅支持*'*','+','!'，即“与”“或”“非”三个运算符号*
4.命题公式仅支持使用单个大写字母作为输入变量
5.调用实例方法getValue()获得计算结果
6.调用实例方法printTruthTable()打印命题公式的真值表

注意：
1.不能进行连续进行两次非运算，如：!!A,!(!A)
2.不能输入本身就是非法的命题公式字符串，如：((A+B),A++B,A!+B
3.并没有做检测命题公式字符串是否合法，非法输入可能会获得一个错误的结果，但没用报错

闲得无聊做着玩的，对本人没什么实际用处
