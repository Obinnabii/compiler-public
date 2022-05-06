	.text
	.intel_syntax noprefix
	.globl	_Imain_paai
	.type  _Imain_paai, @function
_Imain_paai:
	enter 448, 0
	push r12
	push r13
	push r14
	mov  r12, 1
	mov  r13, r12
	mov r14, r13
	mov [rbp - 40], r12
	mov  r12, 1
	add r14, r12
	mov [rbp - 48], r13
	mov  r13, r14
	mov [rbp - 56], r14
	mov  r14, r13
	mov [rbp - 64], r12
	mov  r12, r13
	mov [rbp - 72], r13
	mov r13, r12
	add r13, r14
	mov [rbp - 80], r14
	mov  r14, r13
	mov [rbp - 88], r12
	mov  r12, r14
	mov [rbp - 96], r13
	mov [rbp - 104], r14
	mov r14, [rbp - 48]
	mov r13, r14
	mov [rbp - 112], r12
	mov  r12, 10
	add r13, r12
	mov [rbp - 120], r13
	mov [rbp - 48], r14
	mov r14, [rbp - 120]
	mov  r13, r14
	mov [rbp - 128], r12
	mov  r12, r13
	mov [rbp - 136], r13
	mov r13, r12
	mov [rbp - 120], r14
	mov r14, [rbp - 80]
	add r13, r14
	mov [rbp - 144], r12
	mov  r12, r13
	mov [rbp - 152], r13
	mov r13, r12
	mov [rbp - 80], r14
	mov  r14, 3
	add r13, r14
	mov [rbp - 160], r12
	mov  r12, r13
	mov [rbp - 168], r13
	mov r13, r12
	mov [rbp - 176], r14
	mov r14, [rbp - 112]
	add r13, r14
	mov [rbp - 184], r12
	mov  r12, r13
	mov [rbp - 192], r13
	mov r13, r12
	mov [rbp - 112], r14
	mov  r14, 5
	add r13, r14
	mov [rbp - 200], r12
	mov  r12, r13
	mov [rbp - 208], r13
	mov [rbp - 216], r14
	mov r14, [rbp - 136]
	mov  r13, r14
	mov [rbp - 224], r12
	mov r12, r13
	mov [rbp - 232], r13
	mov r13, [rbp - 80]
	add r12, r13
	mov [rbp - 136], r14
	mov  r14, r12
	mov [rbp - 240], r12
	mov r12, r14
	mov [rbp - 80], r13
	mov  r13, 3
	add r12, r13
	mov [rbp - 248], r14
	mov  r14, r12
	mov [rbp - 256], r12
	mov r12, r14
	mov [rbp - 264], r13
	mov r13, [rbp - 112]
	add r12, r13
	mov [rbp - 272], r14
	mov  r14, r12
	mov [rbp - 280], r12
	mov r12, r14
	mov [rbp - 112], r13
	mov  r13, 5
	add r12, r13
	mov [rbp - 288], r14
	mov  r14, r12
	mov [rbp - 296], r12
	mov [rbp - 304], r13
	mov r13, [rbp - 136]
	mov  r12, r13
	mov [rbp - 312], r14
	mov r14, r12
	mov [rbp - 320], r12
	mov r12, [rbp - 80]
	add r14, r12
	mov [rbp - 136], r13
	mov  r13, r14
	mov [rbp - 328], r14
	mov r14, r13
	mov [rbp - 80], r12
	mov  r12, 3
	add r14, r12
	mov [rbp - 336], r13
	mov  r13, r14
	mov [rbp - 344], r14
	mov r14, r13
	mov [rbp - 352], r12
	mov r12, [rbp - 112]
	add r14, r12
	mov [rbp - 360], r13
	mov  r13, r14
	mov [rbp - 368], r14
	mov r14, r13
	mov [rbp - 112], r12
	mov  r12, 55
	add r14, r12
	mov [rbp - 376], r13
	mov  r13, r14
	mov [rbp - 384], r14
	mov [rbp - 392], r12
	mov r12, [rbp - 224]
	mov r14, r12
	mov [rbp - 400], r13
	mov r13, [rbp - 312]
	add r14, r13
	mov [rbp - 408], r14
	mov [rbp - 224], r12
	mov r12, [rbp - 408]
	mov  r14, r12
	mov [rbp - 312], r13
	mov  r13, r14
	mov [rbp - 416], r14
	mov r14, r13
	mov [rbp - 408], r12
	mov r12, [rbp - 400]
	add r14, r12
	mov [rbp - 424], r13
	mov  r13, r14
	mov [rbp - 432], r14
	mov  r14, r13
	mov rdi, r14
	call _IunparseInt_aii
	mov [rbp - 400], r12
	mov  r12, rax
	mov [rbp - 440], r13
	mov  r13, r12
	mov rdi, r13
	call _Iprintln_pai
	pop r14
	pop r13
	pop r12
	leave
	ret
