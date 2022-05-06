use io

foo(x:int) {
    y:int = 2*x;
}

valid() {
    b: bool = eof();
    foo(2)
    print("yerr");
}