use io
use conv

main(args: int[][]) {
    a: int[] = {1, 2}
    q:int = f(a,0)
    u:int = f(a,1)
    println(unparseInt(q))
    println(unparseInt(u))
    //     println(unparseInt(a[2]))
    //     a1:int = f(a,3)-f(a,2)
    //     println(unparseInt(a[2]))
    //     println(unparseInt(a[3]))
    //     a2:int = f(a,2)-f(a,1)
    //     a3:int = f(a,1)-f(a,0)
    
    //     println(unparseInt(a1))
    //     println(unparseInt(a2))
    //     println(unparseInt(a3))
    //     x: int[a1][a2][a3][][]
    //     //x: int[f(a,3)-f(a,2)][f(a,2)-f(a,1)][f(a,1)-f(a,0)][][]
    //     //x: int[1][2][3][][]
    //     println(unparseInt(length(x)))
    //     println(unparseInt(length(x[0])))
    //     println(unparseInt(length(x[0][0])))
    //     println(unparseInt(a[0]))
    //     println(unparseInt(a[1]))
    //     println(unparseInt(a[2]))
    //     println(unparseInt(a[3]))
    // 
}

g(a: int){
    println(unparseInt(a))
}

f(a: int[], i: int): int {
    print("Index: ")
    println(unparseInt(i))
    a[i] = a[i] + 1
    print("Value: ")
    println(unparseInt(a[i]))
    return a[i]
}