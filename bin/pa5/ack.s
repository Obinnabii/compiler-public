	.text
	.intel_syntax noprefix
	.globl	_Imain_paai
	.type  _Imain_paai, @function
_Imain_paai:
	enter 936, 0
	push r12
	push r13
	push r14
	mov  r12, rdi
	mov  r13, 11
	mov  r14, r13
	mov [rbp - 40], r12
	mov  r12, 2
	mov [rbp - 48], r13
	mov  r13, r12
	mov rdi, r13
	mov rsi, r14
	call _IAck_iii
	mov [rbp - 56], r14
	mov  r14, rax
	mov [rbp - 64], r12
	mov  r12, 6
	mov rax, r12
	mov [rbp - 72], r13
	mov  r13, 8
	imul r13
	mov [rbp - 80], r14
	mov r14, rax
	mov [rbp - 88], r12
	mov r12, r14
	mov [rbp - 96], r13
	mov  r13, 8
	add r12, r13
	mov [rbp - 104], r14
	mov  r14, r12
	mov [rbp - 112], r12
	mov  r12, 6
	mov rax, r12
	mov [rbp - 120], r13
	mov  r13, 8
	imul r13
	mov [rbp - 128], r14
	mov r14, rax
	mov [rbp - 136], r12
	mov  r12, r14
	mov [rbp - 144], r13
	mov r13, r12
	mov [rbp - 152], r14
	mov  r14, 8
	add r13, r14
	mov [rbp - 160], r12
	mov  r12, r13
	mov rdi, r12
	call _xi_alloc
	mov [rbp - 168], r13
	mov  r13, rax
	mov [rbp - 176], r14
	mov  r14, r13
	mov [rbp - 184], r12
	mov  r12, r14
	mov [rbp - 192], r13
	mov  r13, 6
	mov [r12], r13
	mov [rbp - 200], r14
	mov [rbp - 208], r12
	mov r12, [rbp - 200]
	mov r14, r12
	mov [rbp - 216], r13
	mov  r13, 8
	add r14, r13
	mov [rbp - 224], r14
	mov [rbp - 200], r12
	mov r12, [rbp - 224]
	mov  r14, r12
	mov [rbp - 232], r13
	mov  r13, r14
	mov [rbp - 240], r14
	mov  r14, 65
	mov [r13], r14
	mov [rbp - 224], r12
	mov [rbp - 248], r13
	mov r13, [rbp - 200]
	mov r12, r13
	mov [rbp - 256], r14
	mov  r14, 16
	add r12, r14
	mov [rbp - 264], r12
	mov [rbp - 200], r13
	mov r13, [rbp - 264]
	mov  r12, r13
	mov [rbp - 272], r14
	mov  r14, r12
	mov [rbp - 280], r12
	mov  r12, 99
	mov [r14], r12
	mov [rbp - 264], r13
	mov [rbp - 288], r14
	mov r14, [rbp - 200]
	mov r13, r14
	mov [rbp - 296], r12
	mov  r12, 24
	add r13, r12
	mov [rbp - 304], r13
	mov [rbp - 200], r14
	mov r14, [rbp - 304]
	mov  r13, r14
	mov [rbp - 312], r12
	mov  r12, r13
	mov [rbp - 320], r13
	mov  r13, 107
	mov [r12], r13
	mov [rbp - 304], r14
	mov [rbp - 328], r12
	mov r12, [rbp - 200]
	mov r14, r12
	mov [rbp - 336], r13
	mov  r13, 32
	add r14, r13
	mov [rbp - 344], r14
	mov [rbp - 200], r12
	mov r12, [rbp - 344]
	mov  r14, r12
	mov [rbp - 352], r13
	mov  r13, r14
	mov [rbp - 360], r14
	mov  r14, 40
	mov [r13], r14
	mov [rbp - 344], r12
	mov [rbp - 368], r13
	mov r13, [rbp - 200]
	mov r12, r13
	mov [rbp - 376], r14
	mov  r14, 40
	add r12, r14
	mov [rbp - 384], r12
	mov [rbp - 200], r13
	mov r13, [rbp - 384]
	mov  r12, r13
	mov [rbp - 392], r14
	mov  r14, r12
	mov [rbp - 400], r12
	mov  r12, 50
	mov [r14], r12
	mov [rbp - 384], r13
	mov [rbp - 408], r14
	mov r14, [rbp - 200]
	mov r13, r14
	mov [rbp - 416], r12
	mov  r12, 48
	add r13, r12
	mov [rbp - 424], r13
	mov [rbp - 200], r14
	mov r14, [rbp - 424]
	mov  r13, r14
	mov [rbp - 432], r12
	mov  r12, r13
	mov [rbp - 440], r13
	mov  r13, 44
	mov [r12], r13
	mov [rbp - 424], r14
	mov [rbp - 448], r12
	mov r12, [rbp - 240]
	mov  r14, r12
	mov rdi, r14
	call _Iprint_pai
	mov [rbp - 456], r13
	mov [rbp - 464], r14
	mov r14, [rbp - 56]
	mov  r13, r14
	mov rdi, r13
	call _IunparseInt_aii
	mov [rbp - 240], r12
	mov  r12, rax
	mov [rbp - 472], r13
	mov  r13, r12
	mov rdi, r13
	call _Iprint_pai
	mov [rbp - 56], r14
	mov  r14, 3
	mov rax, r14
	mov [rbp - 480], r12
	mov  r12, 8
	imul r12
	mov [rbp - 488], r13
	mov r13, rax
	mov [rbp - 496], r14
	mov r14, r13
	mov [rbp - 504], r12
	mov  r12, 8
	add r14, r12
	mov [rbp - 512], r13
	mov  r13, r14
	mov [rbp - 520], r14
	mov  r14, 3
	mov rax, r14
	mov [rbp - 528], r12
	mov  r12, 8
	imul r12
	mov [rbp - 536], r13
	mov r13, rax
	mov [rbp - 544], r14
	mov  r14, r13
	mov [rbp - 552], r12
	mov r12, r14
	mov [rbp - 560], r13
	mov  r13, 8
	add r12, r13
	mov [rbp - 568], r14
	mov  r14, r12
	mov rdi, r14
	call _xi_alloc
	mov [rbp - 576], r12
	mov  r12, rax
	mov [rbp - 584], r13
	mov  r13, r12
	mov [rbp - 592], r14
	mov  r14, r13
	mov [rbp - 600], r12
	mov  r12, 3
	mov [r14], r12
	mov [rbp - 608], r13
	mov [rbp - 616], r14
	mov r14, [rbp - 608]
	mov r13, r14
	mov [rbp - 624], r12
	mov  r12, 8
	add r13, r12
	mov [rbp - 632], r13
	mov [rbp - 608], r14
	mov r14, [rbp - 632]
	mov  r13, r14
	mov [rbp - 640], r12
	mov  r12, r13
	mov [rbp - 648], r13
	mov  r13, 41
	mov [r12], r13
	mov [rbp - 632], r14
	mov [rbp - 656], r12
	mov r12, [rbp - 608]
	mov r14, r12
	mov [rbp - 664], r13
	mov  r13, 16
	add r14, r13
	mov [rbp - 672], r14
	mov [rbp - 608], r12
	mov r12, [rbp - 672]
	mov  r14, r12
	mov [rbp - 680], r13
	mov  r13, r14
	mov [rbp - 688], r14
	mov  r14, 58
	mov [r13], r14
	mov [rbp - 672], r12
	mov [rbp - 696], r13
	mov r13, [rbp - 608]
	mov r12, r13
	mov [rbp - 704], r14
	mov  r14, 24
	add r12, r14
	mov [rbp - 712], r12
	mov [rbp - 608], r13
	mov r13, [rbp - 712]
	mov  r12, r13
	mov [rbp - 720], r14
	mov  r14, r12
	mov [rbp - 728], r12
	mov  r12, 32
	mov [r14], r12
	mov [rbp - 712], r13
	mov [rbp - 736], r14
	mov r14, [rbp - 648]
	mov  r13, r14
	mov rdi, r13
	call _Iprint_pai
	mov [rbp - 744], r12
	mov [rbp - 752], r13
	mov r13, [rbp - 80]
	mov  r12, r13
	mov rdi, r12
	call _IunparseInt_aii
	mov [rbp - 648], r14
	mov  r14, rax
	mov [rbp - 760], r12
	mov  r12, r14
	mov rdi, r12
	call _Iprint_pai
	mov [rbp - 80], r13
	mov  r13, 0
	mov rax, r13
	mov [rbp - 768], r14
	mov  r14, 8
	imul r14
	mov [rbp - 776], r12
	mov r12, rax
	mov [rbp - 784], r13
	mov r13, r12
	mov [rbp - 792], r14
	mov  r14, 8
	add r13, r14
	mov [rbp - 800], r12
	mov  r12, r13
	mov [rbp - 808], r13
	mov  r13, 0
	mov rax, r13
	mov [rbp - 816], r14
	mov  r14, 8
	imul r14
	mov [rbp - 824], r12
	mov r12, rax
	mov [rbp - 832], r13
	mov  r13, r12
	mov [rbp - 840], r14
	mov r14, r13
	mov [rbp - 848], r12
	mov  r12, 8
	add r14, r12
	mov [rbp - 856], r13
	mov  r13, r14
	mov rdi, r13
	call _xi_alloc
	mov [rbp - 864], r14
	mov  r14, rax
	mov [rbp - 872], r12
	mov  r12, r14
	mov [rbp - 880], r13
	mov  r13, r12
	mov [rbp - 888], r14
	mov  r14, 0
	mov [r13], r14
	mov [rbp - 896], r12
	mov [rbp - 904], r13
	mov r13, [rbp - 896]
	mov r12, r13
	mov [rbp - 912], r14
	mov  r14, 8
	add r12, r14
	mov [rbp - 920], r12
	mov [rbp - 896], r13
	mov r13, [rbp - 920]
	mov  r12, r13
	mov [rbp - 928], r14
	mov  r14, r12
	mov rdi, r14
	call _Iprintln_pai
	pop r14
	pop r13
	pop r12
	leave
	ret
