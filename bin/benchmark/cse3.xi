use io
use conv

main(args:int[][]) {
    a:int = 2
    b:int = 3
    c:int = (a + b) - (a + b) * 4
    d:int = (a + b) - (a + b)
    println(unparseInt(c))
}