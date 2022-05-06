	.text
	.intel_syntax noprefix
	.globl	_Imain_paai
	.type  _Imain_paai, @function
_Imain_paai:
	enter 296, 0
	push r12
	push r13
	push r14
	mov  r12, rdi
	mov  r13, 2
	mov rax, r13
	mov  r14, 8
	imul r14
	mov [rbp - 40], r12
	mov r12, rax
	mov [rbp - 48], r13
	mov r13, r12
	mov [rbp - 56], r14
	mov  r14, 8
	add r13, r14
	mov [rbp - 64], r12
	mov  r12, r13
	mov rdi, r12
	call _xi_alloc
	mov [rbp - 72], r13
	mov  r13, rax
	mov [rbp - 80], r14
	mov  r14, r13
	mov [rbp - 88], r12
	mov r12, r14
	mov [rbp - 96], r13
	mov  r13, 8
	add r12, r13
	mov [rbp - 104], r14
	mov  r14, r12
	mov [rbp - 112], r12
	mov  r12, 1
	mov [r14], r12
	mov [rbp - 120], r13
	mov [rbp - 128], r14
	mov r14, [rbp - 104]
	mov r13, r14
	mov [rbp - 136], r12
	mov  r12, 16
	add r13, r12
	mov [rbp - 144], r13
	mov [rbp - 104], r14
	mov r14, [rbp - 144]
	mov  r13, r14
	mov [rbp - 152], r12
	mov  r12, 2
	mov [r13], r12
	mov [rbp - 160], r13
	mov [rbp - 144], r14
	mov r14, [rbp - 104]
	mov  r13, r14
	mov [rbp - 168], r12
	mov  r12, 2
	mov [r13], r12
	mov [rbp - 176], r13
	mov r13, r14
	mov [rbp - 104], r14
	mov  r14, 8
	add r13, r14
	mov [rbp - 184], r12
	mov  r12, r13
	mov [rbp - 192], r13
	mov  r13, 0
	mov [rbp - 200], r14
	mov  r14, r13
	mov rdi, r12
	mov rsi, r14
	call _If_iaii
	mov [rbp - 208], r12
	mov  r12, rax
	mov [rbp - 216], r13
	mov  r13, 1
	mov [rbp - 224], r14
	mov  r14, r13
	mov [rbp - 232], r12
	mov r12, [rbp - 208]
	mov rdi, r12
	mov rsi, r14
	call _If_iaii
	mov [rbp - 240], r13
	mov  r13, rax
	mov [rbp - 248], r14
	mov [rbp - 208], r12
	mov r12, [rbp - 232]
	mov  r14, r12
	mov rdi, r14
	call _IunparseInt_aii
	mov [rbp - 256], r13
	mov  r13, rax
	mov [rbp - 264], r14
	mov  r14, r13
	mov rdi, r14
	call _Iprintln_pai
	mov [rbp - 232], r12
	mov [rbp - 272], r13
	mov r13, [rbp - 256]
	mov  r12, r13
	mov rdi, r12
	call _IunparseInt_aii
	mov [rbp - 280], r14
	mov  r14, rax
	mov [rbp - 288], r12
	mov  r12, r14
	mov rdi, r12
	call _Iprintln_pai
	pop r14
	pop r13
	pop r12
	leave
	ret
_Ig_pi:
	enter 48, 0
	push r12
	push r13
	push r14
	mov  r12, rdi
	mov  r13, r12
	mov rdi, r13
	call _IunparseInt_aii
	mov  r14, rax
	mov [rbp - 40], r12
	mov  r12, r14
	mov rdi, r12
	call _Iprintln_pai
	pop r14
	pop r13
	pop r12
	leave
	ret
