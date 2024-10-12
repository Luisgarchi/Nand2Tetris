Boolean represenations of values:
TRUE == -1 (all ones, 0xFFFF) and FALSE == 0.
https://www.coursera.org/learn/nand2tetris2/discussions/forums/I9OSxCj3Eea8jw6UvTi2Tw/threads/KuittTKfEeeqRw77WotmkA


Hack assembly optimization tricks:
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
// *(SP-1) = *addr
@SP,
AM=M+1,     // increments the stack counter + goes 1 memory address further than we need to push value on to stack
A=A-1       // reduces the value of the current memory address by 1 (undoing the second part of the above comment)
M=D,        // assigns the value in the D register to the correct stack memory location
```

4) Arthimetic

When we are doing arithmetic with two commands we do not need to do two SP-- operations to get both arguments for the arithmetic command followed by a SP++ to push the result again back on to the stack. We only need to do it once to get the first argument, and then we can subtract the current value of the A-register to get the second argument without changing the stack pointer. We can then perform the arithmetic operation in place i.e. in the current M-register so that desired effect is that the result is pushed on to the stack such that it replaces the second argument. There is no need to increment the stack pointer because it already points to the next available memory address i.e. above the current result.

Before:

```
@SP,      // Pop top
AM=M-1, 
D=M,      // Store top in D
@SP,      // Pop bottom
AM=M-1,   // After this command M will be equal to the bottom value
M=D+M,    // ADD - we add the top value to the bottom
@SP,      // Increment stack pointer
M=M+1
```

After

```
@SP
AM=M-1  // Pop first arg and do SP--
D=M     // Store first arg in D 
A=A-1   // Go to *SP-1 address 
M=D+M   // Get the second arg (M) and add to the first arg (D), store result in M.
```


Single argument

Before:

```
@SP,      // Pop top
AM=M-1,
M=-M,     // NEGATE
@SP,      // Increment stack pointer
M=M+1
```

After:

```
@SP     // Go to top value address
A=M
A=A-1
M=-M    // Inplace operation (negation)
```