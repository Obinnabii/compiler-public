use io
use conv

main(args: int[][]) {
    a:int = 1+2+3;
    b:int = 10-2-3;
    c:int = 10/2;
    d:int = 10*2;
    e:int = 10%3;
    f:int = 10*>>3;
    g:bool = 10 < 4;
    h:bool = 10 <= 10;
    i:bool = 10 > 10;
    j:bool = 10 >= 10;
    k:bool = 10 >= 10;
    l:bool = 10 == 10;
    m:bool = 10 != 10;
    n:bool = true == true;
    o:bool = false != true;
    p:bool = true & true;
    q:bool = true | false;
    r:bool = {1, 2} == {1, 2};
    s:bool = {1, 2} != {1, 2, 3};
    t:bool = !true;
    u:int = -10;
    v:int[] = {1, 2} + {3, 4};
    w:int = {3110, 4120}[1];
    x:int;
    //y:int = x;
    // z:int = y;
    // aa:int = a;
    // _ = true; // apparently these are illegal?
    // _ = 1;
    _ = arr();
    // bb:int = 1 + length({1, 2, 3})
    //v[0] = 1;
    arr()[0] = 1;
    cc:int = arr()[1];
    dd:int, ee:bool = multi(1)
    println(unparseInt(a))
    println(unparseInt(b))
    println(unparseInt(c))
    println(unparseInt(d))
}

arr() : int[] {
    return {1, 2, 3}
}

multi(x:int) : int, bool {
    return x, true
}