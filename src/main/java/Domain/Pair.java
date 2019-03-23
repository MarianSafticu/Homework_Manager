package Domain;

import java.util.Objects;

public class Pair<E extends Comparable<E>,F extends Comparable<F>> implements Comparable<Pair<E,F>>{
    private E first;
    private F second;
    public Pair(E e1,F e2){
        this.first = e1;
        this.second = e2;
    }
    public E getFirst(){
        return first;
    }
    public F getSecond(){
        return second;
    }
    public void setFirst(E first){
        this.first = first;
    }
    public void getSecond(F second){
        this.second = second;
    }
    public boolean equals(Pair<E,F> ot) {
        return (first.equals(ot.first) && second.equals(ot.second));
    }

    @Override
    public int compareTo(Pair<E, F> o) {
        if(first.compareTo(o.first) == 0){
            return second.compareTo(o.second);
        }else{
            return first.compareTo(o.first);
        }
    }
//    public boolean equals(Pair<E,F> o) {
//        return Objects.equals(first, o.first) &&
//                Objects.equals(second, o.second);
//    }

}