_Iusage_p:
	enter 1328, 0
	push r12
	push r13
	push r14
	mov  r12, 29
	mov rax, r12
	mov  r13, 8
	imul r13
	mov r14, rax
	mov [rbp - 40], r12
	mov r12, r14
	mov [rbp - 48], r13
	mov  r13, 8
	add r12, r13
	mov [rbp - 56], r14
	mov  r14, r12
	mov [rbp - 64], r12
	mov  r12, 29
	mov rax, r12
	mov [rbp - 72], r13
	mov  r13, 8
	imul r13
	mov [rbp - 80], r14
	mov r14, rax
	mov [rbp - 88], r12
	mov  r12, r14
	mov [rbp - 96], r13
	mov r13, r12
	mov [rbp - 104], r14
	mov  r14, 8
	add r13, r14
	mov [rbp - 112], r12
	mov  r12, r13
	mov rdi, r12
	call _xi_alloc
	mov [rbp - 120], r13
	mov  r13, rax
	mov [rbp - 128], r14
	mov  r14, r13
	mov [rbp - 136], r12
	mov  r12, r14
	mov [rbp - 144], r13
	mov  r13, 29
	mov [r12], r13
	mov [rbp - 152], r14
	mov [rbp - 160], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 168], r13
	mov  r13, 8
	add r14, r13
	mov [rbp - 176], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 176]
	mov  r14, r12
	mov [rbp - 184], r13
	mov  r13, r14
	mov [rbp - 192], r14
	mov  r14, 80
	mov [r13], r14
	mov [rbp - 176], r12
	mov [rbp - 200], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 208], r14
	mov  r14, 16
	add r12, r14
	mov [rbp - 216], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 216]
	mov  r12, r13
	mov [rbp - 224], r14
	mov  r14, r12
	mov [rbp - 232], r12
	mov  r12, 108
	mov [r14], r12
	mov [rbp - 216], r13
	mov [rbp - 240], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 248], r12
	mov  r12, 24
	add r13, r12
	mov [rbp - 256], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 256]
	mov  r13, r14
	mov [rbp - 264], r12
	mov  r12, r13
	mov [rbp - 272], r13
	mov  r13, 101
	mov [r12], r13
	mov [rbp - 256], r14
	mov [rbp - 280], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 288], r13
	mov  r13, 32
	add r14, r13
	mov [rbp - 296], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 296]
	mov  r14, r12
	mov [rbp - 304], r13
	mov  r13, r14
	mov [rbp - 312], r14
	mov  r14, 97
	mov [r13], r14
	mov [rbp - 296], r12
	mov [rbp - 320], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 328], r14
	mov  r14, 40
	add r12, r14
	mov [rbp - 336], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 336]
	mov  r12, r13
	mov [rbp - 344], r14
	mov  r14, r12
	mov [rbp - 352], r12
	mov  r12, 115
	mov [r14], r12
	mov [rbp - 336], r13
	mov [rbp - 360], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 368], r12
	mov  r12, 48
	add r13, r12
	mov [rbp - 376], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 376]
	mov  r13, r14
	mov [rbp - 384], r12
	mov  r12, r13
	mov [rbp - 392], r13
	mov  r13, 101
	mov [r12], r13
	mov [rbp - 376], r14
	mov [rbp - 400], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 408], r13
	mov  r13, 56
	add r14, r13
	mov [rbp - 416], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 416]
	mov  r14, r12
	mov [rbp - 424], r13
	mov  r13, r14
	mov [rbp - 432], r14
	mov  r14, 32
	mov [r13], r14
	mov [rbp - 416], r12
	mov [rbp - 440], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 448], r14
	mov  r14, 64
	add r12, r14
	mov [rbp - 456], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 456]
	mov  r12, r13
	mov [rbp - 464], r14
	mov  r14, r12
	mov [rbp - 472], r12
	mov  r12, 115
	mov [r14], r12
	mov [rbp - 456], r13
	mov [rbp - 480], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 488], r12
	mov  r12, 72
	add r13, r12
	mov [rbp - 496], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 496]
	mov  r13, r14
	mov [rbp - 504], r12
	mov  r12, r13
	mov [rbp - 512], r13
	mov  r13, 112
	mov [r12], r13
	mov [rbp - 496], r14
	mov [rbp - 520], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 528], r13
	mov  r13, 80
	add r14, r13
	mov [rbp - 536], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 536]
	mov  r14, r12
	mov [rbp - 544], r13
	mov  r13, r14
	mov [rbp - 552], r14
	mov  r14, 101
	mov [r13], r14
	mov [rbp - 536], r12
	mov [rbp - 560], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 568], r14
	mov  r14, 88
	add r12, r14
	mov [rbp - 576], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 576]
	mov  r12, r13
	mov [rbp - 584], r14
	mov  r14, r12
	mov [rbp - 592], r12
	mov  r12, 99
	mov [r14], r12
	mov [rbp - 576], r13
	mov [rbp - 600], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 608], r12
	mov  r12, 96
	add r13, r12
	mov [rbp - 616], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 616]
	mov  r13, r14
	mov [rbp - 624], r12
	mov  r12, r13
	mov [rbp - 632], r13
	mov  r13, 105
	mov [r12], r13
	mov [rbp - 616], r14
	mov [rbp - 640], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 648], r13
	mov  r13, 104
	add r14, r13
	mov [rbp - 656], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 656]
	mov  r14, r12
	mov [rbp - 664], r13
	mov  r13, r14
	mov [rbp - 672], r14
	mov  r14, 102
	mov [r13], r14
	mov [rbp - 656], r12
	mov [rbp - 680], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 688], r14
	mov  r14, 112
	add r12, r14
	mov [rbp - 696], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 696]
	mov  r12, r13
	mov [rbp - 704], r14
	mov  r14, r12
	mov [rbp - 712], r12
	mov  r12, 121
	mov [r14], r12
	mov [rbp - 696], r13
	mov [rbp - 720], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 728], r12
	mov  r12, 120
	add r13, r12
	mov [rbp - 736], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 736]
	mov  r13, r14
	mov [rbp - 744], r12
	mov  r12, r13
	mov [rbp - 752], r13
	mov  r13, 32
	mov [r12], r13
	mov [rbp - 736], r14
	mov [rbp - 760], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 768], r13
	mov  r13, 128
	add r14, r13
	mov [rbp - 776], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 776]
	mov  r14, r12
	mov [rbp - 784], r13
	mov  r13, r14
	mov [rbp - 792], r14
	mov  r14, 116
	mov [r13], r14
	mov [rbp - 776], r12
	mov [rbp - 800], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 808], r14
	mov  r14, 136
	add r12, r14
	mov [rbp - 816], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 816]
	mov  r12, r13
	mov [rbp - 824], r14
	mov  r14, r12
	mov [rbp - 832], r12
	mov  r12, 104
	mov [r14], r12
	mov [rbp - 816], r13
	mov [rbp - 840], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 848], r12
	mov  r12, 144
	add r13, r12
	mov [rbp - 856], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 856]
	mov  r13, r14
	mov [rbp - 864], r12
	mov  r12, r13
	mov [rbp - 872], r13
	mov  r13, 101
	mov [r12], r13
	mov [rbp - 856], r14
	mov [rbp - 880], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 888], r13
	mov  r13, 152
	add r14, r13
	mov [rbp - 896], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 896]
	mov  r14, r12
	mov [rbp - 904], r13
	mov  r13, r14
	mov [rbp - 912], r14
	mov  r14, 32
	mov [r13], r14
	mov [rbp - 896], r12
	mov [rbp - 920], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 928], r14
	mov  r14, 160
	add r12, r14
	mov [rbp - 936], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 936]
	mov  r12, r13
	mov [rbp - 944], r14
	mov  r14, r12
	mov [rbp - 952], r12
	mov  r12, 105
	mov [r14], r12
	mov [rbp - 936], r13
	mov [rbp - 960], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 968], r12
	mov  r12, 168
	add r13, r12
	mov [rbp - 976], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 976]
	mov  r13, r14
	mov [rbp - 984], r12
	mov  r12, r13
	mov [rbp - 992], r13
	mov  r13, 110
	mov [r12], r13
	mov [rbp - 976], r14
	mov [rbp - 1000], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 1008], r13
	mov  r13, 176
	add r14, r13
	mov [rbp - 1016], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 1016]
	mov  r14, r12
	mov [rbp - 1024], r13
	mov  r13, r14
	mov [rbp - 1032], r14
	mov  r14, 112
	mov [r13], r14
	mov [rbp - 1016], r12
	mov [rbp - 1040], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 1048], r14
	mov  r14, 184
	add r12, r14
	mov [rbp - 1056], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 1056]
	mov  r12, r13
	mov [rbp - 1064], r14
	mov  r14, r12
	mov [rbp - 1072], r12
	mov  r12, 117
	mov [r14], r12
	mov [rbp - 1056], r13
	mov [rbp - 1080], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 1088], r12
	mov  r12, 192
	add r13, r12
	mov [rbp - 1096], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 1096]
	mov  r13, r14
	mov [rbp - 1104], r12
	mov  r12, r13
	mov [rbp - 1112], r13
	mov  r13, 116
	mov [r12], r13
	mov [rbp - 1096], r14
	mov [rbp - 1120], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 1128], r13
	mov  r13, 200
	add r14, r13
	mov [rbp - 1136], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 1136]
	mov  r14, r12
	mov [rbp - 1144], r13
	mov  r13, r14
	mov [rbp - 1152], r14
	mov  r14, 32
	mov [r13], r14
	mov [rbp - 1136], r12
	mov [rbp - 1160], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 1168], r14
	mov  r14, 208
	add r12, r14
	mov [rbp - 1176], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 1176]
	mov  r12, r13
	mov [rbp - 1184], r14
	mov  r14, r12
	mov [rbp - 1192], r12
	mov  r12, 115
	mov [r14], r12
	mov [rbp - 1176], r13
	mov [rbp - 1200], r14
	mov r14, [rbp - 152]
	mov r13, r14
	mov [rbp - 1208], r12
	mov  r12, 216
	add r13, r12
	mov [rbp - 1216], r13
	mov [rbp - 152], r14
	mov r14, [rbp - 1216]
	mov  r13, r14
	mov [rbp - 1224], r12
	mov  r12, r13
	mov [rbp - 1232], r13
	mov  r13, 105
	mov [r12], r13
	mov [rbp - 1216], r14
	mov [rbp - 1240], r12
	mov r12, [rbp - 152]
	mov r14, r12
	mov [rbp - 1248], r13
	mov  r13, 224
	add r14, r13
	mov [rbp - 1256], r14
	mov [rbp - 152], r12
	mov r12, [rbp - 1256]
	mov  r14, r12
	mov [rbp - 1264], r13
	mov  r13, r14
	mov [rbp - 1272], r14
	mov  r14, 122
	mov [r13], r14
	mov [rbp - 1256], r12
	mov [rbp - 1280], r13
	mov r13, [rbp - 152]
	mov r12, r13
	mov [rbp - 1288], r14
	mov  r14, 232
	add r12, r14
	mov [rbp - 1296], r12
	mov [rbp - 152], r13
	mov r13, [rbp - 1296]
	mov  r12, r13
	mov [rbp - 1304], r14
	mov  r14, r12
	mov [rbp - 1312], r12
	mov  r12, 101
	mov [r14], r12
	mov [rbp - 1296], r13
	mov [rbp - 1320], r14
	mov r14, [rbp - 192]
	mov  r13, r14
	mov rdi, r13
	call _Iprintln_pai
	pop r14
	pop r13
	pop r12
	leave
	ret
