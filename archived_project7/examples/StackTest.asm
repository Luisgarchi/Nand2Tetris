// push constant 17
@17
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 17
@17
D=A
@SP
AM=M+1
A=A-1
M=D
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
@EQ_TRUE_0
D;JEQ
D=0
@EQ_END_0
0;JMP
(EQ_TRUE_0)
D=-1
(EQ_END_0)
@SP
A=M
A=A-1
M=D
// push constant 17
@17
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 16
@16
D=A
@SP
AM=M+1
A=A-1
M=D
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
@EQ_TRUE_1
D;JEQ
D=0
@EQ_END_1
0;JMP
(EQ_TRUE_1)
D=-1
(EQ_END_1)
@SP
A=M
A=A-1
M=D
// push constant 16
@16
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 17
@17
D=A
@SP
AM=M+1
A=A-1
M=D
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
@EQ_TRUE_2
D;JEQ
D=0
@EQ_END_2
0;JMP
(EQ_TRUE_2)
D=-1
(EQ_END_2)
@SP
A=M
A=A-1
M=D
// push constant 892
@892
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 891
@891
D=A
@SP
AM=M+1
A=A-1
M=D
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LT_TRUE_0
D;JLT
D=0
@LT_END_0
0;JMP
(LT_TRUE_0)
D=-1
(LT_END_0)
@SP
A=M
A=A-1
M=D
// push constant 891
@891
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 892
@892
D=A
@SP
AM=M+1
A=A-1
M=D
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LT_TRUE_1
D;JLT
D=0
@LT_END_1
0;JMP
(LT_TRUE_1)
D=-1
(LT_END_1)
@SP
A=M
A=A-1
M=D
// push constant 891
@891
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 891
@891
D=A
@SP
AM=M+1
A=A-1
M=D
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@LT_TRUE_2
D;JLT
D=0
@LT_END_2
0;JMP
(LT_TRUE_2)
D=-1
(LT_END_2)
@SP
A=M
A=A-1
M=D
// push constant 32767
@32767
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 32766
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@GT_TRUE_0
D;JGT
D=0
@GT_END_0
0;JMP
(GT_TRUE_0)
D=-1
(GT_END_0)
@SP
A=M
A=A-1
M=D
// push constant 32766
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 32767
@32767
D=A
@SP
AM=M+1
A=A-1
M=D
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@GT_TRUE_1
D;JGT
D=0
@GT_END_1
0;JMP
(GT_TRUE_1)
D=-1
(GT_END_1)
@SP
A=M
A=A-1
M=D
// push constant 32766
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 32766
@32766
D=A
@SP
AM=M+1
A=A-1
M=D
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@GT_TRUE_2
D;JGT
D=0
@GT_END_2
0;JMP
(GT_TRUE_2)
D=-1
(GT_END_2)
@SP
A=M
A=A-1
M=D
// push constant 57
@57
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 31
@31
D=A
@SP
AM=M+1
A=A-1
M=D
// push constant 53
@53
D=A
@SP
AM=M+1
A=A-1
M=D
// add
@SP
AM=M-1
D=M
A=A-1
M=D+M
// push constant 112
@112
D=A
@SP
AM=M+1
A=A-1
M=D
// sub
@SP
AM=M-1
D=M
A=A-1
M=M-D
// neg
@SP
A=M
A=A-1
M=-M
// and
@SP
AM=M-1
D=M
A=A-1
M=D&M
// push constant 82
@82
D=A
@SP
AM=M+1
A=A-1
M=D
// or
@SP
AM=M-1
D=M
A=A-1
M=D|M
// not
@SP
A=M
A=A-1
M=!M