_If_iaii:
	enter 1184, 0
	push r12
	push r13
	push r14
	mov  r12, rdi
	mov  r13, rsi
	mov  r14, 7
	mov rax, r14
	mov [rbp - 40], r12
	mov  r12, 8
	imul r12
	mov [rbp - 48], r13
	mov r13, rax
	mov [rbp - 56], r14
	mov r14, r13
	mov [rbp - 64], r12
	mov  r12, 8
	add r14, r12
	mov [rbp - 72], r13
	mov  r13, r14
	mov rdi, r13
	call _xi_alloc
	mov [rbp - 80], r14
	mov  r14, rax
	mov [rbp - 88], r12
	mov  r12, r14
	mov [rbp - 96], r13
	mov  r13, r12
	mov [rbp - 104], r14
	mov  r14, 7
	mov [r13], r14
	mov [rbp - 112], r12
	mov [rbp - 120], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 128], r14
	mov  r14, 8
	add r12, r14
	mov [rbp - 136], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 136]
	mov  r12, r13
	mov [rbp - 144], r14
	mov  r14, 73
	mov [r12], r14
	mov [rbp - 152], r12
	mov [rbp - 136], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 160], r14
	mov  r14, 16
	add r12, r14
	mov [rbp - 168], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 168]
	mov  r12, r13
	mov [rbp - 176], r14
	mov  r14, 110
	mov [r12], r14
	mov [rbp - 184], r12
	mov [rbp - 168], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 192], r14
	mov  r14, 24
	add r12, r14
	mov [rbp - 200], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 200]
	mov  r12, r13
	mov [rbp - 208], r14
	mov  r14, 100
	mov [r12], r14
	mov [rbp - 216], r12
	mov [rbp - 200], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 224], r14
	mov  r14, 32
	add r12, r14
	mov [rbp - 232], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 232]
	mov  r12, r13
	mov [rbp - 240], r14
	mov  r14, 101
	mov [r12], r14
	mov [rbp - 248], r12
	mov [rbp - 232], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 256], r14
	mov  r14, 40
	add r12, r14
	mov [rbp - 264], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 264]
	mov  r12, r13
	mov [rbp - 272], r14
	mov  r14, 120
	mov [r12], r14
	mov [rbp - 280], r12
	mov [rbp - 264], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 288], r14
	mov  r14, 48
	add r12, r14
	mov [rbp - 296], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 296]
	mov  r12, r13
	mov [rbp - 304], r14
	mov  r14, 58
	mov [r12], r14
	mov [rbp - 312], r12
	mov [rbp - 296], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 320], r14
	mov  r14, 56
	add r12, r14
	mov [rbp - 328], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 328]
	mov  r12, r13
	mov [rbp - 336], r14
	mov  r14, 32
	mov [r12], r14
	mov [rbp - 344], r12
	mov [rbp - 328], r13
	mov r13, [rbp - 112]
	mov r12, r13
	mov [rbp - 352], r14
	mov  r14, 8
	add r12, r14
	mov [rbp - 360], r12
	mov [rbp - 112], r13
	mov r13, [rbp - 360]
	mov  r12, r13
	mov rdi, r12
	call _Iprint_pai
	mov [rbp - 368], r14
	mov [rbp - 376], r12
	mov r12, [rbp - 48]
	mov  r14, r12
	mov rdi, r14
	call _IunparseInt_aii
	mov [rbp - 360], r13
	mov  r13, rax
	mov [rbp - 384], r14
	mov  r14, r13
	mov rdi, r14
	call _Iprintln_pai
	mov [rbp - 48], r12
	mov [rbp - 392], r13
	mov r13, [rbp - 40]
	mov  r12, r13
	mov [rbp - 400], r14
	mov [rbp - 408], r12
	mov r12, [rbp - 48]
	mov  r14, r12
	mov [rbp - 40], r13
	mov  r13, 1
	mov [rbp - 416], r14
	mov r14, r13
	mov [rbp - 48], r12
	mov [rbp - 424], r13
	mov r13, [rbp - 416]
	mov r12, r13
	mov [rbp - 432], r14
	mov [rbp - 440], r12
	mov r12, [rbp - 408]
	mov r14, r12
	mov [rbp - 416], r13
	mov  r13, 8
	sub r14, r13
	mov [rbp - 448], r14
	mov [rbp - 408], r12
	mov r12, [rbp - 448]
	mov r14, [r12]
	mov [rbp - 456], r13
	mov r13, [rbp - 440]
	cmp r13, r14
	setb  r13b
	and r13b, 1
	movzx r13, r13b
	mov [rbp - 464], r14
	mov r14, [rbp - 432]
	xor r14, r13
	mov [rbp - 448], r12
	mov  r12, 1
	cmp r14, r12
	mov [rbp - 440], r13
	mov [rbp - 432], r14
	mov [rbp - 472], r12
	jge err_7 # switch default