_IAck_iii:
	enter 328, 0
	push r12
	push r13
	push r14
	mov  r12, rdi
	mov  r13, rsi
	mov r14, r12
	mov [rbp - 40], r12
	mov  r12, 0
	cmp r14, r12
	sete  r14b
	and r14b, 1
	movzx r14, r14b
	mov [rbp - 48], r13
	mov  r13, r14
	mov [rbp - 56], r14
	mov  r14, 1
	mov [rbp - 64], r12
	mov r12, r14
	xor r12, r13
	mov [rbp - 72], r13
	mov  r13, 1
	cmp r12, r13
	mov [rbp - 80], r14
	mov [rbp - 88], r12
	mov [rbp - 96], r13
	jge false_5 # switch default
true_4:
	mov r12, [rbp - 48]
	mov r14, r12
	mov  r13, 1
	add r14, r13
	mov [rbp - 104], r14
	mov [rbp - 48], r12
	mov r12, [rbp - 104]
	mov  r14, r12
	mov [rbp - 112], r13
	mov  r13, r14
	mov  rax, r13
	pop r14
	pop r13
	pop r12
	leave
	ret
	mov [rbp - 120], r14
	mov [rbp - 104], r12
	mov [rbp - 128], r13
false_5:
	mov r12, [rbp - 48]
	mov r14, r12
	mov  r13, 0
	cmp r14, r13
	sete  r14b
	and r14b, 1
	movzx r14, r14b
	mov [rbp - 136], r14
	mov [rbp - 48], r12
	mov r12, [rbp - 136]
	mov  r14, r12
	mov [rbp - 144], r13
	mov  r13, 1
	mov [rbp - 152], r14
	mov r14, r13
	mov [rbp - 136], r12
	mov r12, [rbp - 152]
	xor r14, r12
	mov [rbp - 160], r13
	mov  r13, 1
	cmp r14, r13
	mov [rbp - 168], r14
	mov [rbp - 152], r12
	mov [rbp - 176], r13
	jge false_1 # switch default
