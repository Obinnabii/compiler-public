valid1() {
    x:int = 3;
    return
}

valid2():int {
    x:int = 3;
    if (x < 4) {
        return x
        } else {
        return 4
    }
}

valid3():bool, int {
    x:int = 3;
    if (x < 4) {
        return true, x
        } else {
        if (x > 0) {
            return x == 2, x
            } else {
            return false, 0
        }
    }
}

valid4(y:int, b:bool) {
    x:int = y;
    return
}

valid5(y:int, b:bool):bool {
    x:int = y;
    return b
}

valid6(x:int): bool, int {
    if (x < 4) {
        return true, x
        } else {
        if (x > 0) {
            return x == 2, x
            } else {
            return false, 0
        }
    }
}