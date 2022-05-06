use io
use conv
// link against ./lib !
main(args:int[][]) {
    x:int = 1
    y:int = x + 1
    z:int = x + 1 + y
    w:int = x + 10 + y + 3 + z + 5
    w2:int = x + 10 + y + 3 + z + 5
    walt:int = x + 10 + y + 3 + z + 55
    v:int = w + w2
    println(unparseInt(w + w2 + walt))
}