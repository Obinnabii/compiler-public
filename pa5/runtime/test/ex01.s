	.text
	.intel_syntax noprefix
	.globl	_Imain_paai
	.type	_Imain_paai, @function
_Imain_paai:
	enter 80, 0
	mov  r8, r9
	mov  r10, 3
	mov [rbp - 40], r8
	mov  r8, r10
	mov [rbp - 48], r9
	mov  r9, 4
	mov [rbp - 56], r10
	mov  r10, r9
	mov [rbp - 16], r8
	mov [rbp - 24], r9
	mov [rbp - 32], r10
	mov rdi, r8
	mov rsi, r10
	call _If_iii
	mov r10, [rbp - 32]
	mov r9, [rbp - 24]
	mov r8, [rbp - 16]
	mov [rbp - 64], r8
	mov [rbp - 72], r9
	mov  r8, r9
	leave
	ret
_If_iii:
	enter 88, 0
	mov  r8, r9
	mov [rbp - 40], r8
	mov  r10, r8
	mov [rbp - 48], r9
	mov [rbp - 56], r10
	mov r10, [rbp - 40]
	mov  r9, r10
	mov [rbp - 64], r8
	mov [rbp - 72], r9
	mov r9, [rbp - 56]
	lea r8, [r9 + r9]
	mov [rbp - 40], r10
	mov  r10, r8
	mov [rbp - 80], r8
	mov  r8, r10
	leave
	ret
