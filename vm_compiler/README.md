Hack assembly code tricks:
1) Pop segment: when we want to pop value from the stack and load it into D register.
Before:
```
@SP
M=M-1
A=M
D=M
```

After:
```
@SP
AM=M-1
D=M
```

2) Rearrange the pseudo code for basic pop segment from

- SP--, 
- addr = segment+i, 
- *addr = *SP 

to 
- addr=segment+i, 
- *addr=*SP--

```
// SP--
@SP,
M=M-1,
// addr = segment + i
seg_addr,
D=M,
a_index,
D=D+A,
@R13,
M=D,
// *addr = *SP
@SP,
A=M,
D=M,
@R13,
A=M,
M=D
```

```
// addr = segment + i
seg_addr,
D=M,
a_index,
D=D+A,
@R13,
M=D,
// *addr = *SP--
@SP,
AM=M-1,
D=M,
@R13,
A=M,
M=D
```


3) Push segment: when we want to push a value onto the stack and increase the stack pointer

Before: 
```
// *SP = *addr
@SP,
A=M,
M=D,

// SP++
@SP,
M=M+1
```

After:

```
// SP++
// *SP-- = *addr
@SP,
AM=M+1,     // increments the stack counter + goes 1 memory address further than we need to push value on to stack
A=A-1       // reduces the value of the current memory address by 1 (undoing the second part of the above comment)
M=D,        // assigns the value in the D register to the correct stack memory location
```