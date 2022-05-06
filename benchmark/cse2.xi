f() : int {
    a:int[] = {1, 2, 3}
    return a[2]
}

aBool() : bool {
    return true
}

main(args:int[][]) {
    b:int = f()
    w:int = b + 1
    if(aBool()) {
        b = 5
        z:int = b + 1
    }
    x:int = b + 1
}