true_6:
	mov r14, [rbp - 408]
	mov r13, r14
	mov r12, [rbp - 416]
	mov rax, r12
	mov [rbp - 480], r13
	mov  r13, 8
	imul r13
	mov [rbp - 408], r14
	mov r14, rax
	mov [rbp - 416], r12
	mov r12, [rbp - 480]
	add r12, r14
	mov [rbp - 488], r13
	mov  r13, r12
	mov [rbp - 496], r14
	mov [rbp - 480], r12
	mov r12, [rbp - 40]
	mov  r14, r12
	mov [rbp - 504], r13
	mov [rbp - 512], r14
	mov r14, [rbp - 48]
	mov  r13, r14
	mov [rbp - 40], r12
	mov [rbp - 520], r13
	mov r13, [rbp - 512]
	mov r12, r13
	mov [rbp - 48], r14
	mov  r14, 8
	sub r12, r14
	mov [rbp - 528], r12
	mov [rbp - 512], r13
	mov r13, [rbp - 528]
	mov r12, [r13]
	mov [rbp - 536], r14
	mov r14, [rbp - 520]
	cmp r14, r12
	mov [rbp - 544], r12
	mov [rbp - 528], r13
	mov [rbp - 520], r14
	jb true_0
	jmp err_1
err_7:
	mov  r12, 0
	mov  r13, r12
	mov rdi, r13
	call _xi_out_of_bounds
	mov  r14, rax
	mov [rbp - 552], r12
	mov [rbp - 560], r13
	mov [rbp - 568], r14
	jmp true_6
true_0:
	mov r13, [rbp - 512]
	mov r12, r13
	mov r14, [rbp - 520]
	mov rax, r14
	mov [rbp - 576], r12
	mov  r12, 8
	imul r12
	mov [rbp - 512], r13
	mov r13, rax
	mov [rbp - 520], r14
	mov r14, [rbp - 576]
	add r14, r13
	mov [rbp - 584], r12
	mov r12, [r14]
	mov [rbp - 592], r13
	mov r13, r12
	mov [rbp - 576], r14
	mov  r14, 1
	add r13, r14
	mov [rbp - 600], r12
	mov r12, [rbp - 504]
	mov [r12], r13
	mov [rbp - 608], r13
	mov  r13, 7
	mov rax, r13
	mov [rbp - 616], r14
	mov  r14, 8
	imul r14
	mov [rbp - 504], r12
	mov r12, rax
	mov [rbp - 624], r13
	mov r13, r12
	mov [rbp - 632], r14
	mov  r14, 8
	add r13, r14
	mov [rbp - 640], r12
	mov  r12, r13
	mov rdi, r12
	call _xi_alloc
	mov [rbp - 648], r13
	mov  r13, rax
	mov [rbp - 656], r14
	mov  r14, r13
	mov [rbp - 664], r12
	mov  r12, r14
	mov [rbp - 672], r13
	mov  r13, 7
	mov [r12], r13
	mov [rbp - 680], r14
	mov [rbp - 688], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 696], r13
	mov  r13, 8
	add r14, r13
	mov [rbp - 704], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 704]
	mov  r14, r12
	mov [rbp - 712], r13
	mov  r13, 86
	mov [r14], r13
	mov [rbp - 720], r14
	mov [rbp - 704], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 728], r13
	mov  r13, 16
	add r14, r13
	mov [rbp - 736], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 736]
	mov  r14, r12
	mov [rbp - 744], r13
	mov  r13, 97
	mov [r14], r13
	mov [rbp - 752], r14
	mov [rbp - 736], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 760], r13
	mov  r13, 24
	add r14, r13
	mov [rbp - 768], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 768]
	mov  r14, r12
	mov [rbp - 776], r13
	mov  r13, 108
	mov [r14], r13
	mov [rbp - 784], r14
	mov [rbp - 768], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 792], r13
	mov  r13, 32
	add r14, r13
	mov [rbp - 800], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 800]
	mov  r14, r12
	mov [rbp - 808], r13
	mov  r13, 117
	mov [r14], r13
	mov [rbp - 816], r14
	mov [rbp - 800], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 824], r13
	mov  r13, 40
	add r14, r13
	mov [rbp - 832], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 832]
	mov  r14, r12
	mov [rbp - 840], r13
	mov  r13, 101
	mov [r14], r13
	mov [rbp - 848], r14
	mov [rbp - 832], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 856], r13
	mov  r13, 48
	add r14, r13
	mov [rbp - 864], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 864]
	mov  r14, r12
	mov [rbp - 872], r13
	mov  r13, 58
	mov [r14], r13
	mov [rbp - 880], r14
	mov [rbp - 864], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 888], r13
	mov  r13, 56
	add r14, r13
	mov [rbp - 896], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 896]
	mov  r14, r12
	mov [rbp - 904], r13
	mov  r13, 32
	mov [r14], r13
	mov [rbp - 912], r14
	mov [rbp - 896], r12
	mov r12, [rbp - 680]
	mov r14, r12
	mov [rbp - 920], r13
	mov  r13, 8
	add r14, r13
	mov [rbp - 928], r14
	mov [rbp - 680], r12
	mov r12, [rbp - 928]
	mov  r14, r12
	mov rdi, r14
	call _Iprint_pai
	mov [rbp - 936], r13
	mov [rbp - 944], r14
	mov r14, [rbp - 40]
	mov  r13, r14
	mov [rbp - 928], r12
	mov [rbp - 952], r13
	mov r13, [rbp - 48]
	mov  r12, r13
	mov [rbp - 40], r14
	mov [rbp - 960], r12
	mov r12, [rbp - 952]
	mov r14, r12
	mov [rbp - 48], r13
	mov  r13, 8
	sub r14, r13
	mov [rbp - 968], r14
	mov [rbp - 952], r12
	mov r12, [rbp - 968]
	mov r14, [r12]
	mov [rbp - 976], r13
	mov r13, [rbp - 960]
	cmp r13, r14
	mov [rbp - 984], r14
	mov [rbp - 968], r12
	mov [rbp - 960], r13
	jb true_8
	jmp err_9
