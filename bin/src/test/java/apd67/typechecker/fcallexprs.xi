fint() : int {
    return 3;
}

fbool(arr: int[][][]) : bool {
    return length(arr) == 5
}

farr(x:int, y:int, z:int) : bool[][] {
    return {{true, false}, {true}}
}

main() {
    abc:int = fint() - fint() + 12 / fint() * fint() *>> fint() % fint()
    nb:int[1][2][3]

    b:bool = farr(1,2,3)[1][0] & (fbool(nb) | fbool(nb) & !fbool(nb)) & (fint() <= fint() & 2 == fint() & fint() > fint())

    
    nbb:bool[][] = farr(1,2,3) + farr(4,5,6)

}