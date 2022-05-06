func1(a : int[], b: int[]) : int {
    x:bool = func2(a, b)
    return 4;
}

func2(c: int[], d: int[]) : bool {
    z:int = func1(c, d)
    return true
}