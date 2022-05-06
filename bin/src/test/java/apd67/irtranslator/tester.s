_Imain_paai:
	enter 56, 0
	mov  r8, r9
true_0:
	mov  r10, 1
	mov [rbp - 40], r8
	mov  r8, r10
_2:
	leave
	ret
false_1:
	mov [rbp - 48], r9
	mov  r9, 0
	mov  r8, r9
	jmp _2
