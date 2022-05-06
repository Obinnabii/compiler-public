	.file	"arr.c"
	.intel_syntax noprefix
	.text
	.globl	_Imain_paai
	.type	_Imain_paai, @function
_Imain_paai:
.LFB0:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	sub	rsp, 32
	mov	QWORD PTR -24[rbp], rdi
	mov	rax, QWORD PTR -24[rbp]
	mov	rax, QWORD PTR -8[rax]
	mov	QWORD PTR -16[rbp], rax
	mov	QWORD PTR -8[rbp], 0
	jmp	.L2
.L3:
	mov	rax, QWORD PTR -8[rbp]
	lea	rdx, 0[0+rax*8]
	mov	rax, QWORD PTR -24[rbp]
	add	rax, rdx
	mov	rax, QWORD PTR [rax]
	mov	rdi, rax
	call	_Iprintln_pai@PLT
	add	QWORD PTR -8[rbp], 1
.L2:
	mov	rax, QWORD PTR -8[rbp]
	cmp	rax, QWORD PTR -16[rbp]
	jl	.L3
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	_Imain_paai, .-_Imain_paai
	.globl	_Iyerr_paai
	.type	_Iyerr_paai, @function
_Iyerr_paai:
.LFB1:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	sub	rsp, 32
	mov	QWORD PTR -24[rbp], rdi
	mov	rax, QWORD PTR -24[rbp]
	mov	rax, QWORD PTR -8[rax]
	mov	QWORD PTR -16[rbp], rax
	mov	QWORD PTR -8[rbp], 0
	jmp	.L5
.L6:
	mov	rax, QWORD PTR -8[rbp]
	lea	rdx, 0[0+rax*8]
	mov	rax, QWORD PTR -24[rbp]
	add	rax, rdx
	mov	rax, QWORD PTR [rax]
	mov	rdi, rax
	call	_Iprintln_pai@PLT
	add	QWORD PTR -8[rbp], 1
.L5:
	mov	rax, QWORD PTR -8[rbp]
	cmp	rax, QWORD PTR -16[rbp]
	jl	.L6
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE1:
	.size	_Iyerr_paai, .-_Iyerr_paai
	.ident	"GCC: (Ubuntu 7.5.0-3ubuntu1~18.04) 7.5.0"
	.section	.note.GNU-stack,"",@progbits