true_0:
	mov r12, [rbp - 40]
	mov r14, r12
	mov  r13, 1
	sub r14, r13
	mov [rbp - 184], r14
	mov [rbp - 40], r12
	mov r12, [rbp - 184]
	mov  r14, r12
	mov [rbp - 192], r13
	mov  r13, r14
	mov [rbp - 200], r14
	mov  r14, 1
	mov [rbp - 184], r12
	mov  r12, r14
	mov rdi, r13
	mov rsi, r12
	call _IAck_iii
	mov [rbp - 208], r13
	mov  r13, rax
	mov [rbp - 216], r14
	mov  r14, r13
	mov  rax, r14
	pop r14
	pop r13
	pop r12
	leave
	ret
	mov [rbp - 224], r12
	mov [rbp - 232], r13
	mov [rbp - 240], r14
false_1:
	mov r13, [rbp - 40]
	mov r12, r13
	mov  r14, 1
	sub r12, r14
	mov [rbp - 248], r12
	mov r12, [rbp - 200]
	mov [rbp - 40], r13
	mov r13, [rbp - 248]
	mov  r12, r13
	mov [rbp - 256], r14
	mov  r14, r12
	mov [rbp - 200], r12
	mov [rbp - 248], r13
	mov r13, [rbp - 40]
	mov  r12, r13
	mov [rbp - 264], r14
	mov [rbp - 272], r12
	mov r12, [rbp - 48]
	mov r14, r12
	mov [rbp - 40], r13
	mov  r13, 1
	sub r14, r13
	mov [rbp - 280], r14
	mov [rbp - 48], r12
	mov r12, [rbp - 280]
	mov  r14, r12
	mov [rbp - 288], r13
	mov  r13, r14
	mov [rbp - 296], r14
	mov r14, [rbp - 272]
	mov rdi, r14
	mov rsi, r13
	call _IAck_iii
	mov [rbp - 280], r12
	mov  r12, rax
	mov [rbp - 304], r13
	mov  r13, r12
	mov [rbp - 272], r14
	mov r14, [rbp - 264]
	mov rdi, r14
	mov rsi, r13
	call _IAck_iii
	mov [rbp - 312], r12
	mov  r12, rax
	mov [rbp - 320], r13
	mov  r13, r12
	mov  rax, r13
	pop r14
	pop r13
	pop r12
	leave
	ret
