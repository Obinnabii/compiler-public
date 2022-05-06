	.text
	.intel_syntax noprefix
	.globl	_Imain_paai
	.type  _Imain_paai, @function
_Imain_paai:
	enter 40, 0
	push r12
	push r13
	push r14
	mov  r12, 1
	mov  r13, r12
	mov rdi, r13
	call _IunparseInt_aii
	mov rdi, rax
	call _Iprintln_pai
	pop r14
	pop r13
	pop r12
	leave
	ret
