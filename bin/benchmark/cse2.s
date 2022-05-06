	.text
	.intel_syntax noprefix
	.globl	_Imain_paai
	.type  _Imain_paai, @function
_Imain_paai:
	enter 64, 0
	push r12
	push r13
	push r14
	call _If_i
	call _IaBool_b
	mov  r12, 1
	mov r13, r12
	xor r13, rax
	mov  r14, 1
	cmp r13, r14
	mov [rbp - 40], r12
	mov [rbp - 48], r13
	mov [rbp - 56], r14
	jge false_5 # switch default
true_4:
false_5:
	pop r14
	pop r13
	pop r12
	leave
	ret
_IaBool_b:
	enter 40, 0
	push r12
	push r13
	push r14
	mov  r12, 1
	mov  r13, r12
	mov  rax, r13
	pop r14
	pop r13
	pop r12
	leave
	ret
_If_i:
	enter 304, 0
	push r12
	push r13
	push r14
	mov  r12, 32
	mov  r13, r12
	mov rdi, r13
	call _xi_alloc
	mov  r14, rax
	mov [rbp - 40], r12
	mov r12, rax
	mov [rbp - 48], r13
	mov  r13, 8
	add r12, r13
	mov [rbp - 56], r14
	mov  r14, r12
	mov [rbp - 64], r12
	mov  r12, 1
	mov [r14], r12
	mov [rbp - 72], r13
	mov [rbp - 80], r14
	mov r14, [rbp - 56]
	mov r13, r14
	mov [rbp - 88], r12
	mov  r12, 16
	add r13, r12
	mov [rbp - 96], r13
	mov [rbp - 56], r14
	mov r14, [rbp - 96]
	mov  r13, r14
	mov [rbp - 104], r12
	mov  r12, 2
	mov [r13], r12
	mov [rbp - 112], r13
	mov [rbp - 96], r14
	mov r14, [rbp - 56]
	mov r13, r14
	mov [rbp - 120], r12
	mov  r12, 24
	add r13, r12
	mov [rbp - 128], r13
	mov [rbp - 56], r14
	mov r14, [rbp - 128]
	mov  r13, r14
	mov [rbp - 136], r12
	mov  r12, 3
	mov [r13], r12
	mov [rbp - 144], r13
	mov  r13, 3
	mov [rbp - 128], r14
	mov r14, [rbp - 56]
	mov [r14], r13
	mov [rbp - 152], r12
	mov  r12, 2
	mov [rbp - 160], r13
	mov  r13, r12
	mov [rbp - 56], r14
	mov [rbp - 168], r12
	mov r12, [rbp - 80]
	mov r14, r12
	mov [rbp - 176], r13
	mov  r13, 8
	sub r14, r13
	mov [rbp - 184], r14
	mov [rbp - 80], r12
	mov r12, [rbp - 184]
	mov  r14, r12
	mov [rbp - 192], r13
	mov  r13, 1
	mov [rbp - 200], r14
	mov r14, r13
	mov [rbp - 184], r12
	mov [rbp - 208], r13
	mov r13, [rbp - 176]
	mov r12, r13
	mov [rbp - 216], r14
	mov [rbp - 224], r12
	mov r12, [rbp - 200]
	mov r14, [r12]
	mov [rbp - 176], r13
	mov r13, [rbp - 224]
	cmp r13, r14
	setb  r13b
	and r13b, 1
	movzx r13, r13b
	mov [rbp - 232], r14
	mov r14, [rbp - 216]
	xor r14, r13
	mov [rbp - 200], r12
	mov  r12, 1
	cmp r14, r12
	mov [rbp - 224], r13
	mov [rbp - 216], r14
	mov [rbp - 240], r12
	jge err_1 # switch default
true_0:
	mov r13, [rbp - 176]
	mov rax, r13
	mov  r14, 8
	imul r14
	mov r12, rax
	mov [rbp - 176], r13
	mov  r13, r12
	mov [rbp - 248], r14
	mov [rbp - 256], r12
	mov r12, [rbp - 80]
	mov r14, r12
	add r14, r13
	mov [rbp - 264], r13
	mov r13, [r14]
	mov  rax, r13
	pop r14
	pop r13
	pop r12
	leave
	ret
	mov [rbp - 272], r14
	mov [rbp - 80], r12
	mov [rbp - 280], r13
err_1:
	mov  r14, 0
	mov  r12, r14
	mov rdi, r12
	call _xi_out_of_bounds
	mov [rbp - 288], r14
	mov [rbp - 296], r12
	jmp true_0