err_1:
	mov  r14, 0
	mov  r12, r14
	mov rdi, r12
	call _xi_out_of_bounds
	mov  r13, rax
	mov [rbp - 992], r14
	mov [rbp - 1000], r12
	mov [rbp - 1008], r13
	jmp true_0
true_8:
	mov r12, [rbp - 952]
	mov r14, r12
	mov r13, [rbp - 960]
	mov rax, r13
	mov [rbp - 1016], r14
	mov  r14, 8
	imul r14
	mov [rbp - 952], r12
	mov r12, rax
	mov [rbp - 960], r13
	mov r13, [rbp - 1016]
	add r13, r12
	mov [rbp - 1024], r14
	mov r14, [r13]
	mov rdi, r14
	call _IunparseInt_aii
	mov [rbp - 1032], r12
	mov  r12, rax
	mov [rbp - 1016], r13
	mov  r13, r12
	mov rdi, r13
	call _Iprintln_pai
	mov [rbp - 1040], r14
	mov [rbp - 1048], r12
	mov r12, [rbp - 40]
	mov  r14, r12
	mov [rbp - 1056], r13
	mov [rbp - 1064], r14
	mov r14, [rbp - 48]
	mov  r13, r14
	mov [rbp - 40], r12
	mov [rbp - 1072], r13
	mov r13, [rbp - 1064]
	mov r12, r13
	mov [rbp - 48], r14
	mov  r14, 8
	sub r12, r14
	mov [rbp - 1080], r12
	mov [rbp - 1064], r13
	mov r13, [rbp - 1080]
	mov r12, [r13]
	mov [rbp - 1088], r14
	mov r14, [rbp - 1072]
	cmp r14, r12
	mov [rbp - 1096], r12
	mov [rbp - 1080], r13
	mov [rbp - 1072], r14
	jb true_12
	jmp err_13
err_9:
	mov  r12, 0
	mov  r13, r12
	mov rdi, r13
	call _xi_out_of_bounds
	mov  r14, rax
	mov [rbp - 1104], r12
	mov [rbp - 1112], r13
	mov [rbp - 1120], r14
	jmp true_8
true_12:
	mov r13, [rbp - 1064]
	mov r12, r13
	mov r14, [rbp - 1072]
	mov rax, r14
	mov [rbp - 1128], r12
	mov  r12, 8
	imul r12
	mov [rbp - 1064], r13
	mov r13, rax
	mov [rbp - 1072], r14
	mov r14, [rbp - 1128]
	add r14, r13
	mov [rbp - 1136], r12
	mov r12, [r14]
	mov  rax, r12
	pop r14
	pop r13
	pop r12
	leave
	ret
	mov [rbp - 1144], r13
	mov [rbp - 1128], r14
	mov [rbp - 1152], r12
err_13:
	mov  r13, 0
	mov  r14, r13
	mov rdi, r14
	call _xi_out_of_bounds
	mov  r12, rax
	mov [rbp - 1160], r13
	mov [rbp - 1168], r14
	mov [rbp - 1176], r12
	jmp true_12
